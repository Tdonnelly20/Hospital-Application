package edu.wpi.cs3733.d22.teamV.interfaces;

import javafx.fxml.FXML;

public interface RequestInterface {
  @FXML
  void validateButton();

  @FXML
  void updateTreeTable();

  @FXML
  void resetForm();

  @FXML
  void sendRequest();
}
