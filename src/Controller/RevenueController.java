package Controller;
import Model.*;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * The controller for the Revenue gui, displays the revenue earned and taxes to be paid for different timeframes.
 *
 * @author group_0220
 */
public class RevenueController implements Initializable {
    @FXML private Label dayRevenueLabel;
    @FXML private Label weekRevenueLabel;
    @FXML private Label monthRevenueLabel;
    @FXML private Label yearRevenueLabel;
    @FXML private Label allRevenueLabel;
    @FXML private Label dayTaxesLabel;
    @FXML private Label weekTaxesLabel;
    @FXML private Label monthTaxesLabel;
    @FXML private Label yearTaxesLabel;
    @FXML private Label allTaxesLabel;

    private ZonedDateTime currentTime;
    private Restaurant restaurant = Restaurant.getRestaurantInstance();

    /**
     * Initializes the revenue gui, and displays the revenue and taxes for different timeframes.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        currentTime = ZonedDateTime.now(ZoneId.of("America/Toronto"));
        double revenue = 0;
        double taxes = 0;

        for (Bill bill : restaurant.getRevenueSystem().getAllBills()) {
            if (bill.paymentTime.isAfter(currentTime.minusHours(24))) {
                revenue += bill.amount;
                taxes += bill.amount * 0.13;
            }
        }
        dayRevenueLabel.setText("$" + revenue);
        dayTaxesLabel.setText("$" + taxes);
        revenue = 0;
        taxes = 0;

        for (Bill bill:restaurant.getRevenueSystem().getAllBills()){
            if (bill.paymentTime.isAfter(currentTime.minusDays(7))){
                revenue += bill.amount;
                taxes += bill.amount * 0.13;
            }
        }
        weekRevenueLabel.setText("$" + revenue);
        weekTaxesLabel.setText("$" + taxes);
        revenue = 0;
        taxes = 0;

        for (Bill bill:restaurant.getRevenueSystem().getAllBills()){
            if (bill.paymentTime.isAfter(currentTime.minusDays(30))){
                revenue += bill.amount;
                taxes += bill.amount * 0.13;
            }
        }
        monthRevenueLabel.setText("$" + revenue);
        monthTaxesLabel.setText("$" + taxes);
        revenue = 0;
        taxes = 0;

        for (Bill bill:restaurant.getRevenueSystem().getAllBills()) {
            if (bill.paymentTime.getYear() == currentTime.getYear()) {
                revenue += bill.amount;
                taxes += bill.amount * 0.13;
            }
        }
        yearRevenueLabel.setText("$" + revenue);
        yearTaxesLabel.setText("$" + taxes);
        revenue = 0;
        taxes = 0;

        for (Bill bill:restaurant.getRevenueSystem().getAllBills()) {
            revenue += bill.amount;
            taxes += bill.amount * 0.13;
        }
        allRevenueLabel.setText("$" + revenue);
        allTaxesLabel.setText("$" + taxes);
        revenue = 0;
        taxes = 0;
    }
}