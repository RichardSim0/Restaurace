package com.engeto.restaurace;

import java.util.ArrayList;
import java.util.List;

public class Waiter {
    private static List<Waiter> waiters = new ArrayList<>();
    private int id;
    private String name;

    public Waiter(int id, String name) {
        this.id = id;
        this.name = name;
        waiters.add(this);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Waiter getWaiterById(int id) {
        for (Waiter waiter : waiters) {
            if (waiter.getId() == id) {
                return waiter;
            }
        }
        return null;
    }

    public List<Waiter> getAllWaiters() {
        return new ArrayList<>(waiters);
    }
    @Override
    public String toString() {
        return "Waiter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}