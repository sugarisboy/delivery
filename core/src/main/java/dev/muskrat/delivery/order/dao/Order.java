package dev.muskrat.delivery.order.dao;

import dev.muskrat.delivery.shop.dao.Shop;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    private List<OrderProduct> products;

    @Column
    private String phone;

    @Column
    private String address;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String comments;

    @Column
    private Integer status = 0;

    @ManyToOne(targetEntity = Shop.class)
    private Shop shop;
}
