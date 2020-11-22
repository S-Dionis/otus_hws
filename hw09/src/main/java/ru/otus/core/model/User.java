package ru.otus.core.model;

import ru.otus.core.annotation.ID;

import java.util.Objects;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class User {

    @ID
    private long id;
    private String name;
    private int age;

    public User() {

    }

    public User(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
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
        if (!(obj instanceof User)) {
            return false;
        }
        User another = (User) obj;
        return this.id == another.id && this.age == another.age && this.name.equals(another.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }
}