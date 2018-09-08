package Model;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * What happens currently is that a Table has an Array of Bills, one cell for each seat. Orders are received by adding
 * initializing Bills in each cell and adding Orders to them.
 * <p>
 * A table keeps track of the number of customers sitting at it and all orders that have been received so far at the
 * Table.
 */
public class Table {

    private static int tableCount = 0;  //keeps track of unique table number
    private int capacity;   //Maximum number of people that this table can seat.
    private int tableNum;   //The unique number of this Table.
    private Bill[] bills;   //The array of bills, one per seat, belonging to this table.
    private int numCustomers;   //Number of people sitting at this table
    private boolean occupied;   //flag for if this table is currently occupied
    private ObservableList<Order> receivedOrders;   //All Orders that have been received by this Table.

    /**
     * Constructor for a Table.
     * A Table is, upon initialization, empty and has no Orders received so far. All Bills are prepared but empty.
     *
     * @param capacity The max number of seats at this table.
     */
    public Table(int capacity) {
        this.capacity = capacity;
        this.tableNum = tableCount++;
        bills = new Bill[capacity];

        for (int i = 0; i < capacity; i++) {
            bills[i] = new Bill(tableNum);
        }

        numCustomers = 0;
        occupied = false;
        receivedOrders = FXCollections.observableArrayList();
    }

    /**
     * Getter for the bills attribute.
     *
     * @return An array of one Bill per seat for this table.
     */
    public Bill[] getBills() {
        return bills;
    }

    /**
     * Getter for the occupied attribute.
     *
     * @return If this Table is currently holding any customers.
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Getter for tableNum attribute
     *
     * @return The unique numerical identifier of this table.
     */
    public int getTableNum() {
        return tableNum;
    }

    /**
     * Getter for numCustomers attribute.
     *
     * @return The number of customers currently sitting at this table.
     */
    public int getNumCustomers() {
        return numCustomers;
    }

    /**
     * Returns an array of non-empty bills, representing all the Bills that are being paid and to be processed by the
     * RevenueSystem. The payment time of each bill is also set at the time of payment.
     * <p>
     * Resets the state of the Table to an empty table, with 0 customers and empty bills.
     *
     * @return Array of non-empty Bills that this table will pay for.
     */
    public Bill[] payBills() {

        ArrayList<Bill> collectingBills = new ArrayList<>(0);

        for (int i = 0; i < capacity; i++) {
            if (bills[i].getAmount() > 0) {
                collectingBills.add(bills[i]);
                bills[i].setPaymentTime();
                Restaurant.getRestaurantInstance().getRevenueSystem().getAllBills().add(bills[i]);
            }
            clearBill(i);
        }
        occupied = false;
        numCustomers = 0;

        receivedOrders.clear();

        Restaurant.getRestaurantInstance().getEmptyTables().add(this);

        return collectingBills.toArray(new Bill[collectingBills.size()]);
    }

    /**
     * Returns a Bill for the Table, with all Bills split by seat. This will be displayed in the resultant pop-up
     * window. Indicates which Bills are empty and separates seats with dashed lines.
     *
     * @return A split-by-seat bill for the table.
     */
    public String getSplitBillString() {
        StringBuilder splitReceipt = new StringBuilder("");
        for (Bill bill : bills) {
            splitReceipt.append(bill.toString()).append(System.lineSeparator());
            splitReceipt.append("-----------").append(System.lineSeparator());
        }
        return splitReceipt.toString();
    }

    /**
     * Receives an order from the kitchen.
     * <p>
     * When a Table receives an order, it is added to the Bill of the seat that ordered it (by seatNum) and is added to
     * the list of receivedOrders for this table.
     * <p>
     * Additionally, if a Bill is receiving its first order, numCustomers is increased by 1.
     *
     * @param order The Order being received.
     */
    public void receiveOrder(Order order) {

        receivedOrders.add(order);
        occupied = true;

        Bill targetBill = bills[order.getSeatNum()];

        if (targetBill.getOrders().size() == 0) {
            numCustomers++;
        }

        if (numCustomers == 8) {
            for (Bill bill : bills) {
                bill.mandatoryGratuity();
            }
        }
        targetBill.addOrder(order);
    }

    /**
     * Get a printed Bill for this entire Table, with all individual seat bills combined. This will show up in the pop-
     * up window when printing the bill.
     *
     * @return A full-table Bill for the entire table.
     */
    public String getCombinedBillString() {

        Bill combinedBill = new Bill(tableNum);

        for (Bill bill : bills) {
            for (Order order : bill.getOrders()) {
                combinedBill.addOrder(order);
            }
        }

        return combinedBill.toString();
    }

    /**
     * Getter for receivedOrders attribute.
     *
     * @return list of all orders this table has received.
     */
    public ObservableList<Order> getReceivedOrders() {
        return receivedOrders;
    }

    /**
     * Find a Bill that contains an order.
     *
     * @param order The Order that the resultant bill needs to contain.
     * @return The resultant bill.
     */
    public Bill findBillWithOrder(Order order) {

        Bill result = null;

        for (Bill bill : bills) {
            if (bill != null && bill.contains(order)) {
                result = bill;
                break;
            }
        }

        return result;
    }

    /**
     * toString method for this class.
     *
     * @return Returns a string id for this table in the form "Table #[num]"
     */
    public String toString() {
        return "Table #" + tableNum;
    }

    /**
     * Get all the orders that go to a specific seat at this table
     *
     * @param seatNum The number of the seat for which orders are needed
     * @return The list of orders that were sent to this seat.
     */
    public ObservableList<Order> getOrdersBySeat(int seatNum) {

        ObservableList<Order> result = FXCollections.observableArrayList();

        for (Order order : receivedOrders) {
            if (order.getSeatNum() == seatNum) {
                result.add(order);
            }
        }

        return result;
    }

    /**
     * Combine the Bills of two seats at this table by adding all of seatTwo's orders to seatOne's bill, then clearing
     * seatTwo's bill.
     * <p>
     * This method enables manually splitting the bill of a table beyond just by seat.
     *
     * @param seatOne The seat whose bill is being added to.
     * @param seatTwo The seat whose orders are being added to seatOne and then cleared.
     */
    public void combineBills(int seatOne, int seatTwo) {

        for (Order order : bills[seatOne].getOrders()) {
            bills[seatOne].addOrder(order);
        }
        clearBill(seatTwo);
    }

    /**
     * Clear a Bill by re-initializing it as a new Bill, wiping it of its orders.
     *
     * @param seatNum The seat number of the bill being cleared.
     */
    private void clearBill(int seatNum) {
        bills[seatNum] = new Bill(tableNum);
    }

    /**
     * Getter for the capacity attribute of this table.
     *
     * @return the maximum number of people this table can hold. i.e., its default number of seats.
     */
    public int getCapacity() {
        return capacity;
    }
}
