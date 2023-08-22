package com.engeto.restaurace;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class OrdersManager{
    private List<Order> orderList = new ArrayList<>();

    public void saveDataToOrdersFile(String fileName, String delimiter) throws RestaurantException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            for (Order order : orderList){
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
}