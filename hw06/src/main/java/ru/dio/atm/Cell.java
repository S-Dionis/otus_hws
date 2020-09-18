package ru.dio.atm;

import ru.dio.Banknote;
import ru.dio.Nominal;
import ru.dio.Payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cell implements Comparable<Cell> {

    private final Nominal cellNominal;
    private int amount;

    public Cell(Nominal nominal) {
        this.cellNominal = nominal;
    }

    @Override
    public int compareTo(Cell o) {
        return cellNominal.compareTo(o.cellNominal);
    }

    public void putBanknote(Banknote banknote) {
        if (banknote.getNominal().equals(cellNominal)) {
            amount++;
        }
    }

    public void putBanknotes(Banknote banknote, int count) {
        if (banknote.getNominal().equals(cellNominal)) {
            amount += count;
        }
    }

    public List<Banknote> getBanknotes(int count) throws ATMExceptions.NotEnoughBanknotes {
        if (amount < count) {
            throw new ATMExceptions.NotEnoughBanknotes();
        }
        amount -= count;
        return Collections.nCopies(count, new Banknote(cellNominal));
    }

    public List<Banknote> getAllBanknotes() {
        List<Banknote> banknotes = Collections.nCopies(amount, new Banknote(cellNominal));
        amount = 0;
        return banknotes;
    }

    public int getAmount() {
        return amount;
    }

    public Nominal getNominal() {
        return cellNominal;
    }
}
