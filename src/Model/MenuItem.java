package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The MenuItem class.
 * <p>
 * A MenuItem represents one entry on the menu of this restaurant. It contains a name for the dish, its price, and the
 * default ingredients required to prepare it.
 *
 * @author Group 0220
 */
public class MenuItem implements Serializable {

    private String dishName;                            //The name of the dish represented by this menu item
    private double dishPrice;                           //The price of this dish

    //The default ingredients required to make this dish. Each entry maps an ingredient name to the quantity required.
    private HashMap<String, Double> ingredientNeeded;
    private ArrayList<String> dietTags;

    /**
     * Constructor for a MenuItem object.
     *
     * @param dishName         The name of the dish
     * @param dishPrice        The price of the dish
     * @param ingredientNeeded The default required ingredients to prepare this dish
     */
    MenuItem(String dishName, double dishPrice, HashMap<String, Double> ingredientNeeded, ArrayList<String> tags) {
        this.dishName = dishName;
        this.dishPrice = dishPrice;
        this.ingredientNeeded = ingredientNeeded;
        dietTags = tags;
    }

    /**
     * Getter for the dishPrice attribute.
     *
     * @return The price of the dish in this MenuItem
     */
    double getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(double dishPrice) {
        this.dishPrice = dishPrice;
    }

    /**
     * Getter for the ingredientNeeded attribute.
     *
     * @return The HashMap ingredients needed
     */
    public HashMap<String, Double> getIngredientNeeded() {
        return ingredientNeeded;
    }

    public void setIngredientNeeded(HashMap<String, Double> ingredientNeeded) {
        this.ingredientNeeded = ingredientNeeded;
    }

    /**
     * Provides a String representation of this MenuItem.
     * <p>
     * Formatted as "[dishName]: $[dishPrice]"
     *
     * @return The String representation of a menu item
     */
    @Override
    public String toString() {
        return dishName + ": $" + dishPrice;
    }

    /**
     * Getter for the dishName attribute.
     *
     * @return The name of the dish in this menu item
     */
    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public ArrayList<String> getDietTags() {
        return dietTags;
    }

    public void setDietTags(ArrayList<String> dietTags) {
        this.dietTags = dietTags;
    }


    public boolean contains(String targetTag) {

        for (String tag : dietTags) {
            if (tag.equals(targetTag)) {
                return true;
            }
        }

        return false;
    }
}