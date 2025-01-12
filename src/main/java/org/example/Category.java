package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private static Integer genId = 1;
    private Integer id = genId++;
    private String name;

    public Category(String name) {
        this.name = name;
    }
}
