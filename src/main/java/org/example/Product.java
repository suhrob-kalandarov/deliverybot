package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Product {
    private static Integer idGen = 1;
    private Integer id = idGen++;
    private String name;
    private Integer categoryId;
    private Integer price;

    public Product(String name, Integer categoryId, Integer price) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
    }
}