import com.engeto.restaurace.*;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        DishManager dishManager = new DishManager();
        DishRegister dishRegister = dishManager.getDishRegister();
        Menu menu = new Menu();
        RestaurantManager restaurantManager = new RestaurantManager();
        OrderManager orderManager = restaurantManager.getOrderManager();

        try {
            dishManager.loadDataFromFile(Settings.fileNameDishes(),Settings.delimiter());
        } catch (RestaurantException e) {
            System.err.println("Chyba pri načítaní dát zo súboru: "+Settings.fileNameDishes()+" ! ---> "+e.getLocalizedMessage());
        }

        Dish dish1 = new Dish("Kuřecí řízek obalovaný 150 g", BigDecimal.valueOf(6.50),15,"kureci-rizek-01",DishCategory.MAIN_COURSE);
        Dish dish2 = new Dish("Hranolky 150 g", BigDecimal.valueOf(2.70),5,"hranolky-01",DishCategory.SIDE_DISH);
        Dish dish3 = new Dish("Pstruh na víně 200 g", BigDecimal.valueOf(7.80),25,"pstruh-01",DishCategory.MAIN_COURSE);
        Dish dish4 = new Dish("Kofola 0,5 l", BigDecimal.valueOf(2.50),2,"kofola-01", DishCategory.DRINK);

        dishRegister.add(dish1);
        dishRegister.add(dish2);
        dishRegister.add(dish3);
        dishRegister.add(dish4);

        menu.add(dish1);
        menu.add(dish2);
        menu.add(dish4);
        try {
            dishManager.saveDataToFile(Settings.fileNameDishes(),Settings.delimiter());
        } catch (RestaurantException e) {
            System.err.println("Problé pri zápise do súboru:"+Settings.fileNameDishes()+" ! ---> "+e.getLocalizedMessage());
        }

        try {
            Order order = new Order(1,menu, dish1,1,false,null);
        } catch (RestaurantException e) {
            System.err.println("Nie je možné vytvoriť objednávku"+e.getLocalizedMessage());
        }
    }
}