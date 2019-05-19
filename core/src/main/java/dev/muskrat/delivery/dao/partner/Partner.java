package dev.muskrat.delivery.dao.partner;

import dev.muskrat.delivery.dao.shop.Shop;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Partner {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String password;

    @Column
    private boolean banned;

    @Column
    @OneToMany(targetEntity = Shop.class)
    private List<Shop> shops;
}
