package dev.muskrat.delivery.service;

import dev.muskrat.delivery.model.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;

    public void register() {

    }

}
