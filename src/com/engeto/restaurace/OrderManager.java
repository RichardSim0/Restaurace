package com.engeto.restaurace;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;
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
    public void loadDataFromOrdersFile(String fileName, String delimiter) throws RestaurantException {
        String line = "";
        String[] items;
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                items = line.split(delimiter);
                if (items.length != 7) {
                    throw new RestaurantException("Chybný formát v súbore na riadku: " + line);
                }

                int table = Integer.parseInt(items[0]);

                String[] dishTitles = items[1].split(", ");
                List<Dish> dishes = new ArrayList<>();
                for (String dishTitle : dishTitles) {
                    Dish dish = Menu.getDishByTitle(dishTitle);
                        dishes.add(dish);
                }

                List<Waiter> waiters = new ArrayList<>();
                String[] waiterIds = items[2].split(", ");
                for (String waiterId : waiterIds) {
                    int id = Integer.parseInt(waiterId);
                    Waiter waiter = Waiter.getWaiterById(id);
                    if (waiter != null) {
                        waiters.add(waiter);
                    }
                }

                LocalDateTime orderedTime = LocalDateTime.parse(items[3]);
                LocalDateTime fulfilmentTime = null;
                if (!items[4].equals("null")) {
                    fulfilmentTime = LocalDateTime.parse(items[4]);
                }
                if (fulfilmentTime == null) {
                    continue;
                }
                boolean isPaid = Boolean.parseBoolean(items[5]);
                String note = items[6];

                Order order = new Order(table, dishes, waiters, orderedTime,fulfilmentTime, isPaid, note);

                orderList.add(order);
            }
        } catch (FileNotFoundException e) {
            throw new RestaurantException("Súbor: " + fileName + " sa nepodarilo nájsť: " + e.getLocalizedMessage());
        } catch (NumberFormatException e) {
            throw new RestaurantException("Chybný formát čísla na riadku: "+ line + e.getLocalizedMessage());
        } catch (PatternSyntaxException e) {
            throw new RestaurantException("Chyba pri čítaní súboru: "+ fileName +" na riadku: " + line + " ! "+ e.getLocalizedMessage());
        } catch (DateTimeParseException e) {
            throw new RestaurantException("Chybný formát času v súbore: \""+ fileName + "\" na riadku: \n" + line +" ! "+ e.getLocalizedMessage());
        }
    }
    public void add(Order newOrder){
        orderList.add(newOrder);
    }
    public List<Order> getOrderList() {
        return new ArrayList<>(orderList);
    }

    @Override
    public String toString() {
        return "" + orderList;
    }
}
