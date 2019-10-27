package dev.muskrat.delivery.shop.dao;

import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.map.dao.RegionDelivery;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.product.dao.Product;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@Proxy(lazy = false)
@Table(name = "shops")
@Where(clause = "deleted = 0")
@ToString(of = "name")
public class Shop {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JoinTable(name = "partner_shop",
        joinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "partner_id", referencedColumnName = "id")}
    )
    private Partner partner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "shop_city",
        joinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "city_id", referencedColumnName = "id")}
    )
    private City city;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="shop")
    /*@JoinTable(name = "product_shop",
        joinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")}
    )*/
    private List<Product> products;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @ElementCollection
    @Column(name = "open")
    private List<LocalTime> open;

    @ElementCollection
    @Column(name = "close")
    private List<LocalTime> close;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "shop_region",
        joinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "region_id", referencedColumnName = "id")}
    )
    private RegionDelivery region;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "order_shop",
        joinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")}
    )
    private List<Order> orders;
}
