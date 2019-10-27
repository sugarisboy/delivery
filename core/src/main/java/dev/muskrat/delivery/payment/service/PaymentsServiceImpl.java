package dev.muskrat.delivery.payment.service;

import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.payment.dao.PaymentSystemEntity;
import dev.muskrat.delivery.payment.dao.PaymentSystemRepository;
import dev.muskrat.delivery.payment.dto.PaymentSystemCreateDTO;
import dev.muskrat.delivery.payment.dto.PaymentSystemDTO;
import dev.muskrat.delivery.payment.dto.PaymentSystemResponseDTO;
import dev.muskrat.delivery.payment.dto.PaymentSystemUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentsServiceImpl implements PaymentsService {

    private final PaymentSystemRepository repository;

    public PaymentSystemResponseDTO create(PaymentSystemCreateDTO dto) {
        PaymentSystemEntity paymentSystem = new PaymentSystemEntity();
        paymentSystem.setName(dto.getName());
        paymentSystem.setActive(dto.getActive());
        paymentSystem.setOnline(dto.getOnline());

        PaymentSystemEntity save = repository.save(paymentSystem);
        Long systemId = save.getId();

        return PaymentSystemResponseDTO.builder()
            .id(systemId)
            .build();
    }

    public void delete(Long systemId) {
        repository.deleteById(systemId);
    }

    public PaymentSystemDTO findById(Long systemId) {
        PaymentSystemEntity paymentSystem = repository.findById(systemId).orElseThrow(
            () -> new EntityNotFoundException("Payment system with " + systemId + " not found")
        );

        return PaymentSystemDTO.builder()
            .id(paymentSystem.getId())
            .active(paymentSystem.getActive())
            .online(paymentSystem.getOnline())
            .name(paymentSystem.getName())
            .build();
    }

    public PaymentSystemResponseDTO update(PaymentSystemUpdateDTO updateDTO) {
        Long systemId = updateDTO.getId();

        PaymentSystemEntity paymentSystem = repository.findById(systemId).orElseThrow(
            () -> new EntityNotFoundException("Payment system with " + systemId + " not found")
        );

        if (updateDTO.getName() != null) {
            paymentSystem.setName(updateDTO.getName());
        }

        if (updateDTO.getOnline() != null) {
            paymentSystem.setOnline(updateDTO.getOnline());
        }

        if (updateDTO.getActive() != null) {
            paymentSystem.setActive(updateDTO.getActive());
        }

        repository.save(paymentSystem);

        return PaymentSystemResponseDTO.builder()
            .id(paymentSystem.getId())
            .build();
    }
}
