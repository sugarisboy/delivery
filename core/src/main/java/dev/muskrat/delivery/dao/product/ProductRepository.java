package dev.muskrat.delivery.dao.product;

import dev.muskrat.delivery.dao.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(int category);

    List<Product> findByShopAndCategory(Shop shop, int category);

    default List<Category> findAllCategory() {
        List<Category> categories = new ArrayList<>();

        categories.add(new Category(0, "Other"));
        categories.add(new Category(1, "Vegetables & Fruits"));
        categories.add(new Category(2, "Milk products"));
        categories.add(new Category(3, "Fish"));
        categories.add(new Category(4, "Meat"));

        return categories;
    }
}
