package dev.muskrat.delivery.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Partner {

    @Id
    private int id;

    @Column
    @Getter
    @Setter
    private String name;

    @Column
    private String email;

}
