package dev.muskrat.delivery.products;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class Product {

    interface New {
    }

    interface Exist {
    }

    interface UpdateName extends Exist {
    }

    interface Details {
    }

    interface AdminDetails {
    }

    @Id
    @Null(groups = {New.class})
    @JsonView({Details.class})
    private Long id;

    @NotNull(groups = {New.class, UpdateName.class})
    @JsonView({Details.class})
    private String title;

    @NotNull(groups = {New.class})
    @JsonView({Details.class})
    private double price;

    @Enumerated
    @NotNull(groups = {New.class})
    private Quantities quantity;

    @NotNull(groups = {New.class})
    private String description;

    @NotNull(groups = {New.class})
    private String url;

    @NotNull(groups = {New.class})
    @JsonView({Details.class})
    private boolean availability;

    @NotNull(groups = {New.class})
    private double value;
}
