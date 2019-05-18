package dev.muskrat.delivery.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Shop {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @ManyToOne(targetEntity = Partner.class)
    private Partner partner;

    @OneToMany(targetEntity = Product.class)
    private List<Product> products;

}
