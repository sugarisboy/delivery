package dev.muskrat.delivery.dao.shop;

import dev.muskrat.delivery.dao.mapping.RegionDelivery;
import dev.muskrat.delivery.dao.partner.Partner;
import dev.muskrat.delivery.dao.product.Product;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Where(clause = "deleted = 0")
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
    private Double minOrderPrice;

    @Column
    private Double freeOrderPrice;

    @ElementCollection
    private List<LocalTime> open;

    @ElementCollection
    private List<LocalTime> close;

    @Embedded
    private RegionDelivery region = RegionDelivery.getEmpty();

    @Column
    private Boolean deleted = false;
}
