package dev.muskrat.delivery.service.order;

import dev.muskrat.delivery.converter.OrderCreateDTOTOOrderConverter;
import dev.muskrat.delivery.converter.OrderTOOrderDTOConverter;
import dev.muskrat.delivery.dao.order.Order;
import dev.muskrat.delivery.dao.order.OrderProduct;
import dev.muskrat.delivery.dao.order.OrderRepository;
import dev.muskrat.delivery.dao.product.Product;
import dev.muskrat.delivery.dao.product.ProductRepository;
import dev.muskrat.delivery.dao.shop.Shop;
import dev.muskrat.delivery.dao.shop.ShopRepository;
import dev.muskrat.delivery.dto.order.OrderCreateDTO;
import dev.muskrat.delivery.dto.order.OrderDTO;
import dev.muskrat.delivery.dto.order.OrderUpdateDTO;
import dev.muskrat.delivery.dto.product.ProductDTO;
import dev.muskrat.delivery.exception.EntityNotFoundException;
import dev.muskrat.delivery.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final OrderCreateDTOTOOrderConverter orderCreateDTOTOOrderConverter;
    private final OrderTOOrderDTOConverter orderTOOrderDTOConverter;

    @Override
    public OrderDTO create(OrderCreateDTO orderDTO) {
        Order order = orderCreateDTOTOOrderConverter.convert(orderDTO);

        //TODO check region
        //TODO trigger event

        for (OrderProduct orderProduct : order.getProducts()) {
            Long productId = orderProduct.getProductId();
            Optional<Product> byId = productRepository.findById(productId);
            if (byId.isEmpty())
                throw new EntityNotFoundException("Product with id " + productId + " not found");

            Product product = byId.get();
            if (product.getShop().getId() != orderDTO.getShopId())
                throw new RuntimeException("Order contains products from two and more shop");

        }

        orderRepository.save(order);

        return OrderDTO.builder()
            .id(order.getId())
            .status(order.getStatus())
            .build();
    }

    @Override
    public OrderDTO updateStatus(OrderUpdateDTO orderDTO) {
        Long id = orderDTO.getId();
        Optional<Order> byId = orderRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("Order with id " + id + " not found");

        Order order = byId.get();
        order.setStatus(orderDTO.getStatus());
        orderRepository.save(order);

        //TODO: trigger event

        return OrderDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .build();
    }

    @Override
    public Optional<OrderDTO> findById(Long id) {
        Optional<Order> byId = orderRepository.findById(id);
        return byId.map(orderTOOrderDTOConverter::convert);
    }

    @Override
    public Optional<OrderDTO> findByEmail(String email) {
        Optional<Order> byId = orderRepository.findByEmail(email);
        return byId.map(orderTOOrderDTOConverter::convert);
    }

    @Override
    public Optional<List<OrderDTO>> findOrdersByShop(Long shopId) {
        Optional<Shop> byId = shopRepository.findById(shopId);
        if (byId.isEmpty())
            throw new EntityNotFoundException("Shop not found");
        Shop shop = byId.get();

        Optional<List<Order>> byShop = orderRepository.findByShop(shop);
        if (byId.isEmpty())
            return null;

        List<OrderDTO> collect = byShop.get().stream()
            .map(orderTOOrderDTOConverter::convert)
            .collect(Collectors.toList());

        return Optional.of(collect);
    }

}
