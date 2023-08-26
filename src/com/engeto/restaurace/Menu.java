package com.engeto.restaurace;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private List<Dish> menuList = new ArrayList<>();

    public void saveDataToMenuFile(String fileName, String delimiter) throws RestaurantException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            for (Dish dish : menuList){
                writer.println(
                        dish.getTitle()+delimiter
                                +dish.getPrice()+delimiter
                                +dish.getPreparationTime()+delimiter
                                +dish.getImage()+delimiter
                                +dish.getCategory()
                );
            }
        } catch (IOException e) {
            throw new RestaurantException("Chyba pri zápise do súboru: "+fileName+"! "+e.getLocalizedMessage());
        }
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

    public Dish get(int index){
        return menuList.get(index);
    }

    public ArrayList<Dish> getMenuList() {
        return new ArrayList<>(menuList);
    }
}