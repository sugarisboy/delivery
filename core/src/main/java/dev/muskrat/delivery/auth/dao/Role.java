package dev.muskrat.delivery.auth.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<AuthorizedUser> users;
}
