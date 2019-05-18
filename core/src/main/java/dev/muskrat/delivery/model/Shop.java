package dev.muskrat.delivery.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class Shop {

    @Id
    private int id;

    @Column
    private String name;

    @Column
    @OneToMany
    private List<Item> items;

}
