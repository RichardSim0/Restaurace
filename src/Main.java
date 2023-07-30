import com.engeto.restaurace.*;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        DishManager dishManager = new DishManager();
        Menu menu = new Menu();
        OrdersManager ordersManager = new OrdersManager();
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

        Order order = new Order(2, dishManager.getDishRegister().get(0), 1, LocalDateTime.of(2023, 7, 30, 14, 5), true, null);
        //System.out.println(order);
        menu.add(dishManager.getDishRegister().get(6));
        menu.add(dishManager.getDishRegister().get(4));
        menu.add(dishManager.getDishRegister().get(5));
        menu.add(dishManager.getDishRegister().get(1));
        menu.add(dishManager.getDishRegister().get(9));

        try {
            menu.saveDataToMenuFile(Settings.fileNameMenu(),Settings.delimiter());
        } catch (RestaurantException e) {
            System.err.println("Chyba pri zápise do súboru: " + Settings.fileNameMenu() + "!! " + e.getLocalizedMessage());
        }
        ordersManager.add(order);
        ordersManager.add(order);
        ordersManager.add(order);
        ordersManager.add(order);
        try {
            ordersManager.saveDataToOrdersFile(Settings.fileNameOrders(),Settings.delimiter());
        } catch (RestaurantException e) {
            System.err.println("Chyba pri zápise do súboru: "+Settings.fileNameOrders()+" ! "+e.getLocalizedMessage());
        }

    }
}