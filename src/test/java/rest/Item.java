package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Item {
    public String name;
    public String quantity_unit;
    public Double price_for_quantity;

    public Item(String name, String quantity_unit, Double price_for_quantity){
        this.name = name;
        this.quantity_unit = quantity_unit;
        this.price_for_quantity = price_for_quantity;
    }

    public static void main(String[] args) {
        Item coffee = new Item("Lavazza", "kg.", 20.22);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(coffee));
        String jsonCoffee = gson.toJson(coffee);
    }
}
