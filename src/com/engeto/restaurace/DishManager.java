package com.engeto.restaurace;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class DishManager {
    private List<Dish> dishRegister = new ArrayList<>();
    public void loadDataFromFileDishes(String fileName, String delimiter) throws RestaurantException {
        String line = "";
        String[] items = new String[0];
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            while (scanner.hasNextLine()){
                line = scanner.nextLine();
                items = line.split(delimiter);
                        int dishId = Integer.parseInt(items[0]);
                        String dishTitle = items[1];
                        BigDecimal dishPrice = new BigDecimal(items[2]);
                        int dishPreparationTime = Integer.parseInt(items[3]);
                        String dishImage = items[4];
                        DishCategory dishCategory = DishCategory.valueOf(items[5]);
                        Dish newDish = new Dish(dishId, dishTitle, dishPrice, dishPreparationTime, dishImage, dishCategory);
                        dishRegister.add(newDish);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Súbor: "+fileName+" sa nenašiel! "+e.getLocalizedMessage());
        } catch (NumberFormatException e) {
            System.err.println("Chybné číslo na riadku:\n"+line+" !!! "+e.getLocalizedMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Chybne zadaná kategória jedla: \""+items[4]+"\" na riadku:\n"+line+" !!! "+e.getLocalizedMessage());
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("Súbor: "+fileName+" je prázdny! "+e.getLocalizedMessage());
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
            System.err.println("Chyba pri zápise do súboru: "+fileName+"! "+e.getLocalizedMessage());
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