package dev.muskrat.delivery.order.dao;

import dev.muskrat.delivery.components.dao.BaseEntity;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.user.dao.User;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "products")
    @ElementCollection
    private List<OrderProduct> products;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "order_city",
        joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "city_id", referencedColumnName = "id")}
    )
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "user_orders",
        joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private User user;

    @Column(name = "price")
    private Double price;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "comments")
    private String comments;

    @CreatedDate
    @Column(name = "created")
    private Instant created;

    @Column(name = "order_status")
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "order_status_log",
        joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "status_id", referencedColumnName = "id")}
    )
    private List<OrderStatusEntry> orderStatusLog;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "order_shop",
        joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")}
    )
    private Shop shop;
}
