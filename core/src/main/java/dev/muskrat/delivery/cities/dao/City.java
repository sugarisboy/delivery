package dev.muskrat.delivery.cities.dao;


import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.shop.dao.Shop;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "cities")
@Where(clause = "deleted = 0")
public class City {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;


    @OneToMany(targetEntity = Shop.class)
    private List<Shop> shops;

    @Column
    private Boolean deleted = false;
}
