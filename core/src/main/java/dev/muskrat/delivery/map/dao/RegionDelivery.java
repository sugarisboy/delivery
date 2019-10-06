package dev.muskrat.delivery.map.dao;

import dev.muskrat.delivery.components.dao.BaseEntity;
import dev.muskrat.delivery.shop.dao.Shop;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "regions")
public class RegionDelivery extends BaseEntity {

    @Column(name = "deliveryCost")
    private Double deliveryCost;

    @Column(name = "minOrderCost")
    private Double minOrderCost;

    @Column(name = "freeDeliveryCost")
    private Double freeDeliveryCost;

    @ElementCollection
    @Column(name = "abscissa")
    private List<Double> abscissa;

    @ElementCollection
    @Column(name = "ordinate")
    private List<Double> ordinate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "shop_region",
        joinColumns = {@JoinColumn(name = "region_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")}
    )
    private Shop shop;

    public boolean isRegionAvailable(RegionPoint point) {
        double x = point.getX();
        double y = point.getY();
        boolean c = false;
        
        for (int i = 0, j = abscissa.size() - 1; i < abscissa.size(); j = i++) {
            if ((((ordinate.get(i) <= y) && (y < ordinate.get(j))) || ((ordinate.get(j) <= y) && (y < ordinate.get(i)))) &&
                (ordinate.get(j) - ordinate.get(i) != 0 && x > (abscissa.get(j) - abscissa.get(i)) * (y - ordinate.get(i)) / (ordinate.get(j) - ordinate.get(i)) + abscissa.get(i)))
                c = !c;
        }
        return c;
    }
}
