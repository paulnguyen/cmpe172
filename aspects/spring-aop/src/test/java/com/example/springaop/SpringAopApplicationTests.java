package com.example.springaop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.springaop.tutorial1.*;
import com.example.springaop.tutorial2.*;
import com.example.springaop.tutorial3.*;

@SpringBootTest
class SpringAopApplicationTests {

    @Autowired
    private Service service;

    @Test
    public void tutorial1() throws InterruptedException {
        service.serve();
    }	

    @Autowired
    private SampleAdder sampleAdder;

    @Test
    public void tutorial2_returnsSucessfully() {
        final int addedValue = sampleAdder.add(12, 12);
        assertEquals(24, addedValue) ;
    }
    
    @Test
    public void tutorial2_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            sampleAdder.add(12, -12) ;
        });
    }

    private Account account;

    @BeforeEach
    public void tutorial3_setup() {
        account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(2000.0);
    }

    @Autowired
    BankAccountService bankAccountService;

    @Test
    void tutorial3_withdraw() {
        bankAccountService.withdraw(account, 500.0);
        assertTrue(account.getBalance() == 1500.0);
    }

    @Test
    void tutorial3_withdrawWhenLimitReached() {
        Assertions.assertThrows(WithdrawLimitException.class, () -> {
            bankAccountService.withdraw(account, 600.0) ;
        });    
        assertTrue(account.getBalance() == 2000.0);
    }

    @Test
    void tutorial3_deposit() {
        bankAccountService.deposit(account, 500.0);
        assertTrue(account.getBalance() == 2500.0);
    }

    @Test
    void tutorial3_getBalance() {
        bankAccountService.getBalance();
    }    

}
