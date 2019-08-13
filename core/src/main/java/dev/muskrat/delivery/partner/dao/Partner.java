package dev.muskrat.delivery.partner.dao;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.components.dao.BaseEntity;
import dev.muskrat.delivery.shop.dao.Shop;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "partners")
public class Partner extends BaseEntity {

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    @JoinTable(name = "partner_shop",
        joinColumns = {@JoinColumn(name = "partner_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")}
    )
    private Set<Shop> shops;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "authuser_partner",
        joinColumns = {@JoinColumn(name = "partner_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authuser_id", referencedColumnName = "id")}
    )
    private AuthorizedUser user;
}
