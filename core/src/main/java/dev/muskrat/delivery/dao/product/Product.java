package dev.muskrat.delivery.dao.product;

import dev.muskrat.delivery.dao.shop.Shop;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;


@Entity
@Data
@Table(name = "products")
@Where(clause = "deleted = false")
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
    private String imageUrl;
    
    @Column
    private Boolean available;
    
    @Column
    private Double value;

    @ManyToOne(targetEntity = Category.class)
    private Category category;

    @ManyToOne(targetEntity = Shop.class)
    private Shop shop;

    private Boolean deleted = false;
}
