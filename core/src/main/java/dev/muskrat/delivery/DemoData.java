package dev.muskrat.delivery;

import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.auth.dto.UserLoginDTO;
import dev.muskrat.delivery.auth.dto.UserLoginResponseDTO;
import dev.muskrat.delivery.auth.repository.RoleRepository;
import dev.muskrat.delivery.auth.service.AuthorizationService;
import dev.muskrat.delivery.cities.dao.CitiesRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.map.dao.RegionDelivery;
import dev.muskrat.delivery.map.dao.RegionDeliveryRepository;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.order.dao.OrderProduct;
import dev.muskrat.delivery.order.dao.OrderRepository;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.partner.dao.PartnerRepository;
import dev.muskrat.delivery.partner.service.PartnerService;
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
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DemoData {

    private final UserRepository userRepository;
    private final RegionDeliveryRepository regionDeliveryRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PartnerRepository partnerRepository;
    private final CitiesRepository citiesRepository;
    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;

    private final RoleRepository roleRepository;
    private final PartnerService partnerService;
    private final AuthorizationService authorizationService;
    private final UserService userService;

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

    @EventListener
    public void appReady(ApplicationReadyEvent event) {

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
    }

    private void generateUser() {

        Arrays.stream(Role.Name.values())
            .map(Role.Name::getName)
            .forEach(
                roleName -> {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                }
        );

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
        user.setRoles(Arrays.asList(roles.get(0)));
        user = userService.register(user);

        partnerService.create(user);

        // Create admin
        user = new User();
        user.setEmail("admin@gmail.com");
        user.setPassword("test");
        user = userService.register(user);

        user.setRoles(Arrays.asList(roles.get(0), roles.get(2)));
        userRepository.save(user);

        UserLoginResponseDTO userDTO = authorizationService.login(UserLoginDTO.builder().username("user@gmail.com").password("test").build());
        UserLoginResponseDTO partnerDTO = authorizationService.login(UserLoginDTO.builder().username("part@gmail.com").password("test").build());
        UserLoginResponseDTO adminDTO = authorizationService.login(UserLoginDTO.builder().username("admin@gmail.com").password("test").build());

        ACCESS_USER = "Bearer_" + userDTO.getAccess();
        ACCESS_ADMIN = "Bearer_" + adminDTO.getAccess();
        ACCESS_PARTNER = "Bearer_" + partnerDTO.getAccess();

        KEY_USER = userDTO.getKey();
        KEY_ADMIN = adminDTO.getKey();
        KEY_PARTNER = partnerDTO.getKey();
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

        for (int i = 1; i < 3; i++) {
            City city = new City();
            city.setName("city-" + i);
            cities.add(city);
        }

        citiesRepository.saveAll(cities);
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
                Shop shop = new Shop();
                shop.setName(city.getName() + "-shop-" + i);
                shop.setCity(city);
                shop.setPartner(partner);
                shop.setMinOrderPrice(i * 100D);
                shop.setFreeOrderPrice(i * 200D);
                shop.setRegion(generateRegionDelivery());
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
                    product.setPrice(i * 10D);
                    product.setCategory(category);
                    product.setShop(shop);

                    products.add(product);
                }
            }
        }
        productRepository.saveAll(products);
    }

    public void generateOrder() {
        orders = new ArrayList<>();

        Shop shop1 = shopRepository.findAll().get(0);
        List<Product> products = shop1.getProducts();

        for (City city : cities) {
            for (Shop shop : shops) {
                for (int i = 0; i < 3; i++) {
                    Order order = new Order();

                    OrderProduct product1 = new OrderProduct();
                    product1.setProductId(shop.getProducts().get(0).getId());

                    OrderProduct product2 = new OrderProduct();
                    product2.setProductId(shop.getProducts().get(4).getId());

                    OrderProduct product3 = new OrderProduct();
                    product3.setProductId(shop.getProducts().get(8).getId());

                    order.setCity(city);
                    order.setPhone("+7999666335" + i);
                    order.setEmail("mail-" + i + "@cat.frog");
                    order.setAddress("Jalan Teoh Kim Swee, 4");
                    order.setShop(shop);
                    order.setProducts(Arrays.asList(product1, product2, product3));
                    order.setName(shop.getName() + "-order-" + i);

                    if (i == 1) {
                        order.setOrderStatus(1);
                    } else if (i == 2) {
                        order.setOrderStatus(10);
                    }

                    orders.add(order);
                }
            }
        }
        orderRepository.saveAll(orders);
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

        partner = partnerRepository.findAll().get(0);
    }
}
