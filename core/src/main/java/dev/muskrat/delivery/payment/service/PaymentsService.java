package dev.muskrat.delivery.payment.service;

import dev.muskrat.delivery.payment.dto.PaymentSystemCreateDTO;
import dev.muskrat.delivery.payment.dto.PaymentSystemDTO;
import dev.muskrat.delivery.payment.dto.PaymentSystemResponseDTO;
import dev.muskrat.delivery.payment.dto.PaymentSystemUpdateDTO;

public interface PaymentsService {

    PaymentSystemResponseDTO create(PaymentSystemCreateDTO dto);

    void delete(Long systemId);

    PaymentSystemDTO findById(Long systemId);

    PaymentSystemResponseDTO update(PaymentSystemUpdateDTO updateDTO);
}
