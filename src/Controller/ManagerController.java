package Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import Model.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.Observer;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * The Controller for the Manager GUI. It governs all of the interaction with the back-end of the program
 * and act according to user's action.
 */
public class ManagerController implements Observer,Initializable {
    @FXML private TextArea eventText;
    @FXML private TextField idField;
    @FXML private TextField delRequestField;
    @FXML private TextField ingredient;
    @FXML private TextField quantity;
    @FXML private Button editEmailButton;
    @FXML private Button deleteOrderButton;


    private Restaurant restaurant = Restaurant.getRestaurantInstance();
    private Inventory inventory = restaurant.getInventory();
    private Boolean loggedIn = false;
    private Manager user;

    /**
     * Initialize the Manager GUI tab.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inventory.addObserver(this);
        eventText.setEditable(false);

        editEmailButton.disableProperty().bind(
            ingredient.textProperty().isEmpty()
                .or( quantity.textProperty().isEmpty() )
        );

        deleteOrderButton.disableProperty().bind(
            delRequestField.textProperty().isEmpty()
        );



    }

    /**
     * Check the log-in detail of the Manager accessing the program.
     */
    public void checkLogin(){
        for (Manager manager: restaurant.getStaffSystem().getManagers()){
            if (Integer.toString(manager.id).equals(idField.getText())){
                this.loggedIn = true;
                this.user = manager;
                eventText.appendText("Successfully logged in." + "\n");
                break;
            }
        }
        if (!loggedIn){
            eventText.appendText("ID does not exist." + "\n");
        }

    }

    /**
     * Log out from the system and limit the action that the user can perform in this interface.
     */
    public void logout(){
        if (loggedIn) {
            this.loggedIn = false;
            eventText.appendText("You have successfully logged out" + "\n");
        }
    }

    /**
     * Called when the check inventory button is called by the user.
     * It displays the Inventory of the restaurant.
     */
    public void checkInventoryClicked(){
        if (loggedIn) {
            eventText.appendText(inventory.toString().replace(", ", "\n"));
        } else {
            eventText.appendText("Can only perform this function if logged in\n");
        }
    }


    /**
     * Triggers when the write email button is clicked by the user.
     * It takes the request of ingredients and places the order.
     */
    public void writeEmailClicked() throws IOException {
        if (loggedIn) {
            user.writeIncomingOrders();
            eventText.appendText("An E-mail has been sent requesting ingredients" + "\n");
        } else {
            eventText.appendText("Can only perform this function if logged in" + "\n");
        }
    }

    /**
     * Trigger when the delete request button is clicked.
     * It will delete all of the request of ingredient that is being requested by the auto request system.
     */
    public void deleteRequestsClicked(){
        if (loggedIn) {
            inventory.deleteRequest(delRequestField.getText());
            eventText.setText("A request for " + delRequestField.getText() + " has been deleted\n");
        } else {
            eventText.appendText("Can only perform this function if logged in" + "\n");
        }
    }

    /**
     * Print out the current revenue situation of the Restaurant.
     */
    public void checkRevenueClicked() throws IOException{
        if (loggedIn)
        {
            Parent tableViewParent = FXMLLoader.load(getClass().getResource("/View/Revenue.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);

            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setScene(tableViewScene);
            window.showAndWait();
        } else {
            eventText.appendText("Can only perform this function if logged in" + "\n");
        }
    }

    /**
     * Notifies all observer when ingredient drops below threshold.
     */
    @Override
    public void update(java.util.Observable o, Object arg){
        eventText.appendText("A request for more " + arg + " has been written, confirm by sending an email." + "\n");
    }

    /**
     * Triggers when the edit Email button is clicked.
     * It will edit the request of ingredients.
     */
    public void editEmailClicked(){
      if (loggedIn) {
        String inputIngredient = ingredient.getText().trim();

        String inputQuantity = quantity.getText().trim();


        String oldString = String.format("%s,%s", inputIngredient, "20");
        String newString = String.format("%s,%s", inputIngredient, inputQuantity);

        try {
          user.writeIncomingOrders();
          user.modifyDelivery(oldString, newString);
          eventText.appendText("A request for " + inputIngredient + " has been modified to " +
              inputQuantity) ;
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        eventText.appendText("Can only perform this function if logged in" + "\n");
      }
    }
}
