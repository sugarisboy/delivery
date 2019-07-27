package dev.muskrat.delivery.product.dao;

import dev.muskrat.delivery.shop.dao.Shop;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;


@Entity
@Data
@Table(name = "products")
@Where(clause = "deleted = 0")
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String title;

    @Column
    private Double price;

    @Column
    @Enumerated
    private Quantities quantity;
    
    @Column
    private String description;
    
    @Column
    private Boolean available;
    
    @Column
    private Double value;

    @ManyToOne(targetEntity = Category.class)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Column
    private Boolean deleted = false;

}
