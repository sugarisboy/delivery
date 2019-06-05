package dev.muskrat.delivery.dao.product;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String title;

    public Category(String title) {
        this.title = title;
    }
}
