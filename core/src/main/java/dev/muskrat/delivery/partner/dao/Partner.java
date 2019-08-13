package dev.muskrat.delivery.partner.dao;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.auth.dao.BaseEntity;
import dev.muskrat.delivery.shop.dao.Shop;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
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
