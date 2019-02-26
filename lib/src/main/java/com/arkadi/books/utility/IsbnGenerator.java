package com.arkadi.books.utility;

import java.util.Random;

public class IsbnGenerator implements NumberGenerator {
    @Override
    public String generateNumber() {
        return "13-45" + Math.abs(new Random().nextInt());
    }
}
