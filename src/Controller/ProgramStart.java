package Controller;

import Model.Restaurant;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The starter GUI controller that will initiate all the tabs of the program.
 */
public class ProgramStart extends Application implements Initializable{

  @FXML
  private TabPane tabPane;

  /**
   * Initializes the program.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb){
    tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
  }

  /**
   * Setup the window and loads all the tabs.
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    // Scene Set-up
    Scene scene = new Scene(new Pane());
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/ProgramStart.fxml"));
    scene.setRoot(loader.load());

    primaryStage.setScene(scene);
    primaryStage.setTitle("The Ultimate Restaurant Program");
    primaryStage.show();

    primaryStage.setOnCloseRequest(e-> closeProgram());
  }

  /**
   * Closes the program and saves necessary files.
   */
  public void closeProgram(){
    Restaurant.getRestaurantInstance().SaveFiles();

  }

  /**
   * Launches program.
   */
  public static void main(String[] args) {
    launch(args);
  }




}