package dev.muskrat.delivery.user.dao;

import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.components.dao.BaseEntity;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.partner.dao.Partner;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@ToString(of = "username")
public class User extends BaseEntity {

    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<Role> roles;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "user_partner",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "partner_id", referencedColumnName = "id")}
    )
    //@JoinColumn(name = "partner_id")
    private Partner partner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "city_users",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "city_id", referencedColumnName = "id")}
    )
    private City city;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_orders",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")}
    )
    private List<Order> orders;
}
