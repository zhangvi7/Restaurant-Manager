package Model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * A Bill represents all Orders to a specific seat of a table in the restaurant, and tracks the total price of all
 * Dishes ordered.
 *
 * @author Group 0220
 */
public class Bill implements Serializable {

    private static DecimalFormat rounder = new DecimalFormat("#.##");
    public double amount;               //The total price of all Dishes within the Orders of this Bill.
    public ZonedDateTime paymentTime;   //The system time when this bill is paid
    private ArrayList<Order> orders;    //A collection of all orders added to this Bill.
    private int tableNum;               //The number of the table for this Bill. The first table has number 0.
    private boolean gratuityRequired;   //If this table is required to pay the 15% gratuity

    /**
     * The Bill class constructor. A Bill, when initialized, has no Orders added to it yet. It's total amount is $0.00.
     *
     * @param tableNum The number assigned to the table to which this Bill belongs.
     */
    Bill(int tableNum) {
        this.tableNum = tableNum;
        orders = new ArrayList<>(0);
        amount = 0;
        gratuityRequired = false;
    }

    /**
     * Set the payment time of this Bill by using the current system time in time zone GMT-5.
     */
    void setPaymentTime() {
        paymentTime = ZonedDateTime.now(ZoneId.of("America/Toronto"));
    }


    /**
     * Creates a string representation of the payment time of this bill, containing the date and time, down to the
     * second of payment.
     *
     * @return
     */
    public String getPaymentTimeString() {

        String prelimTime = paymentTime.toString();
        String date = prelimTime.split("T")[0];
        String splitTime = prelimTime.split("-")[2];
        String time = splitTime.split("\\.")[0].substring(3);

        return date + " " + time;
    }

    /**
     * Add an order to this Bill, and increment the amount of this Bill by the ordered dish's price.
     *
     * @param order The Order to be added.
     */
    void addOrder(Order order) {
        if (order.getTableNum() == this.tableNum && !orders.contains(order)) {
            orders.add(order);
            amount += order.getDish().getPrice();
        }
    }

    /**
     * Remove an order from this Bill, and decrement the amount of this Bill by the ordered dish's price.
     *
     * @param order The order to be removed.
     */
    void removeOrder(Order order) {
        orders.remove(order);
        amount -= order.getDish().getPrice();
    }

    /**
     * Provide a String representation of this Bill, in the form of a full receipt.
     * <p>
     * The first line of the receipt indicates the table to which it belongs, by its table number.
     * All subsequent lines until the last are numbered entries, of each Order's dishName and its price, listed out.
     * The final line is a total amount for the Bill, and is what the customer has to pay.
     *
     * @return A receipt for this table.
     */
    public String toString() {
        StringBuilder result = new StringBuilder("Bill for Table " + tableNum + System.lineSeparator());

        if (orders.size() > 0) {
            for (int i = 0; i < orders.size(); i++) {
                Dish orderDish = orders.get(i).getDish();
                result.append(i + 1).append(". ").append(orderDish.toString());
                result.append(": $").append(Double.toString(orderDish.getPrice())).append(System.lineSeparator());
            }
            result.append("Subtotal: $").append(amount).append(System.lineSeparator());

            result.append("Total: $");

            double total = amount * 1.13;
            if (gratuityRequired) {
                total += amount * 1.15;
            }

            result.append(rounder.format(total));

            return result.toString();

        } else {
            return "Blank bill for Table " + tableNum;
        }
    }

    /**
     * Getter for the table number of this Bill.
     *
     * @return the table number of this Bill.
     */
    public int getTableNum() {
        return tableNum;
    }

    /**
     * Getter for the total price of all Dishes on this Bill.
     *
     * @return the total price of this Bill.
     */
    double getAmount() {
        return amount;
    }

    /**
     * Searches for a given Order in this Bill and returns true if it has it.
     *
     * @param order The Order to be searched for in this Bill.
     * @return If this Bill contains the Order.
     */
    public boolean contains(Order order) {
        return orders.contains(order);
    }

    /**
     * Getter for the ArrayList of Orders that this Bill contains.
     *
     * @return The ArrayList of Orders.
     */
    ArrayList<Order> getOrders() {
        return orders;
    }

    void mandatoryGratuity() {
        gratuityRequired = true;
    }

}