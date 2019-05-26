package dev.muskrat.delivery.dao.shop;

import dev.muskrat.delivery.dao.RegionDelivery;
import dev.muskrat.delivery.dao.WeekSchedule;
import dev.muskrat.delivery.dao.partner.Partner;
import dev.muskrat.delivery.dao.product.Product;
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

    @Column
    private String logo;

    @Column
    private String description;

    @Column
    private float minOrder;

    @Column
    private float freeOrder;

    @Embedded
    private WeekSchedule schedule;

    @Embedded
    private RegionDelivery region;

}
