package dev.muskrat.delivery.dao.product;

import dev.muskrat.delivery.dao.shop.Shop;
import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "products")
@Data
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

    @Column
    private Category category;

    @ManyToOne(targetEntity = Shop.class)
    private Shop shop;
}
