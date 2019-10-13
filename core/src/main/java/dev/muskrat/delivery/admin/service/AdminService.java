package dev.muskrat.delivery.admin.service;

import dev.muskrat.delivery.admin.dto.AdminStatsDTO;
import dev.muskrat.delivery.admin.dto.AdminStatsResponseDTO;

public interface AdminService {

    AdminStatsResponseDTO stats(AdminStatsDTO adminStatsDTO);
}
