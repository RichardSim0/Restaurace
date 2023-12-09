package com.engeto.restaurace;


import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    protected static List<Dish> menuList = new ArrayList<>();

    public void loadDataFromFileMenu(String fileName, String delimiter) throws RestaurantException {
        String line = "";
        String[] items = new String[0];
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            while (scanner.hasNextLine()){
                line = scanner.nextLine();
                items = line.split(delimiter);
                int iD = Integer.parseInt(items[0]);
                for (Dish dish : menuList) {
                    if (dish.getId() == iD) {
                        dish.assignValues(items[1], new BigDecimal(items[2]), Integer.parseInt(items[3]), items[4], DishCategory.valueOf(items[5]));
                    }else {
                        System.err.println("Jedlo s ID " + iD + " nebolo nájdené!");
                    }
                }
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

    public void saveDataToFileMenu(String fileName, String delimiter) throws RestaurantException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            for (Dish dish : menuList){
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

    public static Dish getDishByTitle(String title) {
        for (Dish dish : menuList) {
            if (dish.getTitle().equals(title)) {
                return dish;
            }
        }
        return null;
    }

    public boolean isDishOnMenu(Dish dish) {
        return menuList.contains(dish);
    }

    public void add(Dish newDish){
        menuList.add(newDish);
    }

    public void remove(int index){
        menuList.remove(index);
    }

    public void removeAll(){
        menuList.removeAll(menuList);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuList=" + menuList +
                '}';
    }
}