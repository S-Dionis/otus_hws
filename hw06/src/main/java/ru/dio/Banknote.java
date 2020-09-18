package ru.dio;

public class Banknote implements Comparable<Banknote> {

    private final Nominal nominal;

    public Banknote(Nominal nominal) {
        this.nominal = nominal;
    }

    public Nominal getNominal() {
        return this.nominal;
    }

    @Override
    public int compareTo(Banknote o) {
        return Integer.compare(nominal.value, o.nominal.value);
    }

}
