package dev.muskrat.delivery.partner.service;

import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.auth.repository.RoleRepository;
import dev.muskrat.delivery.auth.security.jwt.JwtUser;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.order.dao.OrderRepository;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.partner.dao.PartnerRepository;
import dev.muskrat.delivery.partner.dto.PartnerRegisterResponseDTO;
import dev.muskrat.delivery.partner.dto.PartnerStatsDTO;
import dev.muskrat.delivery.shop.converter.ShopToShopDTOConverter;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dao.ShopRepository;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final RoleRepository roleRepository;
    private final PartnerRepository partnerRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final OrderRepository orderRepository;
    private final ShopToShopDTOConverter shopToShopDTOConverter;

    @Override
    public PartnerRegisterResponseDTO create(User user) {
        if (user.getPartner() != null)
            throw new RuntimeException("User already partner");

        Partner partner = new Partner();
        partner.setUser(user);
        partnerRepository.save(partner);

        Role rolePartner = roleRepository.findByName(Role.Name.PARTNER.getName()).get();
        ArrayList<Role> roles = new ArrayList<>(user.getRoles());
        roles.add(rolePartner);
        user.setRoles(roles);
        user.setPartner(partner);
        userRepository.save(user);

        return PartnerRegisterResponseDTO.builder()
            .id(user.getId())
            .build();
    }

    @Override
    public boolean isCurrentPartner(Authentication authentication, Long id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("User with id " + id + " not found");
        User user = byId.get();

        String authorizedUserEmail = user.getEmail();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String jwtUserEmail = jwtUser.getEmail();

        return authorizedUserEmail.equalsIgnoreCase(jwtUserEmail);
    }

    @Override
    public PartnerStatsDTO stats(Long shopId, String type) {
        try {
            if (type == null)
                type = "weekly";

            Shop shop = shopRepository.findById(shopId).orElseThrow(
                () -> new EntityNotFoundException("Shop with id " + shopId + " not found")
            );
            List<Map<String, Object>> data = new ArrayList<>();

            ZonedDateTime timeA, timePrevA, timeB, timePrevB, end;
            DecrementZonedDateTime decrement = null;
            PrintZonedDateTime print = null;
            ToPreviousZonedDateTime prev = null;

            if (type.equalsIgnoreCase("day")) {
                timeA = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).atZone(ZoneId.of("UTC"));
                timeB = timeA.plus(1, ChronoUnit.HOURS);
                end = timeA.minus(47, ChronoUnit.HOURS).minus(1, ChronoUnit.SECONDS);
                decrement = a -> a.minus(1, ChronoUnit.HOURS);
                print = (a, b) -> String.format("%d - %d", a.getHour(), b.getHour());
                prev = a -> a.minus(1, ChronoUnit.DAYS);
            } else if (type.equalsIgnoreCase("weekly")) {
                timeA = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
                timeB = timeA.plus(1, ChronoUnit.DAYS);
                end = timeA.minus(13, ChronoUnit.DAYS).minus(1, ChronoUnit.SECONDS);
                decrement = a -> a.minus(1, ChronoUnit.DAYS);
                print = (a, b) -> a.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                prev = a -> a.minus(7, ChronoUnit.DAYS);
            } else if (type.equalsIgnoreCase("monthly")) {
                timeA = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
                timeB = timeA.plus(1, ChronoUnit.DAYS);
                end = timeA.minus(59, ChronoUnit.DAYS).minus(1, ChronoUnit.SECONDS);
                decrement = a -> a.minus(1, ChronoUnit.DAYS);
                print = (a, b) -> "" + a.getDayOfMonth();
                prev = a -> a.minus(30, ChronoUnit.DAYS);
            } else {
                throw new RuntimeException("Stats type not valid");
            }

            timePrevA = prev.toPrev(timeA);
            timePrevB = prev.toPrev(timeB);

            Double profit;
            PartnerStatsDTO partnerStatsDTO = new PartnerStatsDTO();
            while (timePrevA.isAfter(end)) {
                Map<String, Object> period = new HashMap<>();
                period.put("name", print.print(timeA, timeB));

                profit = orderRepository.getProfitByShop(timeA.toInstant(), timeB.toInstant(), shop);
                period.put("now", Math.round((profit == null ? 0 : profit) * 100) / 100D);

                profit = orderRepository.getProfitByShop(timePrevA.toInstant(), timePrevB.toInstant(), shop);
                period.put("previous", Math.round((profit == null ? 0 : profit) * 100) / 100D);
                data.add(period);
                // break condition
                timeB = timeA;
                timeA = decrement.decrement(timeA);
                timePrevB = timePrevA;
                timePrevA = decrement.decrement(timePrevA);
            }

            Collections.reverse(data);
            partnerStatsDTO.setShopStats(data);
            return partnerStatsDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected interface DecrementZonedDateTime {
        ZonedDateTime decrement(ZonedDateTime time);
    }

    protected interface PrintZonedDateTime {
        String print(ZonedDateTime a, ZonedDateTime b);
    }

    protected interface ToPreviousZonedDateTime {
        ZonedDateTime toPrev(ZonedDateTime time);
    }

    /*
    Stats for many shops

    public PartnerStatsDTO stats(User user, String type) throws AccessDeniedException {
        try {
            //Partner partner = user.getPartner();
            //if (partner == null)
            //    throw new AccessDeniedException("User is not partner");

            if (type == null)
                type = "weekly";

            System.out.println(type);
            //List<String> userRoles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            boolean isAdmin = true; //userRoles.contains("ADMIN");

            List<Shop> shops = isAdmin ? shopRepository.findAll() : shopRepository.findAll();//shopRepository.findAllByPartner(partner);
            Map<Long, ShopDTO> dtoStore = new HashMap<>();
            for (Shop shop : shops) {
                dtoStore.put(shop.getId(), new ShopDTO(shop.getId(), shop.getName()));
            }
            //shops.forEach(shop -> data.put(shop, new ShopDTO(shop.getId(), shop.getName())));

            ZonedDateTime timeA, timeB, end;
            DecrementZonedDateTime decrement = null;
            PrintZonedDateTime print = null;

            if (type.equalsIgnoreCase("day")) {
                timeA = LocalDateTime.now().withMinute(0).withSecond(0).atZone(ZoneId.of("UTC"));
                timeB = timeA.plus(1, ChronoUnit.HOURS);
                end = timeA.minus(47, ChronoUnit.HOURS).minus(1, ChronoUnit.SECONDS);
                decrement = a -> a.minus(1, ChronoUnit.HOURS);
                print = a -> ZonedDateTime.parse(a.toString());
            } else if (type.equalsIgnoreCase("weekly")) {
                timeA = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
                timeB = timeA.plus(1, ChronoUnit.DAYS);
                end = timeA.minus(13, ChronoUnit.DAYS).minus(1, ChronoUnit.SECONDS);
                decrement = a -> a.minus(1, ChronoUnit.DAYS);
                print = a -> ZonedDateTime.parse(a.toString());
            } else if (type.equalsIgnoreCase("monthly")) {
                timeA = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
                timeB = timeA.plus(1, ChronoUnit.DAYS);
                end = timeA.minus(60, ChronoUnit.DAYS).minus(1, ChronoUnit.SECONDS);
                decrement = a -> a.minus(1, ChronoUnit.DAYS);
                print = a -> ZonedDateTime.parse(a.toString());
            } else {
                throw new RuntimeException("Stats type not valid");
            }

            PartnerStatsDTO partnerStatsDTO = new PartnerStatsDTO();
            ShopsPeriodStatsDTO shopsPeriodStatsDTO;
            while (timeA.isAfter(end)) {

                shopsPeriodStatsDTO = new ShopsPeriodStatsDTO();
                shopsPeriodStatsDTO.setTitle(print.print(timeA) + " - " + print.print(timeB));
                Map<ShopDTO, Double> profits = shopsPeriodStatsDTO.getProfits();
                for (Shop shop : shops) {

                    Double profit = orderRepository.getProfitByShop(timeA.toInstant(), timeB.toInstant(), shop);
                    profits.put(dtoStore.get(shop.getId()), profit);
                }
                partnerStatsDTO.getShopStats().add(shopsPeriodStatsDTO);

                // break condition
                timeB = timeA;
                timeA = decrement.decrement(timeA);
            }




            return partnerStatsDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    * */
}