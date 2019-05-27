package dev.muskrat.delivery.service;

import dev.muskrat.delivery.dao.shop.Shop;
import dev.muskrat.delivery.dao.shop.ShopRepository;
import dev.muskrat.delivery.dto.shop.ShopDTO;
import dev.muskrat.delivery.dto.shop.WorkDayDTO;
import dev.muskrat.delivery.service.shop.ShopService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@RunWith(SpringRunner.class)
public class ShopServiceImplTest {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopService shopService;

    private Shop shop;

    @Before
    public void createShopTest() {
        ShopDTO shopDTO = ShopDTO.builder()
                .name("name")
                .build();
        shopService.create(shopDTO);
        shop = shopRepository.findAll().get(0);

        assertNotNull(shop);
        assertEquals(shop.getName(), "name");
    }

    @Test
    public void updateShopTest() {
        Long id = shop.getId();
        ShopDTO shopDTO = ShopDTO.builder()
                .id(id)
                .name("newname")
                .build();

        shopService.update(shopDTO);
        shop = shopRepository.findById(id).get();

        assertNotNull(shop);
        assertEquals(shop.getName(), "newname");
    }

    @Test
    public void updateShopSettingTest() {
        Shop shop = shopRepository.findAll().get(0);
        Long id = shop.getId();

        List<WorkDayDTO> schedule = Arrays.asList(
                new WorkDayDTO(LocalTime.of(8, 0), LocalTime.of(22, 0)),
                new WorkDayDTO(LocalTime.of(8, 0), LocalTime.of(22, 0)),
                new WorkDayDTO(LocalTime.of(8, 0), LocalTime.of(22, 0)),
                new WorkDayDTO(LocalTime.of(8, 0), LocalTime.of(22, 0)),
                new WorkDayDTO(LocalTime.of(8, 0), LocalTime.of(22, 0)),
                new WorkDayDTO(LocalTime.of(9, 0), LocalTime.of(20, 30)),
                new WorkDayDTO(LocalTime.of(9, 0), LocalTime.of(20, 30))
        );

        ShopDTO shopDTO = ShopDTO.builder()
                .id(id)
                .name("newname")
                .logo("https://upload.wikimedia.org/wikipedia/ru/thumb/7/78/Auchan-logo.svg/1280px-Auchan-logo.svg.png")
                .description("The best shop")
                .freeOrder(500F)
                .minOrder(200F)
                .schedule(schedule)
                .visible(true)
                .build();

        shopService.update(shopDTO);
        shop = shopRepository.findById(id).get();

        assertNotNull(shop);
        assertEquals(shop.getName(), "newname");
    }

    @Test
    public void deleteShopTest() {
        Shop shop = shopRepository.findAll().get(0);
        Long id = shop.getId();
        ShopDTO shopDTO = ShopDTO.builder()
                .id(id)
                .build();
        shopService.delete(shopDTO);

        assertEquals(0, shopRepository.count());
    }

}
