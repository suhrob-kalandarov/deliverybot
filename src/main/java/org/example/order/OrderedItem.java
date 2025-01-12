package org.example.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderedItem {
    private static Integer genId = 1;
    private Integer id = genId++;

    private Integer orderId;
    private Integer productId;
    private Integer amount;

    public OrderedItem(Integer orderId, Integer productId, Integer amount) {
        this.orderId = orderId;
        this.productId = productId;
        this.amount = amount;
    }
}