package ru.otus.core.model;

import ru.otus.core.annotation.ID;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {

    @ID
    private long no;
    private String type;
    private BigDecimal rest;

    public Account() {

    }

    public Account(long no, String type, BigDecimal rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public long getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getRest() {
        return rest;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRest(BigDecimal rest) {
        this.rest = rest;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + no +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(no, type, rest);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account) {
            Account another = (Account) obj;
            return this.no == another.no
                    && this.type.equals(another.type)
                    && this.rest.equals(another.rest);
        }
        return false;
    }
}
