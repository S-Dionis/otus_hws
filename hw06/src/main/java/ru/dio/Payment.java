package ru.dio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Payment {

    private final List<Banknote> banknotes = new ArrayList<>();

    private Payment(List<Banknote> banknotes) {
        this.banknotes.addAll(banknotes);
    }

    public List<Banknote> getBanknotes() {
        return banknotes;
    }

    public static Payment.Builder getBuilder() {
        return new Builder();
    }

    public int getSum() {
        return banknotes.stream()
                .map(banknote -> banknote.getNominal().getValue())
                .reduce(0, Integer::sum);
    }

    public static class Builder {
        private final List<Banknote> banknoteArrayList = new ArrayList<>();

        public Builder addBanknote(Banknote banknote, int count) {
            banknoteArrayList.addAll(Collections.nCopies(count, banknote));
            return this;
        }

        public Builder addBanknotes(List<Banknote> banknotes) {
            banknoteArrayList.addAll(banknotes);
            return this;
        }

        public Payment  build() {
            return new Payment(banknoteArrayList);
        }

    }
}
