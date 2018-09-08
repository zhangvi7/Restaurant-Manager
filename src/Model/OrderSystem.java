package Model;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The OrderSystem class.
 * The OrderSystem class manages all types of orders in restaurant (i.e. unseen, unprepared, and prepared).
 */
public class OrderSystem {

    private Restaurant restaurant;

    private ObservableList<Order> unseenOrders;
    private ObservableList<Order> unpreparedOrders;
    private ObservableList<Order> preparedOrders;

    /**
     * Initializes a new OrderSystem with collections of orders.
     *
     * @param restaurant associated with this system.
     */
    OrderSystem(Restaurant restaurant) throws IOException, ClassNotFoundException {
        this.restaurant = restaurant;

        unseenOrders = FXCollections.observableArrayList();
        unpreparedOrders = FXCollections.observableArrayList();
        preparedOrders = FXCollections.observableArrayList();

    }

    /**
     * Getter that returns restaurant using this system.
     *
     * @return restaurant associated with this system
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Getter that returns collection of prepared orders.
     *
     * @return list of prepared orders.
     */
    public ObservableList<Order> getPreparedOrders() {
        return preparedOrders;
    }
    

    /**
     * Getter that returns collection of unseen orders.
     *
     * @return list of unseen orders.
     */
    public ObservableList<Order> getUnseenOrders() {
        return unseenOrders;
    }

    /**
     * Getter that returns collection of unprepared orders.
     *
     * @return list of unprepared orders.
     */
    public ObservableList<Order> getUnpreparedOrders() {
        return unpreparedOrders;
    }


    /**
     * Adds an Order to the unseen orders in this Restaurant.
     *
     * @param order The order that is to be added to the unseen orders.
     */
    public void addOrder(Order order) {

        getUnseenOrders().add(order);
    }

    /**
     * Cancels an Order from unseen orders in this Restaurant.
     *
     * @param order The order that is to be removed from unseen orders.
     */
    public void cancelOrder(Order order) {
        getUnseenOrders().remove(order);
    }

    /**
     * Delivers an a prepared order in this Restaurant.
     *
     * @param order The order that is to be delivered and removed from prepared orders.
     */
    public void deliverOrder(Order order) {
        getPreparedOrders().remove(order);
    }

    /**
     * Mark an order as seen and prepare to cook it.
     *
     * @param order marked as seen and ready to be cooked.
     */
    public void seenOrder(Order order) {
        getUnseenOrders().remove(order);
        getUnpreparedOrders().add(order);
    }

    /**
     * Mark an order as prepared and ready to deliver.
     *
     * @param order that is prepared.
     */
    public void preparedOrder(Order order) {
        getUnpreparedOrders().remove(order);
        getPreparedOrders().add(order);

    }
}
