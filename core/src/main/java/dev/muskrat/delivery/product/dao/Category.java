package dev.muskrat.delivery.product.dao;

import dev.muskrat.delivery.components.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "categories")
@NoArgsConstructor
@ToString(of = "title")
public class Category extends BaseEntity {

    @Column(name = "title")
    private String title;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_category",
        joinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")}
    )
    private List<Product> products;

    public Category(String title) {
        this.title = title;
    }
}
