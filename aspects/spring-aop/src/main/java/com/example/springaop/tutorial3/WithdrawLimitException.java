package com.example.springaop.tutorial3;

public class WithdrawLimitException extends RuntimeException {
    public WithdrawLimitException(String message) {
        super(message);
    }
}


