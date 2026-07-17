package com.example.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long productId;
    private String productName;
    private Double price;
    private String image;
    private int quantity;

    
    public double getSubtotal() {
        return price * quantity;
    }
}