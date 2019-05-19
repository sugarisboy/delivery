package dev.muskrat.delivery.dao.product;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
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
