package dev.muskrat.delivery.products;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;


@Entity
@Table(name = "products")
@Data
public class ProductDTO {

    @Id
    @Null
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;
    @NotNull
    private double price;

    @Enumerated
    private Quantities quantity;

    private String description;
    private String url;
    private boolean availability;
    private double value;
}
