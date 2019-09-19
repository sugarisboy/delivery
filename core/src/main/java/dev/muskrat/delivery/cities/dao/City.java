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

    @Column(name = "name")
    private String name;

    @Column(name = "orders")
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "order_city",
        joinColumns = {@JoinColumn(name = "city_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")}
    )
    private List<Order> orders;

    @Column(name = "shops")
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "shop_city",
        joinColumns = {@JoinColumn(name = "city_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")}
    )
    private List<Shop> shops;

    @Column(name = "deleted")
    private Boolean deleted = false;
}
