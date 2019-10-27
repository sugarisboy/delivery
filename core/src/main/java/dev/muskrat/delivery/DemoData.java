package dev.muskrat.delivery;

import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.auth.dto.UserLoginDTO;
import dev.muskrat.delivery.auth.dto.UserLoginResponseDTO;
import dev.muskrat.delivery.auth.repository.RoleRepository;
import dev.muskrat.delivery.auth.service.AuthorizationService;
import dev.muskrat.delivery.cities.dao.CitiesRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.components.loaders.SecureLoader;
import dev.muskrat.delivery.map.dao.RegionDelivery;
import dev.muskrat.delivery.map.dao.RegionDeliveryRepository;
import dev.muskrat.delivery.order.converter.OrderProductTOOrderProductDTOConverter;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.order.dao.OrderProduct;
import dev.muskrat.delivery.order.dao.OrderRepository;
import dev.muskrat.delivery.order.dto.OrderCreateDTO;
import dev.muskrat.delivery.order.dto.OrderDTO;
import dev.muskrat.delivery.order.dto.OrderProductDTO;
import dev.muskrat.delivery.order.dto.OrderUpdateDTO;
import dev.muskrat.delivery.order.service.OrderService;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.partner.dao.PartnerRepository;
import dev.muskrat.delivery.partner.service.PartnerService;
import dev.muskrat.delivery.payment.dao.PaymentSystemEntity;
import dev.muskrat.delivery.payment.dao.PaymentSystemRepository;
import dev.muskrat.delivery.payment.dao.Transaction;
import dev.muskrat.delivery.payment.dao.TransactionRepository;
import dev.muskrat.delivery.payment.dto.PaymentSystemCreateDTO;
import dev.muskrat.delivery.payment.dto.PaymentSystemResponseDTO;
import dev.muskrat.delivery.payment.dto.TransactionCreateDTO;
import dev.muskrat.delivery.payment.dto.TransactionResponseDTO;
import dev.muskrat.delivery.payment.service.PaymentsService;
import dev.muskrat.delivery.payment.service.TransactionService;
import dev.muskrat.delivery.product.dao.Category;
import dev.muskrat.delivery.product.dao.CategoryRepository;
import dev.muskrat.delivery.product.dao.Product;
import dev.muskrat.delivery.product.dao.ProductRepository;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dao.ShopRepository;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.repository.UserRepository;
import dev.muskrat.delivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@DependsOn({"secureLoader"})
public class DemoData {

    private final SecureLoader secureLoader;

    private final UserRepository userRepository;
    private final RegionDeliveryRepository regionDeliveryRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PartnerRepository partnerRepository;
    private final CitiesRepository citiesRepository;
    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;
    private final PaymentSystemRepository paymentSystemRepository;
    private final TransactionRepository transactionRepository;

    private final PaymentsService paymentsService;
    private final TransactionService transactionService;
    private final RoleRepository roleRepository;
    private final PartnerService partnerService;
    private final AuthorizationService authorizationService;
    private final OrderService orderService;
    private final UserService userService;

    private final OrderProductTOOrderProductDTOConverter orderProductTOOrderProductDTOConverter;

    public String ACCESS_USER;
    public String ACCESS_ADMIN;
    public String ACCESS_PARTNER;

    public String KEY_USER;
    public String KEY_ADMIN;
    public String KEY_PARTNER;

    public RegionDelivery regionDelivery;
    public Partner partner;

    public List<Category> categories;
    public List<Product> products;
    public List<City> cities;
    public List<Shop> shops;
    public List<Order> orders;
    public List<Role> roles;
    public List<User> users;

    public PaymentSystemEntity paymentSystemCash;
    public Transaction transaction;

    public void load() {
        generateUser();
        update();

        generateCategory();
        update();

        generateCities();
        update();

        generateRegionDelivery();
        update();

        generateShops();
        update();

        generateProducts();
        update();

        generateOrder();
        update();

        generatePaymentSystem();
    }

    private void generatePaymentSystem() {
        PaymentSystemCreateDTO createSystemDTO = PaymentSystemCreateDTO.builder()
            .name("cash")
            .active(true)
            .online(false)
            .build();

        PaymentSystemResponseDTO response = paymentsService.create(createSystemDTO);
        Long systemId = response.getId();

        paymentSystemCash = paymentSystemRepository.findById(systemId).get();



        TransactionCreateDTO createTransactionDTO = TransactionCreateDTO.builder()
            .orderId(orders.get(0).getId())
            .paymentsSystemId(paymentSystemCash.getId())
            .price(100D)
            .build();

        TransactionResponseDTO transactionResponseDTO = transactionService.create(createTransactionDTO);
        Long transactionId = transactionResponseDTO.getId();

        transaction = transactionRepository.findById(transactionId).get();
    }

    private void generateUser() {

        roles = roleRepository.findAll();

        // Create default user
        User user = new User();
        user.setEmail("user@gmail.com");
        user.setPassword("test");
        user.setRoles(Arrays.asList(roles.get(0)));

        userService.register(user);

        // Create partner
        user = new User();
        user.setEmail("part@gmail.com");
        user.setPassword("test");
        user.setRoles(Arrays.asList(roles.get(0), roles.get(1)));
        user = userService.register(user);

        partnerService.create(user);

        UserLoginResponseDTO userDTO = authorizationService.login(UserLoginDTO.builder().username("user@gmail.com").password("test").build());
        UserLoginResponseDTO partnerDTO = authorizationService.login(UserLoginDTO.builder().username("part@gmail.com").password("test").build());

        ACCESS_USER = "Bearer_" + userDTO.getAccess();
        ACCESS_PARTNER = "Bearer_" + partnerDTO.getAccess();
        ACCESS_ADMIN = "Bearer_" + secureLoader.getLoginAdminDTO().getAccess();

        KEY_USER = userDTO.getKey();
        KEY_PARTNER = partnerDTO.getKey();
        KEY_ADMIN = secureLoader.getLoginAdminDTO().getKey();

        System.out.println("Access:\t\t\t" + ACCESS_ADMIN);
        System.out.println("Key:\t\t\t" + KEY_ADMIN);
    }

    private void generateCategory() {
        categories = new ArrayList<>();

        categories.add(new Category("Other"));
        categories.add(new Category("Vegetables & Fruits"));
        categories.add(new Category("Milk products"));

        categoryRepository.saveAll(categories);
    }

    private void generateCities() {
        cities = new ArrayList<>();

        City sibu = new City();
        sibu.setName("Sibu");
        cities.add(sibu);

        for (int i = 1; i < 3; i++) {
            City city = new City();
            city.setName("city-" + i);
            cities.add(city);
        }

        citiesRepository.saveAll(cities);

        City random = cities.get(0);
        for (User user : users) {
            user.setCity(random);
            userRepository.save(user);
        }
    }

    private RegionDelivery generateRegionDelivery() {
        regionDelivery = new RegionDelivery();
        regionDelivery.setAbscissa(Arrays.asList(4.5272D, 4.6335D, 4.5452D, 4.5272D));
        regionDelivery.setOrdinate(Arrays.asList(101.1638D, 101.1250D, 101.0834D, 101.1638D));
        return regionDelivery = regionDeliveryRepository.save(regionDelivery);
    }

    private void generateShops() {
        shops = new ArrayList<>();

        for (City city : cities) {
            for (int i = 0; i < 3; i++) {
                RegionDelivery region = generateRegionDelivery();
                region.setMinOrderCost((i + 1) * 10D);
                region.setDeliveryCost((i + 1) * 15D);
                region.setFreeDeliveryCost((i + 1) * 20D);
                region = regionDeliveryRepository.save(region);

                Shop shop = new Shop();
                shop.setName(city.getName() + "-shop-" + i);
                shop.setCity(city);
                shop.setPartner(partner);
                shop.setRegion(region);
                shop.setOpen(Arrays.asList(
                    LocalTime.of(9, 0),
                    LocalTime.of(9, 0),
                    LocalTime.of(9, 0),
                    LocalTime.of(9, 0),
                    LocalTime.of(9, 0),
                    LocalTime.of(9, 0),
                    LocalTime.of(9, 0))
                );
                shop.setClose(Arrays.asList(
                    LocalTime.of(22, 0),
                    LocalTime.of(22, 0),
                    LocalTime.of(22, 0),
                    LocalTime.of(22, 0),
                    LocalTime.of(22, 0),
                    LocalTime.of(22, 0),
                    LocalTime.of(22, 0))
                );

                shops.add(shop);
            }
        }
        shopRepository.saveAll(shops);
    }

    private void generateProducts() {
        products = new ArrayList<>();

        for (Shop shop : shops) {
            for (Category category : categories) {
                for (int i = 0; i < 3; i++) {
                    Product product = new Product();
                    product.setAvailable(true);
                    product.setDescription("description");
                    product.setTitle(shop.getName() + "-prod-" + i);
                    product.setValue(i * 1D);
                    product.setPrice(i * 10D + 0.90D);
                    product.setCategory(category);
                    product.setShop(shop);

                    products.add(product);
                }
            }
        }
        productRepository.saveAll(products);
    }

    public void generateOrder() {
        for (Shop shop : shops) {
            for (int i = 0; i < 30; i++) {
                OrderProduct product1 = new OrderProduct();
                product1.setProductId(shop.getProducts().get(0).getId());
                product1.setCount(1);

                OrderProduct product2 = new OrderProduct();
                product2.setProductId(shop.getProducts().get(4).getId());
                product2.setCount(2);

                OrderProduct product3 = new OrderProduct();
                product3.setProductId(shop.getProducts().get(8).getId());
                product3.setCount(3);

                List<OrderProductDTO> collect = Stream.of(product1, product2, product3)
                    .map(orderProductTOOrderProductDTOConverter::convert)
                    .collect(Collectors.toList());

                OrderCreateDTO.OrderCreateDTOBuilder build = OrderCreateDTO.builder()
                    .userId(users.get(1).getId())
                    .phone("+7999666335" + i)
                    .email("user@gmail.com")
                    .address("Jalan Teoh Kim Swee, 4")
                    .shopId(shop.getId())
                    .products(collect)
                    .name(shop.getName() + "-order-" + i);

                if (i == 0)
                    build = build.comment("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");

                OrderCreateDTO orderCreateDTO = build.build();
                OrderDTO orderDTO = orderService.create(orderCreateDTO);
                Long orderId = orderDTO.getId();
                Order order = orderRepository.findById(orderId).get();
                order.setCreated(order.getCreated().minusSeconds((long) (5184000L * Math.random())));
                orderRepository.save(order);
            }
        }

        update();

        for (int i = 0; i < 4; i++) {
            for (Order order : orders) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    int status = order.getStatus() == 3 ? 10 : order.getStatus() + 1;
                    orderService.updateStatus(
                        OrderUpdateDTO.builder()
                            .id(order.getId())
                            .status(status)
                            .build()
                    , false);
                    order.setStatus(status);
                }
            }
        }
    }

    public void update() {
        users = userRepository.findAll();
        roles = roleRepository.findAll();
        shops = shopRepository.findAll();
        for (Shop s : shops) {
            Hibernate.initialize(s.getProducts());
        }
        orders = orderRepository.findAll();
        cities = citiesRepository.findAll();
        products = productRepository.findAll();
        categories = categoryRepository.findAll();

        partner = partnerRepository.findByUser(users.get(2)).get();
    }
}
