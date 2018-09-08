package Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import javafx.collections.ObservableList;

/**
 * The Dish class.
 * <p>
 * A Dish represents an item on the menu, all the ingredients required to prepare it, and the price required to pay.
 * The amount of each ingredient can be adjusted through order customizations.
 *
 * @author Group 0220
 */
public class Dish implements Serializable {

    private String name; //The name of the dish.
    private HashMap<String, Double> ingredients; //All ingredients required to cook the dish.
    private double price; //The price of the dish.

    /**
     * Constructor for a Dish with specified customizations. Takes a menu item and deep clones the required ingredients.
     * The menu item is located by the dish name, assuming that all dishes have unique names. All adjustments that are
     * part of the order are made here.
     * <p>
     * Contains a suppressed "unchecked" warning for Cloneable.clone
     *
     * @param menu        The menu item that is taken to get ingredients required and price
     * @param dishName    The name of the dish
     * @param adjustments A collection of all modifications (additions and subtractions of ingredients) attached
     */
    @SuppressWarnings("unchecked")
    public Dish(ObservableList<MenuItem> menu, String dishName,
                HashMap<String, Double> adjustments) {

        name = dishName;

        for (MenuItem item : menu) {
            if (item.getDishName().equals(dishName)) {
                ingredients = (HashMap<String, Double>) item.getIngredientNeeded().clone();
                price = item.getDishPrice();
                break;
            }
        }
        makeAdjustments(adjustments);
    }

    /**
     * Constructor for a Dish. Takes a menu item and deep clones the required ingredients.
     * The menu item is located by the dish name, assuming that all dishes have unique names.
     * <p>
     * Contains a suppressed "unchecked" warning for Cloneable.clone
     *
     * @param menu     The menu item that is taken to ge ingredients required and price
     * @param dishName The name of the dish
     */
    @SuppressWarnings("unchecked")
    public Dish(ObservableList<MenuItem> menu, String dishName) {

        name = dishName;
        for (MenuItem item : menu) {
            if (item.getDishName().equals(dishName)) {
                ingredients = (HashMap<String, Double>) item.getIngredientNeeded().clone();
                price = item.getDishPrice();
                break;
            }

        }
    }

    /**
     * Helper method for Dish constructor with adjustments that modifies the ingredients attribute to reflect order
     * customizations.
     *
     * @param adjustments The collection of all ingredient additions and subtractions to be made to this dish.
     */
    private void makeAdjustments(HashMap<String, Double> adjustments) {

        for (HashMap.Entry<String, Double> adjustment : adjustments.entrySet()) {
            String key = adjustment.getKey();
            if (ingredients.containsKey(key)) {
                ingredients.put(key, ingredients.get(key) + adjustment.getValue());
            }
        }

        //Uses Collection.removeIf which requires a lambda expression, to remove all ingredients whose quantities are 0
        //and below.
        Set<HashMap.Entry<String, Double>> ingredientSet = ingredients.entrySet();
        ingredientSet.removeIf((HashMap.Entry<String, Double> ingredient) -> ingredient.getValue() <= 0);
    }

    /**
     * Getter for the price of this Dish.
     *
     * @return The price of this Dish.
     */
    double getPrice() {
        return price;
    }

    /**
     * Provides a string representation of this Dish, which is just its name.
     *
     * @return The name of this dish.
     */
    public String toString() {
        return name;
    }

    /**
     * Getter for the ingredients that this Dish requires to prepare.
     *
     * @return The HashMap of all ingredients to their quantities.
     */
    public HashMap<String, Double> getIngredients() {
        return ingredients;
    }


}
