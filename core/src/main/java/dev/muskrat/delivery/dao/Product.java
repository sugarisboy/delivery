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

    private String description;
    private String url;
    private boolean availability;
    private double value;

    @ManyToOne
    private Shop shop;
}
