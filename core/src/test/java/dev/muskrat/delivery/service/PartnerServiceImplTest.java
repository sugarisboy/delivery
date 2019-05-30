//package dev.muskrat.delivery.service;
//
//import dev.muskrat.delivery.dao.partner.Partner;
//import dev.muskrat.delivery.dao.partner.PartnerRepository;
//import dev.muskrat.delivery.dto.partner.PartnerRegisterDTO;
//import dev.muskrat.delivery.dto.partner.PartnerRegisterResponseDTO;
//import dev.muskrat.delivery.service.partner.PartnerService;
//import org.junit.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.transaction.Transactional;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@Transactional
//@RunWith(SpringRunner.class)
//public class PartnerServiceImplTest {
//
//    @Autowired
//    private PartnerService partnerService;
//
//    @Autowired
//    private PartnerRepository partnerRepository;
//
//    @Test
//    public void registrationTest() {
//        PartnerRegisterDTO partnerRegisterDTO = PartnerRegisterDTO.builder()
//            .email("test@email.com")
//            .name("test")
//            .password("password123")
//            .passwordRepeat("password123")
//            .build();
//
//        PartnerRegisterResponseDTO partnerRegisterResponseDTO
//            = partnerService.create(partnerRegisterDTO);
//        Long id = partnerRegisterResponseDTO.getId();
//
//        List<Partner> all = partnerRepository.findAll();
//
//        assertEquals(all.size(), 1);
//    }
//
//}