package dev.muskrat.delivery.dao.product;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "categories")
public class Category {

    public Category() {

    }

    public Category(String title) {
        this.title = title;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String title;

}
