package com.engeto.restaurace;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdersManager {
    private List<Order> ordersList = new ArrayList<>();

    public void saveDataToOrdersFile(String fileName, String delimiter) throws RestaurantException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            for (Order order : ordersList){
                writer.println(
                        order.getTable()+delimiter
                        +order.getDish()+delimiter
                        +order.getWaiter()+delimiter
                        +order.getOrderedTime()+delimiter
                        +order.getFulfilmentTime()+delimiter
                        +order.isPaid()+delimiter
                        +order.getNote()+delimiter
                );
            }
        } catch (IOException e) {
                throw new RestaurantException("Chyba pri zápise do súboru: " + fileName + " ! " + e.getLocalizedMessage());
        }
    }
    public void add(Order newOrder){
        ordersList.add(newOrder);
    }
    public void remove(int index){
        ordersList.remove(index);
    }
    public Order get(int index){
        return ordersList.get(index);
    }
    public List<Order> getOrdersList() {
        return new ArrayList<>(ordersList);
    }
}