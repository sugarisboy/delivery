package dev.muskrat.delivery.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Shop {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String name;

    @Column
    @OneToMany
    private List<Product> products;
}
