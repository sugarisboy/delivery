package dev.muskrat.delivery.order.dao;

import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.shop.dao.Shop;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @ManyToOne(targetEntity = City.class)
    private City city;

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

    @CreationTimestamp
    private LocalDateTime createdDate;
}
