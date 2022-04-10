package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public abstract class RequestController extends MapController {

    //Main Header Gridpane stuff
    @FXML private GridPane headerPane = new GridPane();
    private ColumnConstraints col1Constraints = new ColumnConstraints();
    private ColumnConstraints col2Constraints = new ColumnConstraints();
    private ColumnConstraints col3Constraints = new ColumnConstraints();
    private ColumnConstraints col4Constraints = new ColumnConstraints();
    private RowConstraints rowConstraints = new RowConstraints();
    @FXML private ImageView logo = new ImageView(new Image("icon.png"));
    @FXML private Label title = new Label();

    @FXML private GridPane buttonBox = new GridPane();
    @FXML private Button closeButton = new Button();
    @FXML private Button homeButton = new Button();
    @FXML private Button backButton = new Button();


    @FXML protected VBox sideBar = new VBox();

    @FXML protected VBox tableBox = new VBox();

    void fillTopPane(){
        headerPane.add(logo, 0,0);
        headerPane.add(title, 1,0);
        headerPane.add(logo, 3,0);

        col1Constraints.setHalignment(HPos.CENTER);
        col2Constraints.setHalignment(HPos.CENTER);
        col3Constraints.setHalignment(HPos.CENTER);
        col4Constraints.setHalignment(HPos.CENTER);
        col1Constraints.setMinWidth(150);
        col2Constraints.setMinWidth(500);
        col4Constraints.setMinWidth(450);
        col1Constraints.setMaxWidth(150);
        col2Constraints.setMaxWidth(500);
        col4Constraints.setMaxWidth(450);
        col1Constraints.setPrefWidth(150);
        col2Constraints.setPrefWidth(500);
        col4Constraints.setPrefWidth(450);

        rowConstraints.setMinHeight(150);
        rowConstraints.setMaxHeight(150);
        rowConstraints.setPrefHeight(150);



    }

    void setTitleText(String str){
        title.setText(str);
    }

    abstract void updateTreeTable();
    abstract void resetForm();
    abstract void validateButton();
    abstract void fillSideBar();
    abstract void fillTableBox();

}
