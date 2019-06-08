package dev.muskrat.delivery.dao.order;

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

    //@Embedded
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
    private String token;

    @Column
    private String comments;
}
