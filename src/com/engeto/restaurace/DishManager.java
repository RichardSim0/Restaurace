package com.engeto.restaurace;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class DishManager {
    private List<Dish> dishRegister = new ArrayList<>();
    public void loadDataFromFileDishes(String fileName, String delimiter) throws RestaurantException {
        String line = "";
        String[] items = new String[0];
        int maxId = 0;
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            while (scanner.hasNextLine()){
                line = scanner.nextLine();
                items = line.split(delimiter);
                        int dishId = Integer.parseInt(items[0]);
                        if (dishId > maxId) {
                            maxId = dishId;
                        }
                        String dishTitle = items[1];
                        BigDecimal dishPrice = new BigDecimal(items[2]);
                        int dishPreparationTime = Integer.parseInt(items[3]);
                        String dishImage = items[4];
                        DishCategory dishCategory = DishCategory.valueOf(items[5]);
                        Dish newDish = new Dish(dishId, dishTitle, dishPrice, dishPreparationTime, dishImage, dishCategory);
                        dishRegister.add(newDish);
            }
            Dish.setNextId(maxId + 1);
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
                        dish.getId()+delimiter
                        +dish.getTitle()+delimiter
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
    public void add(Dish newDish){
        dishRegister.add(newDish);
    }
    public void remove(int index){
        dishRegister.remove(index);
    }

    public void removeAll(){
        dishRegister.removeAll(new ArrayList<>(dishRegister));
    }
    public List<Dish> getDishRegister() {
        return new ArrayList<>(dishRegister);
    }

    @Override
    public String toString() {
        return "DishManager{" +
                "dishRegister=" + dishRegister +
                '}';
    }
}