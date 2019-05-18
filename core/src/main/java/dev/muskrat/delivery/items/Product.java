package dev.muskrat.delivery.items;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private double price;

    @Getter
    @Setter
    private boolean availability;

    @Getter
    @Setter
    @Enumerated
    private Quantities quantity;

    // Quantity count
    @Getter
    @Setter
    private double value;
}
