package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Stack;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {
    private Long chatId;
    private String Contact;
    private Float latitude;
    private Float Longitude;
    private State state = State.START;
    private Integer counter = 1;
    private Integer messageId;
    private Stack<String> navigationHistory;
    private Category category;
}