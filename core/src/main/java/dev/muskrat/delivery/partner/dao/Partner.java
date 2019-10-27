package dev.muskrat.delivery.partner.dao;

import dev.muskrat.delivery.components.dao.BaseEntity;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.user.dao.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "partners")
public class Partner extends BaseEntity {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "partner_shop",
        joinColumns = {@JoinColumn(name = "partner_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")}
    )
    private Set<Shop> shops;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "partner")
    /*@JoinTable(name = "user_partner",
        joinColumns = {@JoinColumn(name = "partner_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )*/
    private User user;
}
