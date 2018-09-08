package Model;

import java.io.Serializable;

/**
 *
 * An Order contains a Dish and tracks its progress through the restaurant and kitchen.
 * <p>
 * All Staff interact with an Order rather than the Dish it encapsulates.
 *
 * @author Luke Zhang
 */
public class Order implements Serializable {

    private static int numOrders = 0; //Counts all orders made throughout program runtime.
    private boolean seen;             //Indicates if this Order has been seen by a Chef.
    private boolean prepared;         //Indicates if a Chef has completed preparing this Order.
    private boolean delivered;        //Indicates if a Server has successfully delivered this Order to a table.
    private Dish dish;                //The Dish that is contained in this Order.
    private int orderNum;             //The unique numerical identifier of this Order. The first Order has orderNum 1.
    private int tableNum;             //The number of the table that made this Order.

    private int seatNum;

    /**
     * Constructor of the Order class. An Order starts with all its "progress flags" (seen, prepared, delivered) as false.
     * When an Order is created, numOrders is incremented by 1, and then its value is assigned as this order's number.
     *
     * @param dish  The Dish that is represented by this Order.
     * @param table The number of the table that made this Order.
     */
    public Order(Dish dish, int table, int seat) {

        numOrders++;

        seen = false;
        prepared = false;
        delivered = false;
        this.dish = dish;
        orderNum = numOrders;
        tableNum = table;
        seatNum = seat;
    }
    
    /**
     * Getter for seatNum attribute.
     *
     * @return The unique number id for this seat on this table.
     */
    public int getSeatNum() {
        return seatNum;
    }

    /**
     * Setter for the seen attribute to a given boolean.
     *
     * @param s The new value of the seen attribute.
     */
    void setSeen(boolean s) {
        seen = s;
    }

    /**
     * Getter for the prepared attribute.
     *
     * @return if this Order has been prepared by a Chef
     */
    boolean isPrepared() {
        return prepared;
    }

    /**
     * Setter for the prepared attribute to a given boolean.
     *
     * @param p The new value of the prepared attribute.
     */
    void setPrepared(boolean p) {
        prepared = p;
    }

    /**
     * Setter for the seen attribute to a given boolean.
     *
     * @param d The new value of the seen attribute.
     */
    void setDelivered(boolean d) {
        delivered = d;
    }

    /**
     * Provides a String representation of this Order, which can also be used as a unique identifier.
     *
     * @return This Order expressed as a String.
     */
    public String toString() {
        String output = String.format("Order# %d, Table: %d, Seat: %d, Dish: %s\n", orderNum
                , getTableNum(), getSeatNum(), getDish());
        return output;
    }

    /**
     * Getter for this Order's number
     *
     * @return this Order's number
     */
    public int getOrderNum() {
        return orderNum;
    }

    /**
     * Getter for this Order's table number
     *
     * @return this Order's table number
     */
    public int getTableNum() {
        return tableNum;
    }

    /**
     * Getter for the dish attribute
     *
     * @return the Dish that is housed by this Order.
     */
    public Dish getDish() {
        return dish;
    }

}
