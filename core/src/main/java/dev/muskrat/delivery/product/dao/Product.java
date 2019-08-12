package dev.muskrat.delivery.product.dao;

import dev.muskrat.delivery.shop.dao.Shop;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
@Table(name = "products")
@Where(clause = "deleted = 0")
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    @Enumerated(EnumType.STRING)
    private Quantities quantity;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "available")
    private Boolean available;
    
    @Column(name = "value")
    private Double value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "product_category",
        joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")}
    )
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "product_shop",
        joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")}
    )
    private Shop shop;

    @Column(name = "deleted")
    private Boolean deleted = false;

}
