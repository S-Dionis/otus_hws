package ru.dio.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.dio.Banknote;
import ru.dio.Nominal;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    Cell cell;

    @BeforeEach
    void init() {
        cell = new Cell(Nominal.FIVE_HUNDRED);
    }

    @Test
    void putBanknote() {
        Banknote banknoteFive = new Banknote(Nominal.FIVE_HUNDRED);
        Banknote banknoteOne = new Banknote(Nominal.ONE_HUNDRED);

        cell.putBanknote(banknoteOne);
        int expectZeroAmount = cell.getAmount();
        assertEquals(expectZeroAmount, 0);

        cell.putBanknote(banknoteFive);
        assertEquals(1, cell.getAmount());
    }

    @Test
    void putBanknotes() {
        int count = 100;
        Banknote banknoteFive = new Banknote(Nominal.FIVE_HUNDRED);
        Banknote banknoteOne = new Banknote(Nominal.ONE_HUNDRED);

        cell.putBanknotes(banknoteOne, count);
        int expectZeroAmount = cell.getAmount();
        assertEquals(expectZeroAmount, 0);

        cell.putBanknotes(banknoteFive, count);
        assertEquals(count, cell.getAmount());
    }

    @Test
    void getBanknotes() throws ATMExceptions.NotEnoughBanknotes {
        int count = 10;
        Banknote banknoteFive = new Banknote(Nominal.FIVE_HUNDRED);
        cell.putBanknotes(banknoteFive, count);
        assertThrows(ATMExceptions.NotEnoughBanknotes.class, () -> cell.getBanknotes(100));
    }

    @Test
    void getAllBanknotes() {
        int count = 10;
        Banknote banknoteFive = new Banknote(Nominal.FIVE_HUNDRED);
        cell.putBanknotes(banknoteFive, count);
        List<Banknote> allBanknotes = cell.getAllBanknotes();
        assertEquals(count, allBanknotes.size());
        assertEquals(0, cell.getAmount());
    }

}