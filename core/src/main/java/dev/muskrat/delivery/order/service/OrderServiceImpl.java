package dev.muskrat.delivery.order.service;

import dev.muskrat.delivery.auth.security.jwt.JwtUser;
import dev.muskrat.delivery.cities.dao.CitiesRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.map.dao.RegionDelivery;
import dev.muskrat.delivery.map.dao.RegionPoint;
import dev.muskrat.delivery.map.service.MappingService;
import dev.muskrat.delivery.order.converter.OrderCreateDTOTOOrderConverter;
import dev.muskrat.delivery.order.converter.OrderTOOrderDTOConverter;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.order.dao.OrderProduct;
import dev.muskrat.delivery.order.dao.OrderRepository;
import dev.muskrat.delivery.order.dto.*;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.product.dao.Product;
import dev.muskrat.delivery.product.dao.ProductRepository;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dao.ShopRepository;
import dev.muskrat.delivery.user.dao.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final static Integer ORDERS_NOT_ACTIVE_BEGIN_WITH = 10;

    private final MappingService mappingService;
    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;
    private final CitiesRepository citiesRepository;
    private final ProductRepository productRepository;
    private final OrderCreateDTOTOOrderConverter orderCreateDTOTOOrderConverter;
    private final OrderTOOrderDTOConverter orderTOOrderDTOConverter;

    @Override
    public OrderDTO create(OrderCreateDTO orderDTO) {
        //TODO trigger event

        Order order = orderCreateDTOTOOrderConverter.convert(orderDTO);

        Long shopId = orderDTO.getShopId();
        String address = orderDTO.getAddress();
        List<OrderProduct> products = order.getProducts();

        double orderPrice = 0;
        // Check products
        for (OrderProduct orderProduct : products) {
            Long productId = orderProduct.getProductId();
            Optional<Product> byId = productRepository.findById(productId);
            if (byId.isEmpty()) {
                throw new EntityNotFoundException("Product with id " + productId + " not found");
            }

            Product product = byId.get();
            if (product.getShop().getId() != shopId) {
                throw new RuntimeException("Order contains products from two and more shop");
            }

            orderPrice += product.getPrice() * orderProduct.getCount();
        }
        order.setPrice(orderPrice);

        // Check shopId
        Optional<Shop> byId = shopRepository.findById(shopId);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("Shop with id " + shopId + " not found");
        }
        Shop shop = byId.get();

        // Check region delivery
        RegionPoint pointByAddress = mappingService.getPointByAddress(address);
        RegionDelivery shopRegion = shop.getRegion();
        boolean regionAvailable = shopRegion.isRegionAvailable(pointByAddress);
        if (!regionAvailable) {
            throw new RuntimeException("Out of delivery area");
        }

        City city = shop.getCity();
        order.setCity(city);

        orderRepository.save(order);

        return OrderDTO.builder()
            .id(order.getId())
            .price(orderPrice)
            .status(order.getOrderStatus())
            .createdTime(Date.from(order.getCreated()))
            .build();
    }

    @Override
    public OrderDTO updateStatus(OrderUpdateDTO orderDTO) {
        Long id = orderDTO.getId();
        Optional<Order> byId = orderRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("Order with id " + id + " not found");

        Order order = byId.get();
        order.setOrderStatus(orderDTO.getStatus());
        orderRepository.save(order);

        //TODO: trigger event

        return OrderDTO.builder()
            .id(order.getId())
            .status(order.getOrderStatus())
            .build();
    }

    @Override
    public Optional<OrderDTO> findById(Long id) {
        Optional<Order> byId = orderRepository.findById(id);
        return byId.map(orderTOOrderDTOConverter::convert);
    }

    @Override
    public OrderPageDTO findAll(OrderPageRequestDTO requestDTO, Pageable pageable) {
        Shop shop = null;
        City city = null;
        String phone = null;
        String email = null;
        int status = Integer.MAX_VALUE;

        if (requestDTO != null) {
            if (requestDTO.getPhone() != null)
                phone = requestDTO.getPhone();

            if (requestDTO.getEmail() != null)
                email = requestDTO.getEmail();

            if (requestDTO.getActive() != null) {
                status = requestDTO.getActive() ? ORDERS_NOT_ACTIVE_BEGIN_WITH : status;
            }

            if (requestDTO.getCityId() != null) {
                Long cityId = requestDTO.getCityId();
                Optional<City> byId = citiesRepository.findById(cityId);
                if (byId.isEmpty())
                    throw new EntityNotFoundException("City with id " + cityId + " not found");
                city = byId.get();
            }

            if (requestDTO.getShopId() != null) {
                Long shopId = requestDTO.getShopId();
                Optional<Shop> byId = shopRepository.findById(shopId);
                if (byId.isEmpty())
                    throw new EntityNotFoundException("Shop with id " + shopId + " not found");
                shop = byId.get();
            }
        }

        Page<Order> page = orderRepository.findWithFilter(
            phone, email, city, shop, status, pageable
        );

        List<Order> content = page.getContent();
        List<OrderDTO> collect = content.stream()
            .map(orderTOOrderDTOConverter::convert)
            .collect(Collectors.toList());

        return OrderPageDTO.builder()
            .orders(collect)
            .currentPage(pageable.getPageNumber())
            .lastPage(page.getTotalPages())
            .build();
    }

    @Override
    public boolean isOwnerByOrder(Authentication authentication, Long orderId) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String username = jwtUser.getEmail();

        Optional<Order> byId = orderRepository.findById(orderId);
        if (byId.isEmpty())
            throw new EntityNotFoundException("Order with id " + orderId + " not found");
        Order order = byId.get();
        Shop shop = order.getShop();
        Partner partner = shop.getPartner();
        User user = partner.getUser();
        String email = user.getEmail();

        return email.equalsIgnoreCase(username);
    }

    @Override
    public boolean isClientByUser(Authentication authentication, Long userId) {
        if (userId == null)
            return false;

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long id = jwtUser.getId();

        return id.longValue() == userId;
    }

    @Override
    public boolean isClientByOrder(Authentication authentication, Long orderId) {
        if (orderId == null)
            return false;

        Optional<Order> byId = orderRepository.findById(orderId);
        if (byId.isEmpty())
            throw new EntityNotFoundException("Order with id " + orderId + " not found");
        Order order = byId.get();

        User user = order.getUser();
        return user.getId().longValue() == orderId;
    }

    @Override
    public boolean isOwnerByShop(Authentication authentication, Long shopId) {
        if (shopId == null)
            return false;

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long senderId = jwtUser.getId();

        Optional<Shop> byId = shopRepository.findById(shopId);
        if (byId.isEmpty())
            throw new EntityNotFoundException("Shop with id " + shopId + " not found");
        Shop shop = byId.get();
        Partner partner = shop.getPartner();
        User user = partner.getUser();
        Long shopOwnerId = user.getId();

        return shopOwnerId.longValue() == senderId.longValue();
    }
}