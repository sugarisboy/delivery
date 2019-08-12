package dev.muskrat.delivery.product.dao;

import dev.muskrat.delivery.auth.dao.BaseEntity;
import dev.muskrat.delivery.shop.dao.Shop;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "categories")
@NoArgsConstructor
public class Category extends BaseEntity {

    @Column(name = "title")
    private String title;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_category",
        joinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")}
    )
    private List<Product> products;

    public Category(String title) {
        this.title = title;
    }
}
