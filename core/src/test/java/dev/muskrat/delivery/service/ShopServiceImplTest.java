//package dev.muskrat.delivery.service;
//
//import dev.muskrat.delivery.dao.shop.Shop;
//import dev.muskrat.delivery.dao.shop.ShopRepository;
//import dev.muskrat.delivery.dto.shop.ShopDTO;
//import dev.muskrat.delivery.service.shop.ShopService;
//import org.junit.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.transaction.Transactional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@Transactional
//@RunWith(SpringRunner.class)
//public class ShopServiceImplTest {
//
//    @Autowired
//    private ShopRepository shopRepository;
//
//    @Autowired
//    private ShopService shopService;
//
//    @Test
//    public void createShopTest() {
//        ShopDTO shopDTO = ShopDTO.builder()
//                .name("name")
//                .build();
//        shopService.create(shopDTO);
//        Shop shop = shopRepository.findAll().get(0);
//
//        assertNotNull(shop);
//        assertEquals(shop.getName(), "name");
//    }
//
//    @Test
//    public void updateShopTest() {
//        ShopDTO shopDTO = ShopDTO.builder()
//                .name("name")
//                .build();
//        shopService.create(shopDTO);
//
//        Shop shop = shopRepository.findAll().get(0);
//        Long id = shop.getId();
//        shopDTO = ShopDTO.builder()
//                .id(id)
//                .name("newname")
//                .build();
//
//        shopService.update(shopDTO);
//        shop = shopRepository.findById(id).get();
//
//        assertNotNull(shop);
//        assertEquals(shop.getName(), "newname");
//    }
//
//    @Test
//    public void deleteShopTest() {
//        ShopDTO shopDTO = ShopDTO.builder()
//                .name("name")
//                .build();
//        shopService.create(shopDTO);
//
//        Shop shop = shopRepository.findAll().get(0);
//        Long id = shop.getId();
//        shopDTO = ShopDTO.builder()
//                .id(id)
//                .build();
//        shopService.delete(shopDTO);
//
//        assertEquals(0, shopRepository.count());
//    }
//
//}
