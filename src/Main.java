import com.engeto.restaurace.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        DishManager dishManager = new DishManager();
        try {
            dishManager.loadDataFromFile(Settings.fileNameDishes2(), Settings.delimiter());
        } catch (RestaurantException e) {
            System.err.println("Súbor: "+Settings.fileNameDishes()+" sa nenašiel! "+e.getLocalizedMessage());
        }
        try {
            dishManager.saveDataToFile(Settings.fileNameDishes(),Settings.delimiter());
        } catch (RestaurantException e) {
            System.err.println("Chyba pri zápise do súboru: "+Settings.fileNameDishes());
        }

    }
}