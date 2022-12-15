package by.paramonov.entity;

import lombok.Data;

@Data
public class Product extends BaseEntity{
    private String description;
    private double price;
    private boolean isPromotion;
//    private String category;
}
