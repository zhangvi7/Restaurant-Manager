package Model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Observable;

/**
 * Initializes an Inventory class, which will keep track of all the stock of ingredients and their minimum amount
 * required for the restaurant. Also manipulates these amounts through addition and subtraction,
 * and performs actions such as ordering more.
 *
 * @author Group 0220
 */
public class Inventory extends Observable {

    private HashMap<String, Double> stock = new HashMap<>(); // The name of all the ingredients and the amount available
    private HashMap<String, Double> stockThreshold = new HashMap<>(); //The name of all the ingredients
    // and their minimum thresholds

    /**
     * Constructor for inventory which uses a method to read a file to initialize its instance attributes
     */
    public Inventory(String filePath) throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        if (file.exists()) {
            readFromFile(filePath);
        } else {
            file.createNewFile();
            readFile();
        }
    }

    ///code help from https://stackoverflow.com/questions/20365379/creating-a-constructor-to-read-a-txt-file - remember to cite formally

    /**
     * Helper that reads ingredients.txt to format into instance attribute stock and stockThreshold for Inventory
     */
    private void readFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("ingredients.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] foodInfo = line.split(":");
                String[] amtAndThreshold = foodInfo[1].split(",");
                String food = foodInfo[0];
                double[] dblArray = new double[2];
                dblArray[0] = (Double.parseDouble(amtAndThreshold[0]));
                dblArray[1] = (Double.parseDouble(amtAndThreshold[1]));
                if (stock.containsKey(food)) {
                    double result = stock.get(food) + dblArray[0];
                    this.stock.put(food, result);
                    this.stockThreshold.put(food, dblArray[1]);
                } else {
                    this.stock.put(food, dblArray[0]);
                    this.stockThreshold.put(food, dblArray[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter to return the current amount of ingredients.
     *
     * @return the amount of ingredients for all ingredients in Inventory in the form of a HashMap
     */
    public HashMap<String, Double> getStock() {
        return stock;
    }

    /**
     * Checks to see if ingredient is below the minimum amount of ingredient required.
     *
     * @param ingredient String of the name of the ingredient that is to be checked.
     * @return boolean of if the amount of ingredient is below the minimum threshold.
     */
    private boolean belowThreshold(String ingredient) {
        return stock.get(ingredient) < stockThreshold.get(ingredient);
    }

    /**
     * Adds or subtracts ingredient from current stock of ingredient.
     *
     * @param ingredient String of name of ingredient to be added/subtracted
     * @param amount     Double of the amount of ingredient to be added/subtracted
     * @throws IOException For file reading and writing
     */
    void changeIngredientAmt(String ingredient, double amount) throws IOException {
        stock.put(ingredient, stock.get(ingredient) + amount);

        EventLogger.log("An adjustment of " + amount + " " + ingredient + " has been made");
        if (belowThreshold(ingredient)) {
            writeRequest(ingredient);
        }
    }

    //cite help from https://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/

    /**
     * Writes a request in requests.txt for more ingredient, with a default of 20.
     * Then calls a manager class to continue further action.
     * future:request can also be made without ingredient stock going below ingredient threshold
     *
     * @param ingredient String of ingredient of which to write a request for
     * @throws IOException For file reading and writing
     */
    public void writeRequest(String ingredient) throws IOException {
        try (FileWriter fw = new FileWriter("requests.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            String output = String.format("%s,%d", ingredient, 20);
            out.println(output);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            setChanged();
            notifyObservers(ingredient);
            System.out.println(ingredient + " has fallen below the specified threshold, a request has been written");
            EventLogger.log("A request for " + ingredient + " has been written in requests.txt");
        }
    }

    /**
     * Deletes the line of request of ingredient from requests.txt, after an email has been sent for more ingredient.
     *
     * @param ingredient String of the ingredient whose request in requests.txt will be deleted
     */
    public void deleteRequest(String ingredient) {
        try (BufferedReader file = new BufferedReader(new FileReader("requests.txt"))) {
            String line;
            String input = "";
            while ((line = file.readLine()) != null) {
                String[] foodInfo = line.split(",");
                if (foodInfo[0].equals(ingredient)) {
                    line = "";
                }
                input += line + System.lineSeparator();
            }
            FileOutputStream newFile = new FileOutputStream("requests.txt");
            newFile.write(input.getBytes());
            file.close();
            newFile.close();
            EventLogger.log("The request for " + ingredient + "has been deleted from requests.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            //deserialize the Map
            HashMap<String, HashMap<String, Double>> serializeHash = (HashMap<String, HashMap<String, Double>>) input.readObject();
            stock = serializeHash.get("stock");
            stockThreshold = serializeHash.get("stockThreshold");
            input.close();
        } catch (IOException ex) {
        }
    }

    public void saveToFile(String filePath) throws IOException {

        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);


        // Storing HashMap in HashMap
        HashMap<String, HashMap<String, Double>> serializeHash = new HashMap<>();
        serializeHash.put("stock", stock);
        serializeHash.put("stockThreshold", stockThreshold);

        // Serializing HashMap
        output.writeObject(serializeHash);
        output.close();
    }

    /**
     * Representation of Inventory class as a string
     *
     * @return stock as a string
     */
    @Override
    public String toString() {
        return getStock().toString();
    }
}
