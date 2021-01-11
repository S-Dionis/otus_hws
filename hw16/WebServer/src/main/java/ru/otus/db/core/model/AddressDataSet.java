package ru.otus.db.core.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class AddressDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street")
    private String street;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public AddressDataSet() {
    }

    public AddressDataSet(Long id, String street, User user) {
        this.id = id;
        this.street = street;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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
        if (!(o instanceof AddressDataSet)) return false;
        AddressDataSet that = (AddressDataSet) o;
        return id.equals(that.id) &&
                street.equals(that.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street);
    }
}
