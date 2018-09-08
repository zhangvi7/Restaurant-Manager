package Model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * The RevenueSystem class for managing all revenue and paying functionality.
 */
public class RevenueSystem {

    Restaurant restaurant;

    private ArrayList<Bill> allBills;

    /**
     * @param restaurant this system is associated with
     * @param filePath   for serializing
     */
    RevenueSystem(Restaurant restaurant, String filePath) {
        this.restaurant = restaurant;
        allBills = new ArrayList<>();

        File file = new File(filePath);
        if (file.exists()) {
            readFromFile(filePath);
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Obtains a payment from a Table and adds it to the revenue.
     *
     * @param tableNumber of table to obtain payment from.
     */
    public void receivePayment(int tableNumber) {

        double receivedAmount = 0;
        double total = 0;

        Table payingTable = restaurant.getTables().get(tableNumber);

        if (payingTable.isOccupied()) {
            Bill[] paidBills = restaurant.getTables().get(tableNumber).payBills();
            for (Bill bill : paidBills) {
                receivedAmount += bill.getAmount();
            }

            total = receivedAmount * 1.13;

            if (paidBills.length > 0 && restaurant.getTables().get(paidBills[0].getTableNum()).getNumCustomers() >= 8) {
                total += receivedAmount * 1.15;
            }
        }
    }

    /**
     * Getter that returns all bills of restaurant.
     *
     * @return collection of all bills.
     */
    public ArrayList<Bill> getAllBills() {
        return allBills;
    }
    

    /**
     * Method to serialize the system.
     *
     * @param path name of file.
     */
    public void readFromFile(String path) {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            //deserialize the Map
            allBills = (ArrayList<Bill>) input.readObject();
            input.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Serializes HashMap.
     *
     * @param filePath path of file name.
     * @throws IOException if file not found.
     */
    public void saveToFile(String filePath) throws IOException {

        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // Serializing HashMap
        output.writeObject(allBills);
        output.close();
    }


}
