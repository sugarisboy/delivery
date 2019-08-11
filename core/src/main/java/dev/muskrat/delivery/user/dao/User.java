package dev.muskrat.delivery.user.dao;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.auth.dao.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @OneToOne(targetEntity = AuthorizedUser.class, fetch = FetchType.EAGER)
    private AuthorizedUser authorizedUser;

    @Override
    public void setId(Long id) {
        throw new RuntimeException("Use AuthorizationUser id");
    }

    @Override
    public Long getId() {
        throw new RuntimeException("Use AuthorizationUser id");
    }
}
