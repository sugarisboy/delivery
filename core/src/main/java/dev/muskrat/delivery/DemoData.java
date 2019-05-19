package dev.muskrat.delivery;

import dev.muskrat.delivery.dao.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DemoData {

    private final ProductRepository productRepository;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {

        productRepository.
        repo.save(new Entity(...));
    }
}
