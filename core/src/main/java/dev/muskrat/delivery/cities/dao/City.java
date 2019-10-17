package dev.muskrat.delivery.cities.dao;


import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.user.dao.User;
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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "order_city",
        joinColumns = {@JoinColumn(name = "city_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")}
    )
    private List<Order> orders;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "shop_city",
        joinColumns = {@JoinColumn(name = "city_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")}
    )
    private List<Shop> shops;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "city_users",
        joinColumns = {@JoinColumn(name = "city_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private List<User> residents;

    @Column(name = "deleted")
    private Boolean deleted = false;
}
