package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ReligiousRequestController extends MapController {
  @FXML private TextField Status;
  @FXML private TextField userID;
  @FXML private TextField patientID;
  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private JFXComboBox<Object> dropDown;
  @FXML private Button sendRequest;
  @FXML private AnchorPane CheckBoxes;
  @FXML private CheckBox Christian;
  @FXML private CheckBox Jewish;
  @FXML private CheckBox Protestant;
  @FXML private CheckBox Islam;
  @FXML private CheckBox Muslim;
  @FXML private CheckBox Buddhist;
  @FXML private CheckBox Hindu;
  @FXML private CheckBox Other;
  @FXML private TextField specialRequests;

  private static class SingletonHelper {
    private static final ReligiousRequestController controller = new ReligiousRequestController();
  }

  public static ReligiousRequestController getController() {
    return ReligiousRequestController.SingletonHelper.controller;
  }

  @Override
  public void init() {
    mapSetUp();
    filterCheckBox.getCheckModel().check("Religious Requests");
  }

  @FXML
  private void resetForm() {
    Status.setText("Status: Blank");
    userID.setText("");
    patientID.setText("");
    firstName.setText("");
    lastName.setText("");
    specialRequests.setText("");
    Christian.setSelected(false);
    Jewish.setSelected(false);
    Protestant.setSelected(false);
    Islam.setSelected(false);
    Muslim.setSelected(false);
    Buddhist.setSelected(false);
    Hindu.setSelected(false);
    Other.setSelected(false);
    sendRequest.setDisable(true);
  }

  // Checks to see if the user can submit info
  @FXML
  private void validateButton() {
    if (!(userID.getText().isEmpty())
        && !(userID.getText().isEmpty())
        && !(patientID.getText().isEmpty())
        && !(firstName.getText().isEmpty())
        && !(lastName.getText().isEmpty())
        && !((Christian.selectedProperty()) == null)
        && !(Jewish.selectedProperty() == null)
        && !(Protestant.selectedProperty() == null)
        && !(Islam.selectedProperty() == null)
        && !(Muslim.selectedProperty() == null)
        && !(Buddhist.selectedProperty() == null)
        && !(Hindu.selectedProperty() == null)
        && !(Other.selectedProperty() == null)
        && !(specialRequests.getText().isEmpty())) {
      // Information verification and submission needed
      sendRequest.setDisable(false);
      Status.setText("Status: Done");
    } else if (!(userID.getText().isEmpty())
        || !(userID.getText().isEmpty())
        || !(patientID.getText().isEmpty())
        || !(firstName.getText().isEmpty())
        || !(lastName.getText().isEmpty())
        || !((Christian.selectedProperty()) == null)
        || !(Jewish.selectedProperty() == null)
        || !(Protestant.selectedProperty() == null)
        || !(Islam.selectedProperty() == null)
        || !(Muslim.selectedProperty() == null)
        || !(Buddhist.selectedProperty() == null)
        || !(Hindu.selectedProperty() == null)
        || !(Other.selectedProperty() == null)
        || !(specialRequests.getText().isEmpty())) {
      Status.setText("Status: Processing");
    } else {
      Status.setText("Status: Blank");
      sendRequest.setDisable(true);
    }
  }

  @Override
  public void start(Stage primaryStage) {}
}
