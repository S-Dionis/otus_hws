package ru.dio.atm;

import ru.dio.Payment;

public interface ATM {
    void deposit(Payment payment);
    Payment withdrawal(int sum) throws ATMExceptions.WithdrawalException;
    int balance();
}
