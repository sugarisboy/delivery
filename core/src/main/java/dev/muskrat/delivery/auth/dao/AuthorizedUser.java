package dev.muskrat.delivery.auth.dao;

import dev.muskrat.delivery.components.dao.BaseEntity;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.user.dao.User;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "authusers")
public class AuthorizedUser extends BaseEntity {

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "refresh")
    private String refresh;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authuser_roles",
        joinColumns = {@JoinColumn(name = "authuser_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<Role> roles;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "authuser_partner",
        joinColumns = {@JoinColumn(name = "authuser_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "partner_id", referencedColumnName = "id")}
    )
    private Partner partner;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "authuser_user",
        joinColumns = {@JoinColumn(name = "authuser_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private User user;
}
