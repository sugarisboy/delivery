package dev.muskrat.delivery;

import dev.muskrat.delivery.cities.dao.CitiesRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.map.dao.RegionDelivery;
import dev.muskrat.delivery.map.dao.RegionPoint;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.order.dao.OrderProduct;
import dev.muskrat.delivery.order.dao.OrderRepository;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.partner.dao.PartnerRepository;
import dev.muskrat.delivery.product.dao.Category;
import dev.muskrat.delivery.product.dao.CategoryRepository;
import dev.muskrat.delivery.product.dao.Product;
import dev.muskrat.delivery.product.dao.ProductRepository;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dao.ShopRepository;
import lombok.RequiredArgsConstructor;
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

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PartnerRepository partnerRepository;
    private final CitiesRepository citiesRepository;
    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;

    public RegionDelivery regionDelivery;
    public Partner partner;

    public List<Category> categories;
    public List<Product> products;
    public List<City> cities;
    public List<Shop> shops;
    public List<Order> orders;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        generatePartner();
        generateCategory();
        generateCities();
        generateRegionDelivery();
        generateShops();
        generateProducts();
        generateOrder();
    }

    private void generatePartner() {
        partner = new Partner();
        partner.setName("test");
        partner.setEmail("test@test.te");
        partner.setPhone("000000");
        partner.setPassword("uududff");
        partner.setBanned(false);
        partner.setShops(null);
        partnerRepository.save(partner);
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

    private void generateRegionDelivery() {
        regionDelivery = new RegionDelivery();

        RegionPoint point1 = new RegionPoint();
        point1.setX(4.5272D);
        point1.setY(101.1638D);

        RegionPoint point2 = new RegionPoint();
        point1.setX(4.6335D);
        point1.setY(101.1250D);

        RegionPoint point3 = new RegionPoint();
        point1.setX(4.5452D);
        point1.setY(101.0834D);

        RegionPoint point4 = new RegionPoint();
        point1.setX(4.5272D);
        point1.setY(101.1638D);

        regionDelivery.setPoints(Arrays.asList(point1, point2, point3, point4));
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
                shop.setRegion(regionDelivery);
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
                    product.setImageUrl("url");
                    product.setValue(i * 1D);
                    product.setPrice(i * 10D);
                    product.setCategory(category);
                    shop.getProducts().add(product);

                    products.add(product);
                }
            }
        }
        productRepository.saveAll(products);
    }

    private void generateOrder() {
        orders = new ArrayList<>();

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
                        order.setStatus(1);
                    } else if (i == 2) {
                        order.setStatus(10);
                    }

                    orders.add(order);
                }
            }
        }
        orderRepository.saveAll(orders);
    }
}
