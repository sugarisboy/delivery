package dev.muskrat.delivery.admin.controller;

import dev.muskrat.delivery.admin.dto.AdminStatsDTO;
import dev.muskrat.delivery.admin.dto.AdminStatsResponseDTO;
import dev.muskrat.delivery.admin.dto.FrontConfigurationDTO;
import dev.muskrat.delivery.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final FrontConfigurationDTO frontConfigurationDTO;

    @PostMapping("/stats")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AdminStatsResponseDTO stats(
        @Valid @RequestBody AdminStatsDTO adminStatsDTO
        ) {
        return adminService.stats(adminStatsDTO);
    }

    @GetMapping("/config")
    public FrontConfigurationDTO config() {
        return frontConfigurationDTO;
    }
}
