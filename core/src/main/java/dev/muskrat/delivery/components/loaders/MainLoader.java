package dev.muskrat.delivery.components.loaders;

import dev.muskrat.delivery.DemoData;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainLoader {

    private final SecureLoader secureLoader;
    private final DemoData demoData;

    @EventListener
    public void load(ApplicationReadyEvent event) {
        secureLoader.load();
        demoData.load();
    }
}
