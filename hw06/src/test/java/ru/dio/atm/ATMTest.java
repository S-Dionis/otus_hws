package ru.dio.atm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.dio.Banknote;
import ru.dio.Nominal;
import ru.dio.Payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ATMTest {

    @DisplayName("Deposit on account")
    @Test
    void deposit() {
        Nominal nominal100 = Nominal.ONE_HUNDRED;
        Nominal nominal500 = Nominal.FIVE_HUNDRED;
        Cell cell100 = new Cell(nominal100);
        Cell cell500 = new Cell(nominal500);
        Banknote banknote100 = new Banknote(nominal100);
        Banknote banknote500 = new Banknote(nominal500);
        int count = 1;
        ATM atm = ATM.getBuilder()
                .addCells(cell100, cell500)
                .build();

        int expectedBalance = nominal100.getValue() * count + nominal500.getValue() * count;


        Payment paymet = Payment.getBuilder()
                .addBanknote(banknote100, count)
                .addBanknote(banknote500, count)
                .build();

        atm.deposit(paymet);
        int actualBalance = atm.balance();
        assertEquals(expectedBalance, actualBalance);
    }

    @DisplayName("Expected balance equals actual")
    @Test
    void balance() {
        final int count = 10;
        Nominal nominal = Nominal.ONE_HUNDRED;
        int expectedBalance = nominal.getValue() * count;

        Cell cell = new Cell(nominal);
        cell.putBanknotes(new Banknote(Nominal.ONE_HUNDRED), count);

        ATM atm = ATM.getBuilder().addCell(cell).build();
        int actualBalance = atm.balance();

        assertEquals(expectedBalance, actualBalance);
    }

    @DisplayName("WithdrawalException on incorrect sum withdrawal")
    @Test
    void withdrawalError() throws ATMExceptions.WithdrawalException {
        Nominal nominal100 = Nominal.ONE_HUNDRED;
        Cell cell100 = new Cell(nominal100);
        cell100.putBanknotes(new Banknote(Nominal.ONE_HUNDRED), 2);
        ATM atm = ATM.getBuilder()
                .addCells(cell100)
                .build();
        int beforeBalance = atm.balance();
        assertThrows(ATMExceptions.WithdrawalException.class, () -> atm.withdrawal(6400));
        assertThrows(ATMExceptions.WithdrawalException.class, () -> atm.withdrawal(150));
        int afterBalance = atm.balance();
        assertEquals(beforeBalance, afterBalance);
    }

    @Test
    void withdrawal() throws ATMExceptions.WithdrawalException {
        int count = 1;

        Nominal nominal100 = Nominal.ONE_HUNDRED;
        Nominal nominal500 = Nominal.FIVE_HUNDRED;
        Nominal nominal1000 = Nominal.TEN_HUNDRED;
        Nominal nominal5000 = Nominal.FIFTY_HUNDRED;

        Cell cell100 = new Cell(nominal100);
        Cell cell500 = new Cell(nominal500);
        Cell cell1000 = new Cell(nominal1000);
        Cell cell5000 = new Cell(nominal5000);

        Banknote banknote100 = new Banknote(nominal100);
        Banknote banknote500 = new Banknote(nominal500);
        Banknote banknote1000 = new Banknote(nominal1000);
        Banknote banknote5000 = new Banknote(nominal5000);

        cell100.putBanknotes(banknote100, count);
        cell500.putBanknotes(banknote500, count);
        cell1000.putBanknotes(banknote1000, count);
        cell5000.putBanknotes(banknote5000, count);

        ATM atm = ATM.getBuilder()
                .addCells(cell100, cell500, cell1000, cell5000)
                .build();

        int expectedBalance = nominal100.getValue() * count
                + nominal500.getValue() * count
                + nominal1000.getValue() * count
                + nominal5000.getValue() * count;

        int actualBalance = atm.balance();
        assertEquals(expectedBalance, actualBalance);

        int expectedSum = 6000;
        Payment withdrawal = atm.withdrawal(expectedSum);
        int newActualBalance = atm.balance();

        int actualSum = withdrawal.getSum();
        int newExpectedBalance = expectedBalance - expectedSum;
        assertEquals(expectedSum, actualSum);
        assertEquals(newExpectedBalance, newActualBalance);
    }

}