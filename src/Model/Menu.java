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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The menu class.
 * <p>
 * the menu contains all the items offered by the restaurant. Its contents are read from a file and displayed in
 * a drop-down box in the program.
 */
public class Menu {

    //The list of menu items.
    private ObservableList<MenuItem> menu;

    /**
     * Initialize a new menu by adding MenuItems read from menu.txt
     *
     * @param filePath The location and name of the file in the directory
     * @throws IOException            For file reading and writing
     * @throws ClassNotFoundException For reading from a file.
     */
    public Menu(String filePath) throws IOException, ClassNotFoundException {

        menu = FXCollections.observableArrayList();

        File file = new File(filePath);
        if (file.exists()) {
            readFromFile(filePath);
        } else {
            file.createNewFile();
            loadMenu();
        }
    }

    /**
     * Reads menu.txt to "load" a menu into the program upon running.
     * <p>
     * loadMenu() iterates over each line of menu.txt, identifies the dish name, the names of each ingredient required,
     * and the quantity of each ingredient required, and adds maps them in a HashMap. The HashMap takes a integer and
     * maps it to a MenuItem, effectively numbering each menu entry.
     *
     * @return A HashMap of integers to MenuItems.
     * @throws IOException For file reading.
     */
    private void loadMenu() throws IOException {

        String[] firstSplit;
        String[] menuLine;
//        ObservableList<MenuItem> result = FXCollections.observableArrayList();
        Scanner scanner = new Scanner(new FileInputStream("menu.txt"));

        String tagsBeforeSplit;
        String[] splitTags;
        ArrayList<String> tags = new ArrayList<>(0);

        while (scanner.hasNextLine()) {

            firstSplit = scanner.nextLine().split("\\(");

            if (firstSplit.length > 1) {
                tagsBeforeSplit = firstSplit[1].substring(0, firstSplit[1].length() - 1);
                splitTags = tagsBeforeSplit.split(",");
                tags = new ArrayList<>(Arrays.asList(splitTags));
            }

            menuLine = firstSplit[0].split("\\|");
            int size = menuLine.length;
            HashMap<String, Double> ingredientHash = new HashMap<>();


            for (int i = 1; i < size; i++) {
                String[] ingredientPart = menuLine[i].split(",");
                ingredientHash.put(ingredientPart[0].trim(), Double.parseDouble(ingredientPart[1].trim()));
            }

            String[] details = menuLine[0].split(",");
            MenuItem newMenuItemItem = new MenuItem(details[0].trim(), Double.parseDouble(details[1].trim()),
                    ingredientHash, tags);
            menu.add(newMenuItemItem);
        }
        scanner.close();

    }

    /**
     * Reads from the menu.txt file to load the menu.
     *
     * @param path The file directory.
     * @throws ClassNotFoundException For reading from file.
     */
    private void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            //deserialize the Map
            ArrayList<MenuItem> menuArray = (ArrayList<MenuItem>) input.readObject();
            menu = FXCollections.observableArrayList(menuArray);
            input.close();
        } catch (IOException ex) {
            Thread.currentThread().getStackTrace();
        }
    }

    /**
     * Saving the state of the Menu by writing it to a file.
     *
     * @param filePath The location of the file.
     * @throws IOException For file reading and writing.
     */
    public void saveToFile(String filePath) throws IOException {

        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // Turning ObservableList into ArrayList
        ArrayList<MenuItem> menuArray = new ArrayList<>();


        for (MenuItem menuItem : menu) {
            menuArray.add(menuItem);
        }

        // Serializing HashMap
        output.writeObject(menuArray);
        output.close();
    }

    /**
     * Getter for the menu attribute
     *
     * @return The list of all MenuItems this restaurant serves.
     */
    public ObservableList<MenuItem> getMenu() {
        return menu;
    }
}
