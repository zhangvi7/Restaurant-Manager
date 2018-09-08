package Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Server class.
 * <p>
 * A Server is in charge of entering Orders into the restaurant's system and delivering prepared Orders to the tables.
 * Additionally, anything issues with Orders that would impact the table's Bill are resolved through this class.
 *
 * @author Group 0220
 */
public class Server extends Staff {

    private static int numServers = 0;

    /**
     * Constructor for the Server class.
     * <p>
     * Each time a Server is initialized, the static numServers counter is incremented
     * by 1, and this Server is added to the restaurant's collection of Servers.
     *
     * @param name       The name of this Server.
     * @param restaurant The restaurant that employs this server.
     */
    Server(String name, Restaurant restaurant) {
        super(name, restaurant);
        numServers++;
        setId(numServers);
        restaurant.getStaffSystem().addServer(this);
    }

    /**
     * This Server picks up a prepared Order, ready to be delivered. A Server can only pick up an order if they are not
     * currently occupied with another one.
     *
     * @param order The Order to be picked up by this Server
     */
    public void pickUpOrder(Order order) {
        if (order.isPrepared() && getCurrentOrder() == null) {
            takeOrder(order);
            EventLogger.log("Order #" + order.getOrderNum() + " has been picked up by server " + this.getName());
        }
    }

    /**
     * This Server delivers the Order that they are currently carrying. When the Order is delivered, it is added to the
     * table's Bill.
     */
    public void deliverOrder() {
        Order order = getCurrentOrder();
        if (order != null) {

            int tableNum = order.getTableNum();
//            getRestaurant().getTables().get(tableNum).receiveOrder(order);
            order.setDelivered(true);
            getRestaurant().getOrderSystem().deliverOrder(getCurrentOrder());
            doneWithOrder();
            EventLogger.log("Order #" + order.getOrderNum() + " has been delivered by server " + this.getName());
        }
    }

    /**
     * Prints the Bill of the table with the given number. If this table has no Bill to be paid, print an indicative
     * message.
     *
     * @param tableNum The number of the table whose Bill is being printed.
     */
    void printBill(int tableNum) {
        String receipt = getTableBill(tableNum);
        System.out.println(receipt);
        getRestaurant().getStaffSystem().addServer(this);
    }

    public String getTableBill(int tableNum) {
        return getRestaurant().getTables().get(tableNum).getCombinedBillString();
    }

    public String getSeatBill(int tableNum, int seatNum) {
        return getRestaurant().getTables().get(tableNum).getBills()[seatNum].toString();
    }

    /**
     * After delivery, cancels an order by removing it from a table's Bill. It is not added back into the system for a redo.
     *
     * @param order The order to be cancelled.
     */

    public void cancelOrder(Order order) {

        ArrayList<Table> restaurantTables = getRestaurant().getTables();
        Bill targetBill = restaurantTables.get(order.getTableNum()).findBillWithOrder(order);
        targetBill.removeOrder(order);
        restaurantTables.get(order.getTableNum()).getReceivedOrders().remove(order);
        getRestaurant().getOrderSystem().getUnseenOrders().remove(order);
        EventLogger.log("Order #" + order.getOrderNum() + " has been cancelled by server " + this.getName());
    }

    /**
     * After delivery, cancels an order by removing it from a table's Bill. All of its progress is then undone to
     * restart the process of preparing it.
     *
     * @param order The order to be redone.
     */
    void redoOrder(Order order) {
        cancelOrder(order);
        order.setPrepared(false);
        order.setDelivered(false);
        order.setSeen(false);
        getRestaurant().getOrderSystem().addOrder(order);

        EventLogger.log("Order #" + order.getOrderNum() + " has been sent back by Server " + this.getName());
    }

    /**
     * A String representation of this Server.
     * Formatted as "server[id]"
     *
     * @return The String representation
     */
    @Override
    public String toString() {

        String output = String.format("Server #%d: %s", getId(), getName());

        return output;

    }

    /**
     * Create a newOrder with the required Dish class and tableNumber that ordered.
     *
     * @param newDish     The Dish that the customer is ordering.
     * @param tableNumber The table number that is ordering.
     */
    public boolean createNewOrder(Dish newDish, int tableNumber, int seatNum) {
        Order newOrder = new Order(newDish, tableNumber, seatNum);

        if (checkValidIngredients(newOrder)) {
            Table currentTable = getRestaurant().getTables().get(tableNumber);
            getRestaurant().getOrderSystem().addOrder(newOrder);
            useIngredients(newOrder);

            currentTable.receiveOrder(newOrder);
            if (getRestaurant().getEmptyTables().contains(currentTable)) {
                getRestaurant().getEmptyTables().remove(currentTable);
            }
            EventLogger.log("Order #" + newOrder.getOrderNum() + " containing " + newOrder.getDish().toString() +
                    " has been taken by server " + this.getName());
            return true;
        } else {
            System.out.println("Not enough ingredients!");
            return false;

        }
    }

    public boolean checkValidIngredients(Order order) {
        boolean possible = true;

        HashMap<String, Double> currentStock = this.getInventory().getStock();

        for (HashMap.Entry<String, Double> ingredient : order.getDish().getIngredients().entrySet()) {
            String key = ingredient.getKey();
                if (!currentStock.containsKey(key) || currentStock.get(key) < ingredient.getValue()) {
                    possible = false;
                    getRestaurant().getOrderSystem().cancelOrder(order);
                    try {
                        getInventory().writeRequest(ingredient.getKey());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
            }
            return possible;
        }

    private void useIngredients(Order order) {

        for (HashMap.Entry<String, Double> ingredient : order.getDish().getIngredients()
                .entrySet()) {
            String key = ingredient.getKey();
            try {
                getRestaurant().getInventory().changeIngredientAmt(key, ingredient.getValue() * -1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


