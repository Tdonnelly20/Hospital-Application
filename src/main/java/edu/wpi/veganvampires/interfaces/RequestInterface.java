package edu.wpi.veganvampires.interfaces;

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
