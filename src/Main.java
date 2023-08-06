import com.engeto.restaurace.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DishManager dishManager = new DishManager();
        Menu menu = new Menu();
        OrdersManager ordersManager = new OrdersManager();
        RestaurantManager restaurantManager = new RestaurantManager(ordersManager);
        try {
            dishManager.loadDataFromFile(Settings.fileNameDishes(), Settings.delimiter());
        } catch (RestaurantException e) {
            System.err.println("Súbor: " + Settings.fileNameDishes() + " sa nenašiel! " + e.getLocalizedMessage());
        }
        try {
            dishManager.saveDataToFile(Settings.fileNameDishes2(), Settings.delimiter());
        } catch (RestaurantException e) {
            System.err.println("Chyba pri zápise do súboru: " + Settings.fileNameDishes2() + "!! " + e.getLocalizedMessage());
        }

        menu.add(dishManager.getDishRegister().get(6));
        menu.add(dishManager.getDishRegister().get(4));
        menu.add(dishManager.getDishRegister().get(5));
        menu.add(dishManager.getDishRegister().get(1));
        menu.add(dishManager.getDishRegister().get(9));
        Order order = new Order(2, menu.get(0), 1, LocalDateTime.of(2023, 7, 30, 14, 5), false, null);

        Order order1 = new Order(2, menu.get(0), 1, LocalDateTime.of(2023, 7, 30, 14, 5), true, null);
        Order order2 = new Order(2, menu.get(1), 2, LocalDateTime.of(2023, 7, 30, 14, 10), false, null);
        Order order3 = new Order(2, menu.get(2), 3, LocalDateTime.of(2023, 7, 30, 14, 15), false, null);

        ordersManager.add(order1);
        ordersManager.add(order2);
        ordersManager.add(order3);

        try {
            menu.saveDataToMenuFile(Settings.fileNameMenu(), Settings.delimiter());
        } catch (RestaurantException e) {
            System.err.println("Chyba pri zápise do súboru: " + Settings.fileNameMenu() + "!! " + e.getLocalizedMessage());
        }
        ordersManager.add(order);
        ordersManager.add(order);
        ordersManager.add(order);
        ordersManager.add(order);
        ordersManager.add(order);
        ordersManager.add(order);
        try {
            ordersManager.saveDataToOrdersFile(Settings.fileNameOrders(), Settings.delimiter());
        } catch (RestaurantException e) {
            System.err.println("Chyba pri zápise do súboru: " + Settings.fileNameOrders() + " ! " + e.getLocalizedMessage());
        }
        System.out.println(restaurantManager.pendingOrder());

        System.out.println("Před přidáním objednávky: " + ordersManager.getOrdersList().size());
        ordersManager.add(order);
        System.out.println("Po přidání objednávky: " + ordersManager.getOrdersList().size());

        System.out.println(restaurantManager.getListForManagement());

    }
}