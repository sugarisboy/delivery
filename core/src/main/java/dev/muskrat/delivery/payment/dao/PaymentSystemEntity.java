package dev.muskrat.delivery.payment.dao;

import dev.muskrat.delivery.components.dao.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "paymentSystems")
public class PaymentSystemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "online")
    private Boolean online = true;

    @OneToMany
    @JoinTable(name = "transactions_system",
        joinColumns = {@JoinColumn(name = "system_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "transaction_id", referencedColumnName = "id")}
    )
    private List<Transaction> transactions;
}
