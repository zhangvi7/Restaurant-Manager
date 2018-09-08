package Controller;

import Model.EventLogger;
import Model.Restaurant;
import Model.Staff;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

/**
 * The ReceiverController is the class that acts as a bridge between the Receiver GUi and the
 * program's backend.
 */
public class ReceiverController implements Initializable{

  @FXML private ComboBox staffList;
  @FXML private ListView inventory;
  @FXML private TextField restockQuantity;
  @FXML private Button immediateRestockButton;
  @FXML private Button restockRequestButton;

  Restaurant restaurant = Restaurant.getRestaurantInstance();

  /**
   * This method will be ran when the GUi is initiated and will set up all of the fields in the GUI.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb){
    // Staff List set-up
    staffList.getItems().addAll(restaurant.getStaffSystem().getAllStaffs());

    // Inventory list set-up
    for (String ingredient : restaurant.getInventory().getStock().keySet()){
      inventory.getItems().addAll(ingredient);
    }

    immediateRestockButton.disableProperty().bind(
        staffList.valueProperty().isNull()
            .or( inventory.getSelectionModel().selectedItemProperty().isNull() )
            .or( restockQuantity.textProperty().isEmpty())
    );

    restockRequestButton.disableProperty().bind(
        staffList.valueProperty().isNull()
    );

  }

  /**
   * The method that will be called when the restock button is clicked.
   * It will immediately restock the indicated ingredient and specified quantity
   * used in a scenario like when the Staff walked to the nearest grocery store to restock.
   */
  public void restockClicked()throws IOException {
    Staff selectedStaff = (Staff) staffList.getValue();
    String itemSelected = (String) inventory.getSelectionModel().getSelectedItem();
    Integer quantity = Integer.parseInt(restockQuantity.getText());
    selectedStaff.restockIngredients(itemSelected, quantity);
    EventLogger.log(selectedStaff.toString() +" has restocked " + quantity + " " + itemSelected);

  }

  /**
   * The method that will be called when the restock incoming button is clicked.
   * It will restock ingredients that is in the incomingOrders.txt
   */
  public void restockRequestClicked(){
    Staff selectedStaff = (Staff) staffList.getValue();
    try {
      selectedStaff.receiveIncomingOrders();
    }
    catch (IOException e){
      e.printStackTrace();
    }
  }


}
