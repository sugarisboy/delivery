package dev.muskrat.delivery.partner.controller;

import dev.muskrat.delivery.partner.dto.PartnerRegisterResponseDTO;
import dev.muskrat.delivery.partner.dto.PartnerStatsDTO;
import dev.muskrat.delivery.partner.service.PartnerService;
import dev.muskrat.delivery.user.converter.AuthIdToAuthorizedUserConverter;
import dev.muskrat.delivery.user.converter.JwtAuthorizationToUserConverter;
import dev.muskrat.delivery.user.dao.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;
    private final AuthIdToAuthorizedUserConverter authIdToAuthorizedUserConverter;
    private final JwtAuthorizationToUserConverter jwtAuthorizationToUserConverter;

    @PostMapping("/create/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PartnerRegisterResponseDTO register(
        @NotNull @PathVariable Long userId
    ) {
        User user = authIdToAuthorizedUserConverter.convert(userId);
        return partnerService.create(user);
    }

    @ApiOperation("type = daily/weekly/monthly")
    @PostMapping("/stats/{shopId}")
    //@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PARTNER')")
    public PartnerStatsDTO stats(
        @RequestParam(value = "type", required = false, defaultValue = "weekly") String type,
        @PathVariable("shopId") Long shopId
    ) throws AccessDeniedException {
        return partnerService.stats(shopId, type);
    }
}
