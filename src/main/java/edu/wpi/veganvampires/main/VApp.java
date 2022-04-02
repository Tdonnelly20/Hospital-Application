package edu.wpi.veganvampires.main;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VApp extends Application {

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(
          Objects.requireNonNull(getClass().getClassLoader().getResource("FXML/home.fxml")));
      Parent root = loader.load();
      primaryStage.setScene(new Scene(root));
      primaryStage.show();
      main();
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void stop() throws Exception {
    log.info("Shutting Down");
  }

  public static void main() throws Exception {

    Vdb.createAllDB();
  }
}
