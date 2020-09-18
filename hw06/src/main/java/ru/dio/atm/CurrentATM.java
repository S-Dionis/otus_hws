package ru.dio.atm;

import ru.dio.Banknote;
import ru.dio.Nominal;
import ru.dio.Payment;

import java.util.*;

public class CurrentATM implements ATM {

    private final TreeSet<Cell> cells;

    private CurrentATM(TreeSet<Cell> cells) {
        this.cells = cells;
    }

    public void deposit(Payment payment) {
        final List<Banknote> banknotes = payment.getBanknotes();

        for (int i = 0; i < banknotes.size(); i++) {
            final Banknote banknote = banknotes.get(i);
            final Nominal nominal = banknote.getNominal();
            cells.forEach(cell -> {
                if (cell.getNominal().equals(nominal)) {
                    cell.putBanknote(banknote);
                }
            });
        }
    }

    public Payment withdrawal(int sum) throws ATMExceptions.WithdrawalException {
        final Iterator<Cell> cellIterator = cells.descendingIterator();
        Payment.Builder paymentBuilder = Payment.getBuilder();

        while (cellIterator.hasNext()) {
            final Cell cell = cellIterator.next();
            sum = takeBanknotesIfCan(sum, cell, paymentBuilder);
            if (sum == 0) break;
        }

        Payment payment = paymentBuilder.build();

        if (sum != 0) {
            deposit(payment);
            throw new ATMExceptions.WithdrawalException("Can't withdrawal " + sum);
        }

        return payment;
    }

    private int takeBanknotesIfCan(int sum, Cell cell, Payment.Builder paymentBuilder) {
        int numericNominal = cell.getNominal().getValue();
        int entry = sum % numericNominal;
        if (entry < sum) {
            int needBanknotes = (sum - entry) / numericNominal;
            int canGive = cell.getAmount();
            int takeBanknotes = Math.min(canGive, needBanknotes);

            try {
                paymentBuilder.addBanknotes(cell.getBanknotes(takeBanknotes));
            } catch (ATMExceptions.NotEnoughBanknotes notEnoughBanknotes) {
                takeBanknotes = cell.getAmount();
                paymentBuilder.addBanknotes(cell.getAllBanknotes());
            }

            sum = sum - takeBanknotes * numericNominal;

            /*
            sum = sum - takeBanknotes * numericNominal;

            try {
                paymentBuilder.addBanknotes(cell.getBanknotes(takeBanknotes));
            } catch (ATMExceptions.NotEnoughBanknotes notEnoughBanknotes) {
                notEnoughBanknotes.printStackTrace();
            }

 */
        }
        return sum;
    }

    public int balance() {
        return cells
                .stream()
                .map(cell -> cell.getAmount() * cell.getNominal().getValue())
                .reduce(0, Integer::sum);
    }

    public static ATMBuilder getBuilder() {
        return new ATMBuilder();
    }

    public static class ATMBuilder {

        private final TreeSet<Cell> atmCells = new TreeSet<>();

        public ATMBuilder addCell(Cell cell) {
            atmCells.add(cell);
            return this;
        }

        public ATMBuilder addCells(Cell... cells) {
            Collections.addAll(atmCells, cells);
            return this;
        }

        public CurrentATM build() {
            return new CurrentATM(atmCells);
        }

    }

}