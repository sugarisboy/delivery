package dev.muskrat.delivery.auth.dao;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<AuthorizedUser> users;

    public static enum Name {

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
