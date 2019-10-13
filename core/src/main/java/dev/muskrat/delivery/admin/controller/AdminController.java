package dev.muskrat.delivery.admin.controller;

import dev.muskrat.delivery.admin.dto.AdminStatsDTO;
import dev.muskrat.delivery.admin.dto.AdminStatsResponseDTO;
import dev.muskrat.delivery.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/stats")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AdminStatsResponseDTO stats(
        @Valid @RequestBody AdminStatsDTO adminStatsDTO
        ) {
        return adminService.stats(adminStatsDTO);
    }
}
