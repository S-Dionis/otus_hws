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

    public List<Banknote> getBanknotes(int count) {
        amount -= count;
        return Collections.nCopies(count, new Banknote(cellNominal));
    }

    public Banknote getBanknote() throws ATMExceptions.NotEnoughBanknotes {
        if (amount > 0) {
            Banknote banknote = new Banknote(cellNominal);
            amount--;
            return banknote;
        } else {
            throw new ATMExceptions.NotEnoughBanknotes();
        }
    }

    public int getAmount() {
        return amount;
    }

    public Nominal getNominal() {
        return cellNominal;
    }
}
