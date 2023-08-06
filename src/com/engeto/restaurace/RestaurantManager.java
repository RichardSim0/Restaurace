package com.engeto.restaurace;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RestaurantManager {
    OrdersManager ordersManager = new OrdersManager();
    List<Order> listOfOrders = ordersManager.getOrdersList();
    List<Order> listInProcess = new ArrayList<>();
    public int getOrdersInProcess(){
        for (Order order : listOfOrders){
            if (LocalDateTime.now().isAfter(order.getFulfilmentTime())) {
                listInProcess.add(order);
            }else listInProcess.add(order);
        }
        return listInProcess.size();
    }
}