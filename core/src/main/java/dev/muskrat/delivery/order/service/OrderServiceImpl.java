package dev.muskrat.delivery.order.service;

import dev.muskrat.delivery.auth.security.jwt.JwtUser;
import dev.muskrat.delivery.cities.dao.CitiesRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.components.events.order.OrderCreateEvent;
import dev.muskrat.delivery.components.events.order.OrderStatusUpdateEvent;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.components.exception.OrderAmountLowerLowestException;
import dev.muskrat.delivery.map.dao.RegionDelivery;
import dev.muskrat.delivery.map.dao.RegionPoint;
import dev.muskrat.delivery.map.service.MappingService;
import dev.muskrat.delivery.order.converter.OrderCreateDTOTOOrderConverter;
import dev.muskrat.delivery.order.converter.OrderStatusTOOrderStatusDTOConverter;
import dev.muskrat.delivery.order.converter.OrderTOOrderDTOConverter;
import dev.muskrat.delivery.order.dao.*;
import dev.muskrat.delivery.order.dto.*;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.product.dao.Product;
import dev.muskrat.delivery.product.dao.ProductRepository;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dao.ShopRepository;
import dev.muskrat.delivery.user.dao.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    private final OrderStatusRepository orderStatusRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderCreateDTOTOOrderConverter orderCreateDTOTOOrderConverter;
    private final OrderTOOrderDTOConverter orderTOOrderDTOConverter;
    private final OrderStatusTOOrderStatusDTOConverter orderStatusTOOrderStatusDTOConverter;

    @Override
    public OrderDTO create(OrderCreateDTO orderDTO) {
        Order order = orderCreateDTOTOOrderConverter.convert(orderDTO);

        Long shopId = orderDTO.getShopId();
        String address = orderDTO.getAddress();
        List<OrderProduct> products = order.getProducts();

        order.setStatus(0);

        double orderCost = 0;
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

            orderCost += product.getPrice() * orderProduct.getCount();
        }
        order.setCost(orderCost);

        // Check shopId and set delivery cost
        Shop shop = shopRepository.findById(shopId).orElseThrow(
            () -> new EntityNotFoundException("Shop with id " + shopId + " not found")
        );
        RegionDelivery region = Optional.of(shop.getRegion()).orElseThrow(
            () -> new EntityNotFoundException("Region for this shop not found!")
        );

        if (orderCost < region.getMinOrderCost())
            throw new OrderAmountLowerLowestException("Order amount lower lowest");

        boolean isFreeDelivery = region.getFreeDeliveryCost() <= orderCost;
        order.setCostAndDelivery(isFreeDelivery ? orderCost : orderCost + region.getDeliveryCost());

        // Check region delivery
        RegionPoint pointByAddress = mappingService.getPointByAddress(address);
        RegionDelivery shopRegion = shop.getRegion();
        boolean regionAvailable = shopRegion.isRegionAvailable(pointByAddress);
        if (!regionAvailable)
            throw new RuntimeException("Out of delivery area");

        City city = shop.getCity();
        order.setCity(city);
        order = orderRepository.save(order);

        OrderCreateEvent orderCreateEvent = new OrderCreateEvent(this, order);
        applicationEventPublisher.publishEvent(orderCreateEvent);

        return orderTOOrderDTOConverter.convert(order);
    }

    @Override
    public OrderDTO updateStatus(OrderUpdateDTO orderDTO) {
        Long id = orderDTO.getId();
        Order order = orderRepository.findById(id)
            .orElseThrow(()-> new EntityNotFoundException("Order with id " + id + " not found"));

        OrderStatusEntry orderStatusEntry = new OrderStatusEntry();
        orderStatusEntry.setUpdatedTime(Instant.now());
        orderStatusEntry.setStatus(orderDTO.getStatus());
        orderStatusEntry.setOrder(order);

        orderStatusRepository.save(orderStatusEntry);
        order.setStatus(orderDTO.getStatus());
        orderRepository.save(order);

        Order order1 = orderRepository.findById(id).get();
        Hibernate.initialize(order1.getOrderStatusLog());

        List<OrderStatusEntryDTO> orderStatusLog = order.getOrderStatusLog().stream()
            .map(orderStatusTOOrderStatusDTOConverter::convert)
            .collect(Collectors.toList());

        return OrderDTO.builder()
            .id(order.getId())
            .status(orderStatusLog)
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

            /*if (requestDTO.getActive() != null) {
                status = requestDTO.getActive() ? ORDERS_NOT_ACTIVE_BEGIN_WITH : status;
            }*/

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
            phone, email, city, shop, pageable
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