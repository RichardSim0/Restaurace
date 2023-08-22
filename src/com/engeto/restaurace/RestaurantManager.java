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
    OrdersManager ordersManager = new OrdersManager();

//    /*/*/*/* AKTUáLNE ROZPRACOVANé A NEDOKONčENé OBJEDNáVKY *\*\*\*\
    public int pendingOrder() {
        List<Order> pendingOrdersList = new ArrayList<>();
        for (Order order : ordersManager.getOrderList()) {
            if (order.getFulfilmentTime().isAfter(LocalDateTime.now())) {
                pendingOrdersList.add(order);
            }
        }
        return pendingOrdersList.size();
    }

    public void sortOrdersByOrderedTime() {
        Collections.sort(ordersManager.getOrderList());
    }

    public void sortOrdersByWaiters(){
        ordersManager.getOrderList().sort(new OrderWaiterComparator());
    }

    public Map<Integer, BigDecimal> calculateTotalPricePerWaiter() {
        Map<Integer, BigDecimal> totalPricePerWaiter = new HashMap<>();
        for (Order order : ordersManager.getOrderList()) {
            int waiterId = order.getWaiter();
            BigDecimal dishPrice = order.getDish().getPrice();
            BigDecimal totalPrice = totalPricePerWaiter.getOrDefault(waiterId, BigDecimal.ZERO);
            totalPrice = totalPrice.add(dishPrice);
            totalPricePerWaiter.put(waiterId, totalPrice);
        }
        return totalPricePerWaiter;
    }
    public long calculateAverageOrderedTime(LocalDateTime startTime, LocalDateTime endTime) {
        int count = 0;
        BigDecimal totalOrderedTimeInMinutes = BigDecimal.ZERO;
        for (Order order : ordersManager.getOrderList()) {
            LocalDateTime orderedTime = order.getOrderedTime();
            if (orderedTime.isAfter(startTime) && orderedTime.isBefore(endTime)) {
                long minutes = orderedTime.until(order.getFulfilmentTime(), ChronoUnit.MINUTES);
                totalOrderedTimeInMinutes = totalOrderedTimeInMinutes.add(BigDecimal.valueOf(minutes));
                count++;
            }
        }
        if (count == 0) {
            // Návrat hodnoty null alebo iná správa v prípade, že nie sú žiadne objednávky v určenom období
            return 0;
        }
        BigDecimal averageOrderedTimeInMinutes = totalOrderedTimeInMinutes.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        return averageOrderedTimeInMinutes.longValue();
    }
    public List<Order> getMealsOrderedToday() {
        List<Order> mealsOrderedToday = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Order order : ordersManager.getOrderList()) {
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

        for (Order order : ordersManager.getOrderList()) {
            if (order.getTable() == tableNumber) {
                ordersForTable.add(order);
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String tableNumberFormatted = (tableNumber <= 9) ? " " + tableNumber : String.valueOf(tableNumber);

            writer.println("** Objednávky pro stůl č. " + tableNumberFormatted + " **");
            writer.println("****");

            int counter = 1; // Counter for item numbers

            Map<String, Integer> dishCounts = new HashMap<>(); // To track dish counts

            for (Order order : ordersForTable) {
                Dish dish = order.getDish();
                String dishKey = dish.getTitle() + " (" + dish.getPrice() + " Kč)";

                int quantity = dishCounts.getOrDefault(dishKey, 0) + 1;
                dishCounts.put(dishKey, quantity);

                String quantityInfo = (quantity > 1) ? " " + quantity + "x" : ""; // Include "x" if quantity is greater than 1
                String dishPrice = dish.getPrice() + " Kč";

                writer.println(counter + "." + " " + dish.getTitle() + " " + quantityInfo + " "
                        + "(" + dishPrice + ")" + ":" + "\t"
                        + order.getOrderedTime().format(timeFormatter) + "-"
                        + order.getFulfilmentTime().format(timeFormatter) + "\t"
                        + "číšník č. " + order.getWaiter());

                counter++;
            }

            writer.println("******");
        } catch (IOException e) {
            throw new RestaurantException("Chyba pri zápise do súboru: "+fileName+"! "+e.getLocalizedMessage());
        }
    }
}
