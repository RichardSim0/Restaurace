package com.engeto.restaurace;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderManager {
    private List<Order> orderList = new ArrayList<>();

    public void saveDataToOrdersFile(String fileName, String delimiter) throws RestaurantException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            for (Order order : orderList) {
                List<Waiter> waiters = order.getWaiters();
                String waitersInfo = waiters.stream()
                        .map(waiter -> String.valueOf(waiter.getId()))
                        .collect(Collectors.joining(", "));

                List<Dish> dishes = order.getDishes();

                String dishesInfo = dishes.stream()
                        .map(Dish::getTitle)
                        .collect(Collectors.joining(", "));

                writer.println(
                        order.getTable() + delimiter +
                                dishesInfo + delimiter +
                                waitersInfo + delimiter +
                                order.getOrderedTime() + delimiter +
                                order.getFulfilmentTime() + delimiter +
                                order.isPaid() + delimiter +
                                order.getNote() + delimiter
                );
            }
        } catch (IOException e) {
            throw new RestaurantException("Chyba pri zápise do súboru: " + fileName + " ! " + e.getLocalizedMessage());
        }
    }
    public void add(Order newOrder){
        orderList.add(newOrder);
    }
    public void remove(int index){
        orderList.remove(index);
    }
    public Order get(int index){
        return orderList.get(index);
    }
    public List<Order> getOrderList() {
        return new ArrayList<>(orderList);
    }

    @Override
    public String toString() {
        return "" + orderList;
    }
}
