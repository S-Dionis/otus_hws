package ru.otus.db.core.model;

import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "phone")
public class PhoneDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public PhoneDataSet() {
    }

    public PhoneDataSet(Long id, String number, User user) {
        this.id = id;
        this.number = number;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneDataSet)) return false;
        PhoneDataSet that = (PhoneDataSet) o;

        return id.equals(that.id) &&
                number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }
}
