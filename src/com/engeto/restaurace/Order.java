package com.engeto.restaurace;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order implements Comparable<Order>{
    private int table;
    private List<Dish> dishes;
    private Menu menu;
    private List<Waiter> waiters;
    private LocalDateTime orderedTime;
    private LocalDateTime fulfilmentTime;
    private boolean isPaid;
    private String note;

    public Order(int table, Menu menu, List<Dish> dishes, List<Waiter> waiters, LocalDateTime orderedTime, boolean isPaid, String note) throws RestaurantException {
        try{
        this.table = table;
        this.menu = menu;
        this.dishes = new ArrayList<>(dishes);
        this.waiters = waiters;
        this.orderedTime = orderedTime;
            int preparationTime = 0;
            for (Dish dish : dishes) {
                preparationTime = dish.getPreparationTime();
            }
            this.fulfilmentTime = orderedTime.plusMinutes(preparationTime);
        this.isPaid = isPaid;
        this.note = note;

            for (Dish dish : dishes) {
                if (!menu.isDishOnMenu(dish)) {
                    throw new RestaurantException("Toto jedlo nie je v menu a nemôže byť objednané.");
                }
            }

            System.out.println("Objednávka pre viacero jedál bola prijatá.");
        } catch (IllegalArgumentException e) {
            throw new RestaurantException("Toto jedlo nie je v menu a nemôže byť objednané." + e.getLocalizedMessage());
        }
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<Waiter> getWaiters() {
        return waiters;
    }

    public void addWaiter(Waiter waiter) {
        waiters.add(waiter);
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public LocalDateTime getFulfilmentTime() {
        return fulfilmentTime;
    }

    public void setFulfilmentTime(LocalDateTime fulfilmentTime) {
        this.fulfilmentTime = fulfilmentTime;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Order{" +
                "table=" + table +
                ", dishes=" + dishes +
                ", menu=" + menu +
                ", waiters=" + waiters +
                ", orderedTime=" + orderedTime +
                ", fulfilmentTime=" + fulfilmentTime +
                ", isPaid=" + isPaid +
                ", note='" + note + '\'' +
                '}';
    }

    @Override
    public int compareTo(Order o) {
        return this.orderedTime.compareTo(o.orderedTime);
    }
}