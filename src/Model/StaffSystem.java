package Model;

import java.util.ArrayList;

/**
 * The StaffSystem class for managing all staffs employed in restaurant
 */
public class StaffSystem {

    public ArrayList<Manager> managers; // An array list of the managers working at this restaurant.
    Restaurant restaurant;
    private ArrayList<Staff> allStaffs;     // A linked list of the staff who are working in this restaurant.
    private ArrayList<Chef> chefs;          // A linked list of the Chefs who are currently unoccupied.
    private ArrayList<Server> servers;      // A linked list of the Servers currently unoccupied.

    /**
     * Initializes a new Staff System that keeps tracks of chefs, servers and managers.
     *
     * @param restaurant that uses this staff system.
     */
    StaffSystem(Restaurant restaurant) {
        this.restaurant = restaurant;
        allStaffs = new ArrayList<>();
        chefs = new ArrayList<>();
        servers = new ArrayList<>();
        managers = new ArrayList<>();
    }


    /**
     * Adds an unoccupied Chef into Restaurant
     *
     * @param chef The Chef that is to be added to Restaurant.
     */
    void addChef(Chef chef) {
        chefs.add(chef);
        allStaffs.add(chef);
    }

    /**
     * Adds an unoccupied Server into this Restaurant.
     *
     * @param server the Server that is to be added to this Restaurant.
     */
    void addServer(Server server) {
        servers.add(server);
        allStaffs.add(server);
    }

    /**
     * Adds a working Manager into the Restaurant.
     *
     * @param manager The Manager that is to be added to this Restaurant.
     */
    void addManager(Manager manager) {
        managers.add(manager);
        allStaffs.add(manager);
    }

    /**
     * Getter than returns collection of all staffs.
     *
     * @return list of all staffs employed in restaurant.
     */
    public ArrayList<Staff> getAllStaffs() {
        return allStaffs;
    }

    /**
     * Return all chefs working at restaurant
     *
     * @return list of all chefs currently employed at restaurant.
     */
    public ArrayList<Chef> getChefs() {
        return chefs;
    }

    /**
     * Return all servers working at restaurant
     *
     * @return list of all servers currently employed at restaurant.
     */
    public ArrayList<Server> getServers() {
        return servers;
    }

    /**
     * Return all managers working at restaurant
     *
     * @return list of all managers currently employed at restaurant.
     */
    public ArrayList<Manager> getManagers() {
        return managers;
    }
}


