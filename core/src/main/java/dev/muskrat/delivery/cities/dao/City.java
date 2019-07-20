package dev.muskrat.delivery.cities.dao;


import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cities")
@Where(clause = "deleted = 0")
public class City {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private Boolean deleted = false;
}
