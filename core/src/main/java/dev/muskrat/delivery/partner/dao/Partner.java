package dev.muskrat.delivery.partner.dao;

import dev.muskrat.delivery.shop.dao.Shop;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "partners")
@Where(clause = "banned = 0")
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
    private Boolean banned;

    @Column
    @OneToMany(targetEntity = Shop.class)
    private List<Shop> shops;
}
