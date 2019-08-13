package dev.muskrat.delivery.shop.dao;

import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.map.dao.RegionDelivery;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.product.dao.Product;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "shops")
@Where(clause = "deleted = 0")
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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_shop",
        joinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")}
    )
    private List<Product> products = new ArrayList<>();

    @Column(name = "description")
    private String description;

    @Column(name = "min_order_price")
    private Double minOrderPrice;

    @Column(name = "free_order_price")
    private Double freeOrderPrice;

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
}
