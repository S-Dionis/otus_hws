package ru.otus.core.model;

import javax.persistence.*;
import java.util.*;

/**
 * @author sergey
 * created on 03.02.19.
 */
@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age", nullable = false)
    private int age;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PhoneDataSet> phones;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private AddressDataSet address;

    public User() {
    }

    public User(Long id, String name, int age, List<PhoneDataSet> phones, AddressDataSet address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phones = phones;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDataSet> phones) {
        this.phones = phones;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) {
            return false;
        }
        User another = (User) obj;

        return this.id.equals(another.id) &&
                this.age == another.age &&
                this.name.equals(another.name) &&
                this.address.equals(another.address) &&
                new ArrayList<>(this.getPhones()).equals(new ArrayList<>(another.getPhones()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, address, phones);
    }

}