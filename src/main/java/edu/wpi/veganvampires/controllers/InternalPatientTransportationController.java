package edu.wpi.veganvampires.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.veganvampires.dao.InternalPatientTransportationDao;
import edu.wpi.veganvampires.interfaces.RequestInterface;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.InternalPatientTransportation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class InternalPatientTransportationController extends Controller implements RequestInterface{

    @FXML
    private TreeTableView<InternalPatientTransportation> internalPatientTransportationTable;

    @FXML private TreeTableColumn<InternalPatientTransportation, Integer> hospitalIDCol;
    @FXML private TreeTableColumn<InternalPatientTransportation, Integer> patientIDCol;
    @FXML private TreeTableColumn<InternalPatientTransportation, String> firstNameCol;
    @FXML private TreeTableColumn<InternalPatientTransportation, String> lastNameCol;
    @FXML private TreeTableColumn<InternalPatientTransportation, String> roomNumberCol;
    @FXML private TreeTableColumn<InternalPatientTransportation, String> otherInfoCol;

    @FXML private TextField patientID;
    @FXML private TextField hospitalID;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField roomNum;
    @FXML private Button sendRequest;
    @FXML private TextArea requestDetails;
    @FXML private Label statusLabel;

    //private static InternalPatientTransportationDao internalPatientTransportationDao = Vdb.internalPatientTransportationDao;

    /** Update the table with values from fields and the DB */
    @Override
    public void updateTreeTable() {
        // Set our cell values based on the MedicineDelivery Class, the Strings represent the actual
        // name of the variable we are adding to a specific column
        hospitalIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("hospitalID"));
        patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
        firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientFirstName"));
        lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientLastName"));
        roomNumberCol.setCellValueFactory(new TreeItemPropertyValueFactory("roomNumber"));
        otherInfoCol.setCellValueFactory(new TreeItemPropertyValueFactory("requestDetails"));

        // Get the current list of transportations from the DAO
        ArrayList<InternalPatientTransportation> currInternalPatientTransportations =
                (ArrayList<InternalPatientTransportation>) internalPatientTransportationDao.getAllInternalPatientTransportations();


        // Create list for tree items
        ArrayList<TreeItem> treeItems = new ArrayList<>();

        // make sure the list isn't empty
        if (!currInternalPatientTransportations.isEmpty()) {

            // for each loop cycling through each patient transportation request currently entered into the system
            for (InternalPatientTransportation transport : currInternalPatientTransportations) {
                TreeItem<InternalPatientTransportation> item = new TreeItem(transport);
                treeItems.add(item);
            }
            // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
            // we get the standard table functionality
            internalPatientTransportationTable.setShowRoot(false);
            // Root is just the first entry in our list
            TreeItem root = new TreeItem(InternalPatientTransportationDao.getAllInternalPatientTransportations().get(0));
            // Set the root in the table
            internalPatientTransportationTable.setRoot(root);
            // Set the rest of the tree items to the root, including the one we set as the root
            root.getChildren().addAll(treeItems);
        }
    }

    /** Determine whether or not all fields have been filled out, so we can submit the info */
    @FXML
    public void validateButton() {

        try {
            if ((hospitalID.getText().equals("")
                    && patientID.getText().equals("")
                    && firstName.getText().equals("")
                    && lastName.getText().equals("")
                    && roomNum.getText().equals(""))) {
                sendRequest.setDisable(true);
                statusLabel.setText("Status: Blank");
                statusLabel.setTextFill(Color.web("Black"));
            } else if ((hospitalID.getText().equals("")
                    || patientID.getText().equals("")
                    || firstName.getText().equals("")
                    || lastName.getText().equals("")
                    || roomNum.getText().equals(""))) {
                sendRequest.setDisable(true);
                statusLabel.setText("Status: Processing");
                statusLabel.setTextFill(Color.web("Black"));
            } else {
                sendRequest.setDisable(false);
            }
        } catch (Exception e) {
            sendRequest.setDisable(true);
        }
    }

    /** Determines if a medical delivery request is valid, and sends it to the Dao */
    @Override
    public void sendRequest() {
        // If any field is left blank, (except for request details) throw an error

        // Make sure the patient ID is an integer
        if (!isInteger(patientID.getText()) || !isInteger(hospitalID.getText())) {
            statusLabel.setText("Status: Failed. Patient/Hospital ID must be a number!");
            statusLabel.setTextFill(Color.web("Red"));

            // If all conditions pass, create the request
        } else {

            // Send the request to the Dao pattern
            internalPatientTransportationDao.addInternalPatientTransportation(
                    firstName.getText(),
                    lastName.getText(),
                    roomNum.getText(),
                    Integer.parseInt(patientID.getText()),
                    Integer.parseInt(hospitalID.getText()),
                    requestDetails.getText());

            // For testing purposes
            System.out.println(
                    "\nHospital ID: "
                            + hospitalID.getText()
                            + "\nPatient ID: "
                            + patientID.getText()
                            + "\nRoom #: "
                            + roomNum.getText()
                            + "\nName: "
                            + firstName.getText()
                            + " "
                            + lastName.getText()
                            + "\n\nRequest Details: "
                            + requestDetails.getText());

            resetForm(); // Set all fields to blank for another entry
            updateTreeTable();
        }
    }

    /** Sets all the fields to their default value for another entry */
    @FXML
    public void resetForm() {
        patientID.setText("");
        hospitalID.setText("");
        firstName.setText("");
        lastName.setText("");
        roomNum.setText("");
        requestDetails.setText("");
        sendRequest.setDisable(true);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {}
}
