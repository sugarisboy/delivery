package dev.muskrat.delivery.dto;

import lombok.Getter;

public class ProductDTO {

    @Getter
    private Long id;

    @Getter
    private String title;

    @Getter
    private double price;

    @Getter
    private String description;
    @Getter
    private String url;
    @Getter
    private boolean availability;
    @Getter
    private double value;
}
