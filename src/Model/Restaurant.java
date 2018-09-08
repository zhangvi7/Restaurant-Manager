package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Restaurant class unifies all the other classes to simulate an actual restaurant.
 */
public class Restaurant {

    private static Restaurant restaurantInstance = new Restaurant();
    String revenueSystemPath = "./RevenueSystem.ser";
    String inventoryPath = "./Inventory.ser";
    String restaurantMenuPath = "./Menu.ser";
    private Inventory inventory; // The inventory that this restaurant has.
    private Menu restaurantMenu; // The menu that this restaurant has to order from.
    private ArrayList<Table> tables; //An array of all Tables in the Restaurant.
    private ArrayList<Table> emptyTables;
    private RevenueSystem revenueSystem;
    private StaffSystem staffSystem;
    private OrderSystem orderSystem;
    /**
     * Initializes the Restaurant by creating instances of attributes and calling constructors.
     */
    private Restaurant() {
        try {

            inventory = new Inventory(inventoryPath);
            restaurantMenu = new Menu(restaurantMenuPath);
            tables = new ArrayList<>(20);
            revenueSystem = new RevenueSystem(this, revenueSystemPath);           //Initialize Systems
            staffSystem = new StaffSystem(this);
            orderSystem = new OrderSystem(this);

            initializeTables();
            initializeStaff();          //Create staffs from file and add to respective lists.

            emptyTables = (ArrayList<Table>) tables.clone();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns Restaurant for singleton design pattern.
     *
     * @return this restaurant instance.
     */
    public static Restaurant getRestaurantInstance() {
        return restaurantInstance;
    }

    /**
     * Initializes all staff. Staff are added to restaurant lists
     * through respective staff constructors. Staff are read from Staff.txt file.
     */
    private void initializeStaff() throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(
                "Staff.txt"))) {

            String line = fileReader.readLine();

            while (line != null) {
                String[] staffLine = line.split("-");             // Split each line in file into array.

                String staffType = staffLine[0].trim();
                String[] staffNames = staffLine[1].trim().split(",");

                if (staffType.equals("Chef")) {
                    for (String staffName : staffNames) {
                        new Chef(staffName.trim(), this);
                    }
                } else if (staffType.equals("Server")) {
                    for (String staffName : staffNames) {
                        new Server(staffName.trim(), this);
                    }
                } else if (staffType.equals("Manager")) {
                    for (String staffName : staffNames) {
                        new Manager(staffName.trim(), this);
                    }
                } else {
                    int i = 0;
                    for (String id : staffNames) {
                        staffSystem.managers.get(i++).setId(Integer.parseInt(id.trim()));
                    }
                }

                line = fileReader.readLine();
            }
        }
    }

    /**
     * Initializes 20 tables from tables.txt and stores in tables attribute.
     * Tables are of capacity 4 or 8.
     */
    private void initializeTables() throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(
                "Table.txt"))) {

            String line = fileReader.readLine();

            while (line != null) {

                String[] tableLine = line.split("-");             // Split each line in file into array.

                Integer tableAmount = Integer.parseInt(tableLine[1].trim());
                Integer tableCapacity = Integer.parseInt(tableLine[0].split(",")[1].trim());

                for (int i = 0; i < tableAmount; i++) {
                    tables.add(new Table(tableCapacity));
                }

                line = fileReader.readLine();
            }
        }
    }

    /**
     * Getter for the current inventory of Restaurant.
     *
     * @return This Restaurant's inventory attribute.
     */
    public Inventory getInventory() {
        return inventory;
    }


    /**
     * Getter for tables attribute.
     */
    public ArrayList<Table> getTables() {
        return tables;
    }


    /**
     * Getter for restaurantMenu attribute.
     */
    public Menu getRestaurantMenu() {
        return restaurantMenu;
    }

    /**
     * Getter that returns the revenue system.
     *
     * @return revenue system associated with restaurant.
     */
    public RevenueSystem getRevenueSystem() {
        return revenueSystem;
    }

    /**
     * Getter that returns the staff system.
     *
     * @return staff system associated with restaurant.
     */
    public StaffSystem getStaffSystem() {
        return staffSystem;
    }

    /**
     * Getter that returns the order system.
     *
     * @return order system associated with restaurant.
     */
    public OrderSystem getOrderSystem() {
        return orderSystem;
    }

    /**
     * Serializing the data of this class.
     */
    public void SaveFiles() {
        try {
            inventory.saveToFile(inventoryPath);
            restaurantMenu.saveToFile(restaurantMenuPath);
            revenueSystem.saveToFile(revenueSystemPath);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Getter that returns the empty tables in restaurant.
     *
     * @return collection of empty tables.
     */
    public ArrayList<Table> getEmptyTables() {
        return emptyTables;
    }


}
