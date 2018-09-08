package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.channels.FileChannel;

/**
 * Manager class keeps track of instance attributes of Inventory class and orders more when necessary. Manager may also
 * receive deliveries.
 *
 * @author Group 0220
 */
public class Manager extends Staff implements Serializable {

    /**
     * Constructor of Manager class that assigns them their name and their respective Restaurant class
     *
     * @param name       String of the name of the manager
     * @param restaurant Restaurant of which restaurant the manager occupies
     */
    public Manager(String name, Restaurant restaurant) {
        super(name, restaurant);
        restaurant.getStaffSystem().addManager(this);

    }

    // Precondition: args must be called in format: modifyDelivery("carrot,20", "carrot,60")

    /**
     * Changes the current amount of ingredient to be delivered to a new amount in incomingOrders.txt
     *
     * @param oldIngredientToAmount String of the ingredient and amount of ingredient that is to be changed
     *                              in format: "ingredient,double"
     * @param newIngredientToAmount String of the ingredient and amount of ingredient that it is to be changed to
     *                              in format: "ingredient,double"
     */
    public void modifyDelivery(String oldIngredientToAmount, String newIngredientToAmount) { //format: "ingredient, amount"
        File fileToBeModified = new File("incomingOrders.txt");


        String oldContent = "";

        BufferedReader reader = null;
        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));

            //Reading all the lines of input text file into oldContent

            String line = reader.readLine();

            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();

                line = reader.readLine();
            }

            //Replacing oldIngredientToAmount with newIngredientToAmount in the oldContent

            String newContent = oldContent.replaceAll(oldIngredientToAmount, newIngredientToAmount);

            //Rewriting the input text file with newContent

            writer = new FileWriter(fileToBeModified);

            writer.write(newContent);

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                reader.close();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //create email incomingOrders from requests.txt with default quantities.
    //source: https://stackoverflow.com/questions/2520305/java-io-to-copy-one-file-to-another

    /**
     * Writes orders that are incoming (to being delivered).
     *
     * @throws IOException For file reading and writing
     */
    public void writeIncomingOrders() throws IOException {
        try {
            File file1 = new File("requests.txt");
            File file2 = new File("incomingOrders.txt");


            FileChannel src = new FileInputStream(file1).getChannel();          //copy and paste into new file
            FileChannel dest = new FileOutputStream(file2).getChannel();
            dest.transferFrom(src, 0, src.size());

            //erase copied file contents
            new PrintWriter("requests.txt").close();


        } catch (IOException e) {
            throw new IOException("File not found!");
        }
    }

    /**
     * String representation of Manager class, which is their name
     *
     * @return String of the Manager's name
     */
    public String toString() {

//        return this.getName();
        return String.format("Manager: %s", getName());
    }


}


