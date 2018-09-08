package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The Staff class.
 * <p>
 * Staff are the workers at this restaurant.
 * <p>
 * This class is abstract and is thus unimplemented. It is extended by Manager, Chef, and Server.
 * <p>
 * All Staff members are able to be occupied with an order, and are able to receive and scan in restocking deliveries.
 * Each staff member also has their unique id, but the id are specific to each subclass initialized and are independent
 * of other subclasses.
 */
public abstract class Staff {

    public int id;                 //The number that goes into a staff member's unique
    private Order currentOrder;     //The order that this person may currently be in charge of
    private String name;            //The staff member's name
    private Inventory inventory;    //The restaurant inventory that this staff may need access to.
    private Restaurant restaurant;  //The restaurant this staff member works at

    /**
     * The constructor for a Staff object.
     *
     * @param name       This staff member's name
     * @param restaurant The staff member's workplace restaurant.
     */
    Staff(String name, Restaurant restaurant) {

        this.name = name;
        this.restaurant = restaurant;
        this.inventory = restaurant.getInventory();
    }

    /**
     * Takes an order by setting this Staff's current charge as the order.
     * <p>
     * Staff are unable to take more than one order at a time.
     *
     * @param order The order that this Staff is taking.
     */
    void takeOrder(Order order) {
        currentOrder = order;
    }

    /**
     * Indicates that this Staff is done with whatever Order they were in charge of.
     * <p>
     * This Staff is now available to take another Order.
     */
    void doneWithOrder() {
        currentOrder = null;
    }

    /**
     * Getter for the currentOrder attribute.
     *
     * @return The Order this Staff is currently handing.
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Receives a restocking delivery of ingredients.
     * <p>
     * A currently available Staff will read incomingOrders.txt, representing a delivery of ingredients to the
     * restaurant, and add the quantity in each line to the respective ingredient in the restaurant's inventory.
     * <p>
     * Once the receiver is done scanning in ingredients, they are wiped from incomingOrders.txt, which will then be
     * rewritten for the next delivery.
     *
     * @throws IOException for file reading and writing
     */
    public void receiveIncomingOrders() throws IOException {

        try (BufferedReader incomingOrders = new BufferedReader(new FileReader(
                "incomingOrders.txt"))) {
            String orderLine = incomingOrders.readLine();
            while (orderLine != null) {

                String[] ingredientToQuantity = orderLine.trim().split(",");

                String ingredient = ingredientToQuantity[0].trim();                //get ingredient
                int quantity = Integer.parseInt(ingredientToQuantity[1].trim());         //get quantity

                getInventory().changeIngredientAmt(ingredient, quantity);               //update inventory
                orderLine = incomingOrders.readLine();
            }

            new PrintWriter("incomingOrders.txt")
                    .close();      //delete current contents of incomingOrders

        } catch (IOException e) {
            throw new IOException("Filename not found!");
        }
    }


    /**
     * Restocks inventory even if not below threshold.
     * Sends a staff receiver to restock ingredients right away.
     */
    public void restockIngredients(String ingredient, int quantity) throws IOException {
        getInventory().changeIngredientAmt(ingredient, quantity);
    }


    /**
     * Getter for the name attribute. Yet unused but is in preparation for phase 2 displays.
     *
     * @return This Staff's name.
     */
    String getName() {
        return name;
    }

    /**
     * Getter of the inventory attribute.
     *
     * @return The restaurant's inventory, into which this Staff may scan items.
     */
    Inventory getInventory() {
        return inventory;
    }

    /**
     * Getter for the id attribute
     *
     * @return This Staff's id.
     */
    int getId() {
        return id;
    }

    /**
     * Setter for the id attribute
     *
     * @param id The new id for this Staff
     */
    void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for the restaurant attribute
     *
     * @return The restaurant that this staff works at.
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Setter for the restaurant attribute
     *
     * @param restaurant The restaurant that this staff works at.
     */
    void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * Abstract method that provides a String representation for each subclass
     *
     * @return A String representation of this Staff subclass
     */
    @Override
    public String toString() {
        String output = String.format("%s: %s", getClass(), getName());
        return output;

    }
}