package dev.muskrat.delivery.payment.dao;

import dev.muskrat.delivery.components.dao.BaseEntity;
import dev.muskrat.delivery.order.dao.Order;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @Column(name = "price")
    private Double price;

    @Column(name = "paid")
    private Boolean paid;


    @ManyToOne
    @JoinTable(name = "transactions_system",
        joinColumns = {@JoinColumn(name = "transaction_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "system_id", referencedColumnName = "id")}
    )
    private PaymentSystemEntity system;

    @OneToOne
    @JoinTable(name = "order_transaction",
        joinColumns = {@JoinColumn(name = "transaction_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")}
    )
    private Order order;
}
