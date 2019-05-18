package dev.muskrat.delivery.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private Long id;

    private String title;

    private double price;

    private String description;

    private String url;

    private boolean availability;

    private double value;

}
