package dev.muskrat.delivery;

import dev.muskrat.delivery.dao.product.Category;
import dev.muskrat.delivery.dao.product.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DemoData {

    private final CategoryRepository categoryRepository;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        List<Category> categories = new ArrayList<>();

        categories.add(new Category("Other"));
        categories.add(new Category("Vegetables & Fruits"));
        categories.add(new Category("Milk products"));
        categories.add(new Category("Fish"));
        categories.add(new Category("Meat"));

        categoryRepository.saveAll(categories);
    }
}
