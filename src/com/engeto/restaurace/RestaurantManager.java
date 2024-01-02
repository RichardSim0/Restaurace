package com.engeto.restaurace;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.Scanner;

public class RestaurantManager {
    OrderManager orderManager = new OrderManager();

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public long pendingOrder() {
        List<Order> pendingOrdersList = new ArrayList<>();
        for (Order order : orderManager.getOrderList()) {
            if (!order.isPaid()) {
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

    public Map<String, BigDecimal> calculateTotalPricePerWaiter() {
        return orderManager.getOrderList().stream()
                .flatMap(order -> order.getWaiters().stream()
                        .map(waiter -> waiter.getName() + " " + waiter.getId())
                        .map(waiterKey -> new AbstractMap.SimpleEntry<>(waiterKey, order.getDishes())))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .map(Dish::getPrice)
                                        .filter(Objects::nonNull)
                                        .filter(price -> price.compareTo(BigDecimal.ZERO) >= 0)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
    }

    public long calculateAverageOrderedTime(LocalDateTime startTime, LocalDateTime endTime) {
        int count = 0;
        BigDecimal totalOrderedTimeInMinutes = BigDecimal.ZERO;
        for (Order order : orderManager.getOrderList()) {
            LocalDateTime orderedTime = order.getOrderedTime();
            if (orderedTime.isAfter(startTime) && orderedTime.isBefore(endTime) && order.isPaid()) {
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
    public List<String> getMealsOrderedToday() {
        List<String> mealNamesOrderedToday = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Order order : orderManager.getOrderList()) {
            LocalDateTime orderedTime = order.getOrderedTime();
            LocalDate orderedDate = orderedTime.toLocalDate();

            if (orderedDate.equals(today)) {
                List<Dish> dishes = order.getDishes();
                for (Dish dish : dishes) {
                    String dishTitle = dish.getTitle();
                    mealNamesOrderedToday.add(dishTitle);
                }
            }
        }

        return mealNamesOrderedToday;
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

            List<Dish> currentOrderDishes = new ArrayList<>();

            for (Order order : ordersForTable) {
                List<Dish> dishes = order.getDishes();
                List<Waiter> waiters = order.getWaiters();

                for (Dish dish : dishes) {

                    if (currentOrderDishes.contains(dish)) {
                        continue;
                    }

                    int quantity = Collections.frequency(dishes, dish);

                    LocalDateTime dishFulfilmentTime = order.getOrderedTime().plusMinutes(dish.getPreparationTime());
                    String dishPrice = dish.getPrice()+" Kč";

                    String waiterInfo = waiters.stream()
                            .map(waiter -> String.valueOf(waiter.getId()))
                            .collect(Collectors.joining(", "));

                    writer.println(counter + "." + " "
                            + dish.getTitle() + " "
                            + (quantity > 1 ? quantity + "x" : "") + " "
                            + "(" + dishPrice + ")"
                            + ":" + "\t"
                            + order.getOrderedTime().format(timeFormatter)
                            + "-"
                            + dishFulfilmentTime.format(timeFormatter) + "\t"
                            + "číšník č. " + waiterInfo);

                    counter++;

                    currentOrderDishes.add(dish);
                }
            }

            writer.println("******");
        } catch (IOException e) {
            throw new RestaurantException("Chyba pri zápise do súboru: " + fileName + "! " + e.getLocalizedMessage());
        }
    }
    public void loadOrdersFromOrdersForTableFile(String fileName) throws RestaurantException {
        String line = "";
        String[] parts;
        int tableNumber = -1;

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {

            String header1 = scanner.nextLine();

            if (header1.startsWith("** Objednávky pro stůl č. ")) {
                parts = header1.split("\\s+");
                if (parts.length >= 6) {
                    tableNumber = Integer.parseInt(parts[5]);
                }
            }

            scanner.nextLine();

            List<Order> orders = new ArrayList<>();
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();

                if (line.equals("******")) {
                    break;
                }

               parts = line.split("\\t");
                if (parts.length != 3) {
                    System.err.println("Chybný formát riadku: " + line);
                    continue;
                }

                String dishInfo = parts[0].trim();
                String timeInfo = parts[1].trim();
                String waiterInfo = parts[2].trim();

                String[] dishTitles = dishInfo.split(", ");
                List<Dish> dishes = new ArrayList<>();
                for (String dishTitle : dishTitles) {
                    Dish dish = Menu.getDishByTitle(dishTitle);
                    dishes.add(dish);
                }

                String[] timeParts = timeInfo.split("-");
                if (timeParts.length != 2) {
                    System.err.println("Chybný formát času: " + timeInfo);
                    continue;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalDateTime orderedTime = LocalTime.parse(timeParts[0].trim(), formatter).atDate(LocalDate.now());
                LocalDateTime fulfillmentTime = LocalTime.parse(timeParts[1].trim(), formatter).atDate(LocalDate.now());

                List<Waiter> waiters = Arrays.stream(waiterInfo.split(","))
                        .map(waiterId -> {
                            try {
                                String[] idParts = waiterId.trim().split("číšník č\\.\\s*");
                                if (idParts.length == 2) {
                                    int numericId = Integer.parseInt(idParts[1]);
                                    return Waiter.getWaiterById(numericId);
                                } else {
                                    System.err.println("Chybný formát číšníka: " + waiterId);
                                    return null;
                                }
                            } catch (NumberFormatException e) {
                                System.err.println("Chyba při parsování čísla na začátku řádku: " + waiterId);
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                Order order = new Order(tableNumber, dishes, waiters, orderedTime, fulfillmentTime);
                orders.add(order);
            }
        } catch (IOException e) {
            throw new RestaurantException("Súbor: " + fileName + " sa nenašiel! " + e.getLocalizedMessage());
        } catch (DateTimeParseException e) {
            throw new RestaurantException("Chybný formát času v súbore: " + fileName + " na riadku: " + line + " ! " + e.getLocalizedMessage());
        } catch (PatternSyntaxException e) {
            throw new RestaurantException("Chyba pri čítaní súboru: " + fileName + " na riadku: " + line + " ! " + e.getLocalizedMessage());
        } catch (NumberFormatException e) {
            throw new RestaurantException("Chybný formát čísla na riadku: " + line + e.getLocalizedMessage());
        } catch (NoSuchElementException e) {
            throw new RestaurantException("Nebol nájdený riadok v súbore: " + fileName + "! " + e.getLocalizedMessage());
        }
    }


    public BigDecimal calculateTotalPriceForTable(int tableNumber) throws RestaurantException {
        try {
            BigDecimal totalPrice = BigDecimal.ZERO;

            for (Order order : orderManager.getOrderList()) {
                if (order.getTable() == tableNumber) {
                    List<Dish> dishes = order.getDishes();

                    for (Dish dish : dishes) {
                        totalPrice = totalPrice.add(dish.getPrice());
                    }
                }
            }
            return totalPrice;
        } catch (NullPointerException e){
            throw new RestaurantException("Pri stole: "+ tableNumber + " nastala chyba! " + e.getLocalizedMessage());
        }
    }

    @Override
    public String toString() {
        return ""+orderManager;
    }
}
