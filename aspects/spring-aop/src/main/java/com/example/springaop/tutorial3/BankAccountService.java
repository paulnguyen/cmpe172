package com.example.springaop.tutorial3;

import org.springframework.stereotype.Component;

@Component
public class BankAccountService {

    @AccountOperation(operation = "deposit")
    public void deposit(Account account, Double amount) {
        account.setBalance(account.getBalance() + amount);
    }

    @AccountOperation(operation = "withdraw")
    public void withdraw(Account account, Double amount) throws WithdrawLimitException {

        if (amount > 500.0) {
            throw new WithdrawLimitException("Withdraw limit exceeded.");
        }

        account.setBalance(account.getBalance() - amount);

    }

    public double getBalance() {
        return 1500.00 ; // change to randown
    }

}

