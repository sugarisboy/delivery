package dev.muskrat.delivery.admin.service;

import dev.muskrat.delivery.admin.dto.AdminStatsDTO;
import dev.muskrat.delivery.admin.dto.AdminStatsResponseDTO;
import dev.muskrat.delivery.order.dao.OrderRepository;
import dev.muskrat.delivery.shop.dto.ShopStatsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final OrderRepository orderRepository;

    @Override
    public AdminStatsResponseDTO stats(AdminStatsDTO adminStatsDTO) {
        Instant startDate = adminStatsDTO.getStartDate();
        Instant endDate = adminStatsDTO.getEndDate();

        Double profit = orderRepository.getProfit(startDate, endDate);

        return AdminStatsResponseDTO.builder()
            .profit(profit)
            .build();
    }
}
