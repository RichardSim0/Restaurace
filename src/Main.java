import com.engeto.restaurace.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        DishManager dishManager = new DishManager();
        Menu menu = new Menu();
        OrdersManager ordersManager = new OrdersManager();
        RestaurantManager restaurantManager = new RestaurantManager();
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
        Order order = new Order(2, menu.get(0), 1, LocalDateTime.now(), false, null);

        Order order1 = new Order(2, menu.get(0), 1, LocalDateTime.of(2021, 7, 30, 14, 5), false, null);
        Order order2 = new Order(3, menu.get(1), 2, LocalDateTime.of(2022, 7, 30, 14, 10), false, null);
        Order order3 = new Order(1, menu.get(2), 3, LocalDateTime.of(2020, 7, 30, 14, 15), false, null);

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

        System.out.println("Před přidáním objednávky: " + ordersManager.getOrderList().size());
        ordersManager.add(order);
        System.out.println("Po přidání objednávky: " + ordersManager.getOrderList().size());

        System.out.println(ordersManager.getOrderList());
        //zoradenie podľa času zadania
        restaurantManager.sortOrdersByOrderedTime();
        System.out.println(ordersManager.getOrderList());
        //zoradenie podľa čašníka
        restaurantManager.sortOrdersByWaiters();
        System.out.println(ordersManager.getOrderList());

        Map<Integer, BigDecimal> totalPricePerWaiter = restaurantManager.calculateTotalPricePerWaiter();
        for (Map.Entry<Integer, BigDecimal> entry : totalPricePerWaiter.entrySet()) {
            int waiterId = entry.getKey();
            BigDecimal totalPrice = entry.getValue();
            System.out.println("Waiter " + waiterId + ": Total Price = " + totalPrice);
        }
        System.out.println(restaurantManager.calculateAverageOrderedTime(LocalDateTime.of(2020,7,30,2,5),LocalDateTime.now()));
        System.out.println(restaurantManager.getMealsOrderedToday());
        System.out.println(restaurantManager.calculateTotalPricePerWaiter());

        try {
            restaurantManager.exportOrdersForTableToFile(2,Settings.fileNameOrdersForTable());
        } catch (RestaurantException e) {
            System.err.println("Chyba pri zápise do súboru: " + Settings.fileNameOrdersForTable() + "!! " + e.getLocalizedMessage());
        }
    }
}