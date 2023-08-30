package com.engeto.restaurace;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class RestaurantManager {
    OrderManager orderManager = new OrderManager();

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public int pendingOrder() {
        List<Order> pendingOrdersList = new ArrayList<>();
        for (Order order : orderManager.getOrderList()) {
            if (order.getFulfilmentTime().isAfter(LocalDateTime.now())) {
                pendingOrdersList.add(order);
            }
        }
        return pendingOrdersList.size();
    }

    public void sortOrdersByOrderedTime() {
        Collections.sort(orderManager.getOrderList());
    }

    public void sortOrdersByWaiters(){
        orderManager.getOrderList().sort(new OrderWaiterComparator());
    }

    public Map<Integer, BigDecimal> calculateTotalPricePerWaiter() {
        Map<Integer, BigDecimal> totalPricePerWaiter = new HashMap<>();
        for (Order order : orderManager.getOrderList()) {
            List<Waiter> waiters = order.getWaiters();
            List<Dish> dishes = order.getDishes();

            for (Waiter waiter : waiters) {
                int waiterId = waiter.getId();

                for (Dish dish : dishes) {
                    BigDecimal dishPrice = dish.getPrice();
                    BigDecimal totalPrice = totalPricePerWaiter.getOrDefault(waiterId, BigDecimal.ZERO);
                    totalPrice = totalPrice.add(dishPrice);
                    totalPricePerWaiter.put(waiterId, totalPrice);
                }
            }
        }
        return totalPricePerWaiter;
    }

    public long calculateAverageOrderedTime(LocalDateTime startTime, LocalDateTime endTime) {
        int count = 0;
        BigDecimal totalOrderedTimeInMinutes = BigDecimal.ZERO;
        for (Order order : orderManager.getOrderList()) {
            LocalDateTime orderedTime = order.getOrderedTime();
            if (orderedTime.isAfter(startTime) && orderedTime.isBefore(endTime)) {
                long minutes = orderedTime.until(order.getFulfilmentTime(), ChronoUnit.MINUTES);
                totalOrderedTimeInMinutes = totalOrderedTimeInMinutes.add(BigDecimal.valueOf(minutes));
                count++;
            }
        }
        if (count == 0) {
            return 0;
        }
        BigDecimal averageOrderedTimeInMinutes = totalOrderedTimeInMinutes.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        return averageOrderedTimeInMinutes.longValue();
    }
    public List<Order> getMealsOrderedToday() {
        List<Order> mealsOrderedToday = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Order order : orderManager.getOrderList()) {
            LocalDateTime orderedTime = order.getOrderedTime();
            LocalDate orderedDate = orderedTime.toLocalDate();
            if (orderedDate.equals(today)) {
                mealsOrderedToday.add(order);
            }
        }
        return mealsOrderedToday;
    }
    public void exportOrdersForTableToFile(int tableNumber, String fileName) throws RestaurantException {
        List<Order> ordersForTable = new ArrayList<>();

        for (Order order : orderManager.getOrderList()) {
            if (order.getTable() == tableNumber) {
                ordersForTable.add(order);
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String tableNumberFormatted = (tableNumber <= 9) ? " " + tableNumber : String.valueOf(tableNumber);

            writer.println("** Objednávky pro stůl č. " + tableNumberFormatted + " **");
            writer.println("****");

            int counter = 1;

            Map<String, Integer> dishCounts = new HashMap<>();

            for (Order order : ordersForTable) {
                List<Dish> dishes = order.getDishes();

                for (Dish dish : dishes) {
                    String dishKey = dish.getTitle() + " (" + dish.getPrice() + " Kč)";

                    int quantity = dishCounts.getOrDefault(dishKey, 0) + 1;
                    dishCounts.put(dishKey, quantity);

                    String quantityInfo = (quantity > 1) ? " " + quantity + "x" : "";
                    String dishPrice = dish.getPrice() + " Kč";

                    writer.println(counter + "." + " " + dish.getTitle() + " " + quantityInfo + " "
                            + "(" + dishPrice + ")" + ":" + "\t"
                            + order.getOrderedTime().format(timeFormatter) + "-"
                            + order.getFulfilmentTime().format(timeFormatter) + "\t"
                            + "číšník č. " + order.getWaiters());

                    counter++;
                }
            }

            writer.println("******");
        } catch (IOException e) {
            throw new RestaurantException("Chyba pri zápise do súboru: " + fileName + "! " + e.getLocalizedMessage());
        }
    }
}
