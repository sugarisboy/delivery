package dev.muskrat.delivery.dao.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "categories")
@NoArgsConstructor
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
