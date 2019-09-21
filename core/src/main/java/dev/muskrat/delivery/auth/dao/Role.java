package dev.muskrat.delivery.auth.dao;

import dev.muskrat.delivery.components.dao.BaseEntity;
import dev.muskrat.delivery.user.dao.User;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

    public enum Name {

        USER("USER"),
        PARTNER("PARTNER"),
        ADMIN("ADMIN");

        @Getter
        private String name;

        Name(String name) {
            this.name = name;
        }
    }
}
