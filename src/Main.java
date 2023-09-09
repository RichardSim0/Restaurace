import com.engeto.restaurace.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DishManager dishManager = new DishManager();
        DishRegister dishRegister = dishManager.getDishRegister();
        Menu menu = new Menu();
        RestaurantManager restaurantManager = new RestaurantManager();
        OrderManager orderManager = restaurantManager.getOrderManager();

        Waiter waiter1 = new Waiter(1, "Čašník1");
        Waiter waiter2 = new Waiter(2, "Čašník2");
//        1. Úloha
        try {
            dishManager.loadDataFromFile(Settings.fileNameDishes(),Settings.delimiter());
        } catch (RestaurantException e) {
            System.err.println("Chyba pri načítaní dát zo súboru: "+Settings.fileNameDishes()+" ! ---> "+e.getLocalizedMessage());
        }
//        2. Úloha
//        ---jedlá
        Dish rizek = new Dish("Kuřecí řízek obalovaný 150 g", BigDecimal.valueOf(6.50),15,"kureci-rizek-01",DishCategory.MAIN_COURSE);
        Dish hranolky = new Dish("Hranolky 150 g", BigDecimal.valueOf(2.70),5,"hranolky-01",DishCategory.SIDE_DISH);
        Dish pstruh = new Dish("Pstruh na víně 200 g", BigDecimal.valueOf(7.80),25,"pstruh-01",DishCategory.MAIN_COURSE);
        Dish kofola = new Dish("Kofola 0,5 l", BigDecimal.valueOf(2.50),2,"kofola-01", DishCategory.DRINK);

        dishRegister.add(rizek);
        dishRegister.add(hranolky);
        dishRegister.add(pstruh);
        dishRegister.add(kofola);
//        ---zaradenie do menu
        menu.add(rizek);
        menu.add(hranolky);
        menu.add(kofola);
//        ---objednávka na stôl č.15
        try {
            orderManager.add(new Order(15,menu,List.of(rizek,rizek,hranolky,hranolky,kofola,kofola), List.of(waiter1),LocalDateTime.of(2020,9,9,13,10), false, "Zákazníkom sa páči výzdoba reštaurácie."));
        } catch (RestaurantException e) {
            System.err.println("Chyba pri vytváraní objednávky: " + e.getLocalizedMessage());
        }
//        ---objednávka na stôl č.2
        try {
            orderManager.add(new Order(2,menu,List.of(hranolky,kofola,rizek), List.of(waiter1,waiter2), LocalDateTime.now(),false, null));
        } catch (RestaurantException e) {
            System.err.println("Chyba pri vytváraní objednávky: " + e.getLocalizedMessage());
        }
//        3. Úloha
        try {
            orderManager.add(new Order(1,menu,List.of(rizek,rizek,hranolky,hranolky,kofola,kofola,pstruh), List.of(waiter2), LocalDateTime.now(), false, null));
        } catch (RestaurantException e) {
            System.err.println("Chyba pri vytváraní objednávky: " + e.getLocalizedMessage());
        }
//        4. Úloha
        System.out.println(restaurantManager.calculateTotalPriceForTable(15));
//        5. Úloha
//        ---rozpacované objednávky
        System.out.println("Aktuálne rozpracované objednávky: "+restaurantManager.pendingOrder());
//        ---zoradenie objednávok podľa čašníka
        restaurantManager.sortOrdersByWaiters();
        System.out.println("Zoradenie objednávok podľa čašníka: " + "\n" + restaurantManager);
//        ---zoradenie objednávok podľa času zadania
        restaurantManager.sortOrdersByOrderedTime();
        System.out.println("Zoradenie objednávok podľa času zadania: " + "\n" + restaurantManager);
//        ---celková cena objednávok pre jednotlivých čašníkov
        System.out.println("Celková cena objednávok pre jednotlivých čašníkov: " + "\n" + restaurantManager.calculateTotalPricePerWaiter());
//        ---priemerná doba zpracovania objednávok
        System.out.println("Priemerná doba zpracovania objednávok v minútach: " + "\n" + restaurantManager.calculateAverageOrderedTime(LocalDateTime.of(2002, 8, 8, 10, 2), LocalDateTime.now()));



    }
}