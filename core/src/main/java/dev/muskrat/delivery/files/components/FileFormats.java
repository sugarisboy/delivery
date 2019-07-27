package dev.muskrat.delivery.files.components;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class FileFormats {

    @Value("${application.storage.upload.main}")
    private String mainDir;

    @Value("${application.storage.upload.product}")
    private String directoryProduct;

    @Getter
    private FileFormat product;

    @Value("${application.storage.upload.shop}")
    private String directoryShop;

    @Getter
    private FileFormat shop;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        product = new FileFormat(mainDir, directoryProduct);
        shop = new FileFormat(mainDir, directoryShop);
    }
}
