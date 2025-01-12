package org.example.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Order {
    private static Integer idGen = 1;
    private Integer id = idGen++;

    private OrderStatus status = OrderStatus.CREATED;
    private Date date = new Date();
    private Long userChatId;

    public Order(Long userChatId) {
        this.userChatId = userChatId;
    }
}