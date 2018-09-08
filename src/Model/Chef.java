package Model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * The Chef class.
 * <p>
 * A Chef, after seeing an Order, checks if there are enough ingredients to make it. If there are, prepare the Dish.
 *
 * @author Group 0220
 */
public class Chef extends Staff implements Serializable {

    private static int numChefs = 0;

    /**
     * Constructor for the Chef class. Each time a Chef is initialized, the static numChefs counter is incremented by 1,
     * and this Chef is added to the restaurant's collection of Chefs.
     *
     * @param name       The name of this Chef
     * @param restaurant The restaurant this Chef works at.
     */
    Chef(String name, Restaurant restaurant) {
        super(name, restaurant);
        numChefs++;
        setId(numChefs);
        restaurant.getStaffSystem().addChef(this);
    }

    /**
     * Simulates a Chef "seeing" an Order. After seeing an order, they will take it and have it as their currentOrder.
     * A Chef can only see a given Order if they are not preoccupied with another Order.
     *
     * @param order The order that will be seen by this Chef
     */
    public void seeOrder(Order order) {
        if (getCurrentOrder() == null) {
            order.setSeen(true);
            takeOrder(order);
            getRestaurant().getOrderSystem().seenOrder(order);
            EventLogger.log("Order #" + order.getOrderNum() + " has been seen by chef " + this.getName());
        }
    }

    /**
     * Confirms that the Order this Chef is currently working on has completed preparation.
     * When preparation is complete, this Chef no longer has a currentOrder.
     */
    public void confirmPrepared() {
        getCurrentOrder().setPrepared(true);
        getRestaurant().getOrderSystem().preparedOrder(getCurrentOrder());
        EventLogger.log("Order #" + getCurrentOrder().getOrderNum() + " has been prepared by chef " + this.getName());
        doneWithOrder();
    }

    /**
     * This Chef will prepare his currentOrder.
     * <p>
     * First, the Chef checks whether this Order is possible by ensuring that each ingredient required by the Order's
     * Dish is contained with sufficient quantity in the restaurant's inventory.
     * If, and only if, the Order is possible, the Chef will deduct the required ingredients from the inventory and
     * "cook" the dish.
     * Returns a boolean to indicate this method's success of completion.
     *
     * @return Whether this method has successfully resulted in a completed Order.
     * @throws IOException For file reading and writing in changeIngredientAmt
     */
    public boolean prepareOrder() throws IOException {

        Order order = getCurrentOrder();

        if (order == null) {

            return false;

        } else {

            boolean possible = true;

            HashMap<String, Double> currentStock = this.getInventory().getStock();

            for (HashMap.Entry<String, Double> ingredient : order.getDish().getIngredients().entrySet()) {
                String key = ingredient.getKey();
                if (!currentStock.containsKey(key) || currentStock.get(key) < ingredient.getValue()) {
                    possible = false;
                    getRestaurant().getOrderSystem().cancelOrder(order);

                    break;
                }
            }

            if (possible) {
                for (HashMap.Entry<String, Double> ingredient : order.getDish().getIngredients()
                        .entrySet()) {
                    String key = ingredient.getKey();
                    getInventory().changeIngredientAmt(key, ingredient.getValue() * -1);
                }
            } else {
                doneWithOrder();
            }

            return possible;
        }
    }

    /**
     * A String representation of this Chef.
     * <p>
     * Formatted as "chef[id]"
     *
     * @return The String representation of this Chef.
     */
    @Override
    public String toString() {
        String output = String.format("Chef #%d: %s", getId(), getName());

        return output;
    }
}
