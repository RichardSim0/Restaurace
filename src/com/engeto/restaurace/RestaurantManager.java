package com.engeto.restaurace;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    public void sortOrdersByOrderedTime() {
        Collections.sort(listForManagement);
    }

    public void sortOrdersByWaiters(){
        Collections.sort(listForManagement, new OrderWaiterComparator());
    }

    public Map<Integer, BigDecimal> calculateTotalPricePerWaiter() {
        Map<Integer, BigDecimal> totalPricePerWaiter = new HashMap<>();
        for (Order order : listForManagement) {
            int waiterId = order.getWaiter();
            BigDecimal dishPrice = order.getDish().getPrice();
            BigDecimal totalPrice = totalPricePerWaiter.getOrDefault(waiterId, BigDecimal.ZERO);
            totalPrice = totalPrice.add(dishPrice);
            totalPricePerWaiter.put(waiterId, totalPrice);
        }
        return totalPricePerWaiter;
    }
    public LocalDateTime calculateAverageOrderedTime(LocalDateTime startTime, LocalDateTime endTime) {
        int count = 0;
        BigDecimal totalOrderedTimeInMinutes = BigDecimal.ZERO;
        for (Order order : listForManagement) {
            LocalDateTime orderedTime = order.getOrderedTime();
            if (orderedTime.isAfter(startTime) && orderedTime.isBefore(endTime)) {
                long minutes = startTime.until(orderedTime, ChronoUnit.MINUTES);
                totalOrderedTimeInMinutes = totalOrderedTimeInMinutes.add(BigDecimal.valueOf(minutes));
                count++;
            }
        }
        if (count == 0) {
            // Návrat hodnoty null alebo iná správa v prípade, že nie sú žiadne objednávky v určenom období
            return null;
        }
        BigDecimal averageOrderedTimeInMinutes = totalOrderedTimeInMinutes.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        return startTime.plusMinutes(averageOrderedTimeInMinutes.longValue());
    }

    public List<Order> getListForManagement() {
        return new ArrayList<>(listForManagement);
    }
}
