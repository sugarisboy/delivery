package dev.muskrat.delivery.order.dao;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "orders_status_entries")
public class OrderStatusEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time")
    private Instant updatedTime;

    @Column(name = "orderStatusLog")
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "order_status_log",
        joinColumns = {@JoinColumn(name = "status_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")}
    )
    private Order order;
}
