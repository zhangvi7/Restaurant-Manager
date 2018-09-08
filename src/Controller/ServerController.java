package Controller;

import Model.Dish;
import Model.MenuItem;
import Model.Order;
import Model.Restaurant;
import Model.Server;
import Model.Table;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ServerController implements Initializable {
    
    @FXML
    private ComboBox tableNumber;
    @FXML
    private ListView seats;
    @FXML
    private ListView preparedOrders;
    @FXML
    private ListView orders;
    @FXML
    private ComboBox menu;
    @FXML
    private ListView addOns;
    @FXML
    private ComboBox serverList;
    @FXML
    private TextArea emptyTables;
    @FXML
    private ComboBox MenuCategory;
    @FXML
    private ComboBox Allergens;
    @FXML
    private ComboBox DietaryRestriction;
    @FXML
    private Button createOrderButton;
    @FXML
    private Button cancelOrderButton;
    @FXML
    private Button deliverOrderButton;
    @FXML
    private MenuButton payBillButton;
    @FXML
    private Button plusButton;
    @FXML
    private Button minusButton;
    @FXML
    private Button redoOrderButton;


    private HashMap<String, Double> adjustment = new HashMap<>();
    
    private Restaurant restaurant = Restaurant.getRestaurantInstance();
    private ObservableList<MenuItem> restaurantMenu = restaurant.getRestaurantMenu().getMenu();
    private ObservableList<Order> pendingOrder = restaurant.getOrderSystem().getPreparedOrders();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Empty-Table set up
        emptyTables.setEditable(false);
        updateEmptyTables();
        
        // Menu Category set up
        MenuCategory.getItems().addAll("Appetizer", "Main", "Dessert", "Side");
        // Allergens set up
        Allergens.getItems().addAll("Nut", "Dairy");
        // Dietary restriction set up
        DietaryRestriction.getItems().addAll("Gluten-free", "Vegetarian");
        
        // Table Number ComboBox set-up
        tableNumber.getItems().addAll(restaurant.getTables());
        tableNumber.setPromptText("Table #");
        
        // Prepared Orders set-up
        preparedOrders.setItems(pendingOrder);
        
        for (Order order : restaurant.getOrderSystem().getPreparedOrders()) {
            preparedOrders.getItems().addAll(order);
        }
        
        // Menu set-up
        
        menu.getItems().addAll(restaurantMenu);
        
        // Add-ons set-up
        
        addOns.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        // ServerList set-up
        
        for (Server server : restaurant.getStaffSystem().getServers()) {
            serverList.getItems().add(server);
        }
        
        // Making sure that the user has all required input before able to click button
        createOrderButton.disableProperty().bind(
            tableNumber.valueProperty().isNull()
                .or(seats.getSelectionModel().selectedItemProperty().isNull())
                .or(serverList.valueProperty().isNull())
                .or(menu.valueProperty().isNull())
        );
        
        cancelOrderButton.disableProperty().bind(
            tableNumber.valueProperty().isNull()
                .or(serverList.valueProperty().isNull())
                .or(orders.getSelectionModel().selectedItemProperty().isNull())
        );
        
        deliverOrderButton.disableProperty().bind(
            tableNumber.valueProperty().isNull()
                .or(serverList.valueProperty().isNull())
                .or(preparedOrders.getSelectionModel().selectedItemProperty().isNull())
        );
        
        payBillButton.disableProperty().bind(
            tableNumber.valueProperty().isNull()
                .or(serverList.valueProperty().isNull())
                .or(orders.getSelectionModel().selectedItemProperty().isNull())
        );

        redoOrderButton.disableProperty().bind(
            tableNumber.valueProperty().isNull()
                .or(serverList.valueProperty().isNull())
                .or(preparedOrders.getSelectionModel().selectedItemProperty().isNull())
        );
        
        plusButton.disableProperty().bind(
            (addOns.getSelectionModel().selectedItemProperty().isNull())
        );
        minusButton.disableProperty().bind(
            (addOns.getSelectionModel().selectedItemProperty().isNull())
        );
        
    }
    
    public void createOrderClicked() {
        Table selectedTable = (Table) tableNumber.getValue();
        Integer selectedTableNumber = selectedTable.getTableNum();
        Integer selectedSeat = (Integer) seats.getSelectionModel().getSelectedItem();
        Server selectedServer = (Server) serverList.getValue();
        MenuItem selectedMenuItem = (MenuItem) menu.getValue();
        Dish newDish;
        if (adjustment.isEmpty()) {
            newDish = new Dish(restaurantMenu, selectedMenuItem.getDishName());
        } else {
            newDish = new Dish(restaurantMenu, selectedMenuItem.getDishName(), adjustment);
        }
        boolean result = selectedServer.createNewOrder(newDish, selectedTableNumber, selectedSeat);
        if (result) {
            updateEmptyTables();
        } else {
            popupWindow("Not enough ingredients to create the order.");
        }
    }
    
    public void cancelOrderClicked() {
        Order selectedOrder = (Order) orders.getSelectionModel().getSelectedItem();
        Server selectedServer = (Server) serverList.getValue();
        selectedServer.cancelOrder(selectedOrder);
        
    }
    
    public void deliverOrderClicked() {
        Order selectedOrder = (Order) preparedOrders.getSelectionModel().getSelectedItem();
        Server selectedServer = (Server) serverList.getValue();
        selectedServer.pickUpOrder(selectedOrder);
        selectedServer.deliverOrder();
    }
    
    
    public void payBillClicked() {
        Table chosenTable = (Table) tableNumber.getValue();
        String billForAll = chosenTable.getCombinedBillString();
        popupWindow(billForAll, chosenTable.getTableNum());
        restaurant.getEmptyTables().add(chosenTable);
        orders.getItems().clear();
        updateEmptyTables();
    }
    
    public void splitBillBySeatClicked() {
        Table chosenTable = (Table) tableNumber.getValue();
        String splitBill = chosenTable.getSplitBillString();
        popupWindow(splitBill, chosenTable.getTableNum());
        restaurant.getEmptyTables().add(chosenTable);
        orders.getItems().clear();
        updateEmptyTables();
    }
    
    public void tableNumberChose() {
        Table chosenTable = (Table) tableNumber.getValue();
        seats.getItems().clear();
        for (int i = 0; i < chosenTable.getCapacity(); i++) {
            seats.getItems().addAll(i);
        }
        orders.setItems(chosenTable.getReceivedOrders());
    }
    
    public void menuItemChose() {
        adjustment = new HashMap<>();
        MenuItem chosenMenuItem = (MenuItem) menu.getValue();
        addOns.getItems().clear();
        if (chosenMenuItem != null) {
            addOns.getItems().addAll(chosenMenuItem.getIngredientNeeded().keySet());
        }
    }
    
    public void clearMenuChoice() {
        addOns.getItems().clear();
        menu.getItems().clear();
        menu.getItems().addAll(restaurantMenu);
    }
    
    public void plusAddOnClicked() {
        ObservableList<String> addingIngredient = addOns.getSelectionModel().getSelectedItems();
        for (String ingredient : addingIngredient) {
            adjustment.put(ingredient, (double) 1);
        }
    }
    
    public void minusAddOnClicked() {
        ObservableList<String> addingIngredient = addOns.getSelectionModel().getSelectedItems();
        for (String ingredient : addingIngredient) {
            adjustment.put(ingredient, (double) -1);
        }
    }
    
    public void updateEmptyTables() {
        emptyTables.clear();
        String result = "";
        for (Table table : restaurant.getEmptyTables()) {
            result = result + table.toString() + "\n";
        }
        emptyTables.setText(result);
    }
    
    public void MenuCategorySelected() {
        menu.getSelectionModel().clearSelection();
        menu.setValue(null);
        menu.getItems().clear();
        String selectedCategory = (String) MenuCategory.getValue();
        for (MenuItem menuItem : restaurant.getRestaurantMenu().getMenu()) {
            if (menuItem.contains(selectedCategory)) {
                menu.getItems().add(menuItem);
            }
        }
    }
    
    public void AllergenSelected() {
        menu.getSelectionModel().clearSelection();
        menu.setValue(null);
        menu.getItems().clear();
        String selectedAllergen = (String) Allergens.getValue();
        for (MenuItem menuItem : restaurant.getRestaurantMenu().getMenu()) {
            if (!menuItem.contains(selectedAllergen)) {
                menu.getItems().add(menuItem);
            }
        }
        
    }
    
    public void DietaryRestrictionSelected() {
        menu.getSelectionModel().clearSelection();
        menu.setValue(null);
        menu.getItems().clear();
        String selectedDiet = (String) DietaryRestriction.getValue();
        for (MenuItem menuItem : restaurant.getRestaurantMenu().getMenu()) {
            if (menuItem.contains(selectedDiet)) {
                menu.getItems().add(menuItem);
            }
        }
    }
    
    public void resetFilterClicked() {
        MenuCategory.valueProperty().set(null);
        Allergens.valueProperty().set(null);
        DietaryRestriction.valueProperty().set(null);
        menu.getItems().addAll(restaurantMenu);
    }
    
    public void redoOrderSelected() throws IOException {
        Order selectedOrder = (Order) preparedOrders.getSelectionModel().getSelectedItem();
        Server selectedServer = (Server) serverList.getValue();
        selectedServer.cancelOrder(selectedOrder);
        if (selectedServer.checkValidIngredients(selectedOrder)) {
            boolean result = selectedServer.createNewOrder(selectedOrder.getDish(), selectedOrder.getTableNum(),
                    selectedOrder.getSeatNum());
            restaurant.getOrderSystem().getPreparedOrders().remove(selectedOrder);
            if (result) {
                updateEmptyTables();
            }
        } else {
            popupWindow("Not enough ingredients to create the order");
        }
    }
    
    public void popupWindow(String text, int tableNum) {
        Stage window = new Stage();
        
        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Receipt");
        window.setMinWidth(250);
        
        Label label = new Label();
        label.setText(text);
        Button closeButton = new Button("Okay");
        closeButton.setOnAction(e -> {
            restaurant.getRevenueSystem().receivePayment(tableNum);
            window.close();
        });
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        
        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
    
    public void popupWindow(String text) {
        Stage window = new Stage();
        
        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Receipt");
        window.setMinWidth(250);
        
        Label label = new Label();
        label.setText(text);
        Button closeButton = new Button("Okay");
        closeButton.setOnAction(e -> {
            window.close();
        });
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        
        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
    
}


