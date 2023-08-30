package com.engeto.restaurace;

public class Waiter {
    private int id;
    private String name;

    public Waiter(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Waiter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}