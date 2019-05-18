package dev.muskrat.delivery.dao;

import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private double price;

    @Enumerated
    private Quantities quantity;
    
    @Column
    private String description;
    
    @Column
    private String imageUrl;
    
    @Column
    private boolean available;
    
    @Column
    private double cost;

    @ManyToOne(targetEntity = Shop.class)
    private Shop shop;
}
