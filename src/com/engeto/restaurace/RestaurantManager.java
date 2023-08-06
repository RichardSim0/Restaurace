package com.engeto.restaurace;

import java.util.ArrayList;
import java.util.List;

public class RestaurantManager {
    private OrdersManager ordersManager;
    private List<Order> listForManagement = new ArrayList<>();
    public RestaurantManager(OrdersManager ordersManager) {
        this.ordersManager = ordersManager;
    }

//    /*/*/*/* AKTUáLNE ROZPRACOVANé A NEDOKONčENé OBJEDNáVKY *\*\*\*\
    public int pendingOrder() {
        List<Order> orders = ordersManager.getOrdersList();
        for (Order order : orders) {
            if (!order.isPaid()) {
                listForManagement.add(order);
            }
        }
        return listForManagement.size();
    }
    private static void sorting(){

    }
    public List<Order> getListForManagement() {
        return new ArrayList<>(listForManagement);
    }
}
