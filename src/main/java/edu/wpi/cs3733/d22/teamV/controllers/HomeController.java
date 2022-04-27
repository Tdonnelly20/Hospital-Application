package edu.wpi.cs3733.d22.teamV.controllers;

import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomeController extends RequestController {
  @FXML private TreeTableView<ServiceRequest> requestTable;
  @FXML private TreeTableColumn<ServiceRequest, Integer> serviceIDCol;
  @FXML private TreeTableColumn<ServiceRequest, String> typeCol;
  @FXML private TreeTableColumn<ServiceRequest, String> floorCol;
  @FXML private TreeTableColumn<ServiceRequest, Integer> nodeIDCol;
  @FXML private TreeTableColumn<ServiceRequest, Integer> employeeIDCol;
  @FXML private TreeTableColumn<ServiceRequest, Integer> patientIDCol;
  @FXML private TreeTableColumn<ServiceRequest, Integer> statusCol;
  @FXML private TreeTableColumn<ServiceRequest, Integer> timeCol;
  @FXML private Pane tablePane;
  @FXML private Label homeClock;

  @FXML private Pane homePane;
  @FXML private ImageView homeImage;
  @FXML private Group homeGroup;

  @FXML private TextField searchBar;

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @Override
  public void init() {
    setTitleText("Home");
    fillTopPaneAPI();

    homePane
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = homePane.getWidth();
                double h = homePane.getHeight();

                if (w / 16 * 9 > h) {
                  homeImage.setFitWidth(w);
                  homeImage.setFitHeight(w / 16 * 9);
                }

                if (h / 9 * 16 > w) {
                  homeImage.setFitWidth(h / 9 * 16);
                  homeImage.setFitHeight(h);
                }

                homeGroup.setLayoutX(w / 2 - 603);
              }
            });

    homePane
        .heightProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = homePane.getWidth();
                double h = homePane.getHeight();

                if (w / 16 * 9 > h) {
                  homeImage.setFitWidth(w);
                  homeImage.setFitHeight(w / 16 * 9);
                }

                if (h / 9 * 16 > w) {
                  homeImage.setFitWidth(h / 9 * 16);
                  homeImage.setFitHeight(h);
                }

                homeGroup.setLayoutY(h / 2 - 293);
              }
            });

    tablePane
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = tablePane.getWidth();
                requestTable.setPrefWidth(w - 30);
                searchBar.setPrefWidth(w - 30);
                setColumnSizes(w);
              }
            });

    tablePane
        .heightProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double h = tablePane.getHeight();
                requestTable.setPrefHeight(h - 30);
              }
            });
    initializeClockHome();
  }

  @FXML
  void updateTreeTable() {
    serviceIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("serviceID"));
    typeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("type"));
    nodeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("nodeID"));
    floorCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("floorName"));
    employeeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("employeeID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientID"));
    statusCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("status"));
    timeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("timeString"));

    ArrayList<ServiceRequest> serviceRequests =
        (ArrayList<ServiceRequest>) Vdb.requestSystem.getEveryServiceRequest();

    ArrayList<TreeItem<ServiceRequest>> treeItems = new ArrayList<>();

    if (serviceRequests.isEmpty()) {
      requestTable.setRoot(null);
    } else {
      for (ServiceRequest request : serviceRequests) {
        TreeItem<ServiceRequest> item = new TreeItem<>(request);
        treeItems.add(item);
      }
      requestTable.setShowRoot(false);
      TreeItem<ServiceRequest> root = new TreeItem<>(serviceRequests.get(0));
      requestTable.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  @Override
  void resetForm() {}

  @Override
  void validateButton() {}

  void setColumnSizes(double w) {
    setColumnSize(serviceIDCol, (w - 30) / 12);
    setColumnSize(typeCol, (w - 30) / 6);
    setColumnSize(floorCol, (w - 30) / 12);
    setColumnSize(nodeIDCol, (w - 30) / 6);
    setColumnSize(employeeIDCol, (w - 30) / 12);
    setColumnSize(patientIDCol, (w - 30) / 12);
    setColumnSize(statusCol, (w - 30) / 6);
    setColumnSize(timeCol, (w - 30) / 6);
  }

  @FXML
  public void initializeClockHome() {

    Timeline clock =
        new Timeline(
            new KeyFrame(
                Duration.ZERO,
                e ->
                    homeClock.setText(
                        LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))),
            new KeyFrame(Duration.seconds(1)));
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();
  }
}
