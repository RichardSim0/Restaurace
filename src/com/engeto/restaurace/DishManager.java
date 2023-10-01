package com.engeto.restaurace;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class DishManager {
    DishRegister dishRegister = new DishRegister();
    public void loadDataFromFileDishes(String fileName, String delimiter) throws RestaurantException {
        String line = "";
        String[] items = new String[0];
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            while (scanner.hasNextLine()){
                line = scanner.nextLine();
                items = line.split(delimiter);
                String title = items[0];
                BigDecimal price = new BigDecimal(items[1]);
                int preparationTime = Integer.parseInt(items[2]);
                String image = items[3];
                DishCategory category = DishCategory.valueOf(items[4]);
                Dish newDish = new Dish(title, price, preparationTime, image, category);
                dishRegister.add(newDish);
            }
        } catch (FileNotFoundException e) {
            throw new RestaurantException("Súbor: "+fileName+" sa nenašiel! "+e.getLocalizedMessage());
        } catch (NumberFormatException e) {
            throw new RestaurantException("Chybné číslo na riadku:\n"+line+" !!! "+e.getLocalizedMessage());
        } catch (IllegalArgumentException e) {
            throw new RestaurantException("Chybne zadaná kategória jedla: \""+items[4]+"\" na riadku:\n"+line+" !!! "+e.getLocalizedMessage());
        } catch (ArrayIndexOutOfBoundsException e){
            throw new RestaurantException("Súbor: "+fileName+" je prázdny! "+e.getLocalizedMessage());
        }
    }
    public void saveDataToFileDishes(String fileName, String delimiter) throws RestaurantException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            for (Dish dish : dishRegister){
                writer.println(
                        dish.getTitle()+delimiter
                        +dish.getPrice()+delimiter
                        +dish.getPreparationTime()+delimiter
                        +dish.getImages()+delimiter
                        +dish.getCategory()
                );
            }
        } catch (IOException e) {
            throw new RestaurantException("Chyba pri zápise do súboru: "+fileName+"! "+e.getLocalizedMessage());
        }
    }
    public DishRegister getDishRegister() {
        return dishRegister;
    }

}