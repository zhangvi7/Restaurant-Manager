package Controller;

import Model.Chef;
import Model.Order;
import Model.Restaurant;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The <code>ChefController</code> class implements <code>Initializable</code>is used to handle all
 * of the event and actions triggered by the Chef.fxml GUI.
 */
public class ChefController implements Initializable {

  // The list of unseen Orders that appears in the Chef's screen.
  @FXML private ListView unseenOrders;
  // The list of seen but unprepared Orders that appears in Chef's screen.
  @FXML private ListView unpreparedOrders;
  // The selection of all of the Chef in the Restaurant
  @FXML private ComboBox chefList;
  // The Button that will trigger prepare Order action
  @FXML private Button preparedOrderButton;
  // The Button that will trigger seen Order action
  @FXML private Button seenOrderButton;

  Restaurant restaurant = Restaurant.getRestaurantInstance();
  ObservableList<Order> pendingSeenOrders = restaurant.getOrderSystem().getUnseenOrders();
  ObservableList<Order> pendingPreparedOrders = restaurant.getOrderSystem().getUnpreparedOrders();

  /**
   * Initialize all of the fields in the Chef tab.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb){
    // Chef List set-up
    chefList.getItems().addAll(restaurant.getStaffSystem().getChefs());

    // Received Orders list set-up
    unseenOrders.setItems(pendingSeenOrders);

    // Seen Orders but unprepared
    unpreparedOrders.setItems(pendingPreparedOrders);

    seenOrderButton.disableProperty().bind(
        chefList.valueProperty().isNull()
            .or( unseenOrders.getSelectionModel().selectedItemProperty().isNull() )
    );

    preparedOrderButton.disableProperty().bind(
        chefList.valueProperty().isNull()
    );

  }

  /**
   * Called when Seen Order button is clicked in the GUI.
   * It will get the selected Chef to start preparing the Order.
   */
  public void seenOrderClicked(){
    Chef selectedChef = (Chef) chefList.getValue();
    Order selectedOrder = (Order) unseenOrders.getSelectionModel().getSelectedItem();
    if (selectedChef.getCurrentOrder() == null){
      selectedChef.seeOrder(selectedOrder);
    }
    else{
      System.out.println(selectedChef.getCurrentOrder());
      popupwindow();
    }
  }

  /**
   * Called when Prepare Order button is clicked in the GUI.
   * It will get the selected Chef to indicate that the Order has been done.
   */
  public void preparedOrderClicked(){
    Chef selectedChef = (Chef) chefList.getValue();
    selectedChef.confirmPrepared();
  }

  /**
   * Generates a pop up window in the GUI to alert the user on certain condition.
   */
  private void popupwindow(){
    Stage window = new Stage();

    //Block events to other windows
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Warning");
    window.setMinWidth(250);

    Label label = new Label();
    label.setText("Please finish your current job first.");
    Button closeButton = new Button("Close this window");
    closeButton.setOnAction(e -> window.close());

    VBox layout = new VBox(10);
    layout.getChildren().addAll(label, closeButton);
    layout.setAlignment(javafx.geometry.Pos.CENTER);

    //Display window and wait for it to be closed before returning
    Scene scene = new Scene(layout);
    window.setScene(scene);
    window.showAndWait();
  }



}
