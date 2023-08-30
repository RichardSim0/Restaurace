import com.engeto.restaurace.*;

import java.math.BigDecimal;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        DishManager dishManager = new DishManager();
        DishRegister dishRegister = dishManager.getDishRegister();
        Menu menu = new Menu();
        RestaurantManager restaurantManager = new RestaurantManager();
        OrderManager orderManager = restaurantManager.getOrderManager();

        Waiter waiter1 = new Waiter(1, "Čašník1");
        Waiter waiter2 = new Waiter(2, "Čašník2");

        try {
            dishManager.loadDataFromFile(Settings.fileNameDishes(),Settings.delimiter());
        } catch (RestaurantException e) {
            System.err.println("Chyba pri načítaní dát zo súboru: "+Settings.fileNameDishes()+" ! ---> "+e.getLocalizedMessage());
        }

        Dish rizek = new Dish("Kuřecí řízek obalovaný 150 g", BigDecimal.valueOf(6.50),15,"kureci-rizek-01",DishCategory.MAIN_COURSE);
        Dish hranolky = new Dish("Hranolky 150 g", BigDecimal.valueOf(2.70),5,"hranolky-01",DishCategory.SIDE_DISH);
        Dish pstruh = new Dish("Pstruh na víně 200 g", BigDecimal.valueOf(7.80),25,"pstruh-01",DishCategory.MAIN_COURSE);
        Dish kofola = new Dish("Kofola 0,5 l", BigDecimal.valueOf(2.50),2,"kofola-01", DishCategory.DRINK);

        dishRegister.add(rizek);
        dishRegister.add(hranolky);
        dishRegister.add(pstruh);
        dishRegister.add(kofola);

        menu.add(rizek);
        menu.add(hranolky);
        menu.add(kofola);
        try {
            dishManager.saveDataToFile(Settings.fileNameDishes(),Settings.delimiter());
        } catch (RestaurantException e) {
            System.err.println("Problém pri zápise do súboru:"+Settings.fileNameDishes()+" ! ---> "+e.getLocalizedMessage());
        }

        try {
            orderManager.add(new Order(2,menu,Arrays.asList(rizek,hranolky,kofola), Arrays.asList(waiter1,waiter2), false,null));
        } catch (RestaurantException e) {
            System.err.println("Chyba pri vytváraní objednávky: " + e.getLocalizedMessage());
        }
        try {
            restaurantManager.exportOrdersForTableToFile(2, Settings.fileNameOrdersForTable());
        } catch (RestaurantException e) {
            System.err.println("Chyba pri zápise do súboru: " + Settings.fileNameOrdersForTable() + "! --->" + e.getLocalizedMessage());
        }
    }
}