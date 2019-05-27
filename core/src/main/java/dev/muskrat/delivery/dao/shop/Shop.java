package dev.muskrat.delivery.dao.shop;

import dev.muskrat.delivery.dao.RegionDelivery;
import dev.muskrat.delivery.dao.WorkDay;
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
    private Float minOrder;

    @Column
    private Float freeOrder;

    @ElementCollection
    private List<WorkDay> schedule;

    @Embedded
    private RegionDelivery region;

    @Column
    private Boolean visible;
}
