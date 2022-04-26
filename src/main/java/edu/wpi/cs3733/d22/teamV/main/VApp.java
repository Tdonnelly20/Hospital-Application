package edu.wpi.cs3733.d22.teamV.main;

import edu.wpi.cs3733.d22.teamV.controllers.Controller;
import java.io.IOException;
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
  public static String currentPath;

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(
          Objects.requireNonNull(getClass().getClassLoader().getResource("FXML/Login.fxml")));
      Parent root = loader.load();

      Controller controller = loader.getController();
      controller.init();

      primaryStage.setScene(new Scene(root));
      primaryStage.show();
      currentPath = returnPath();
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String returnPath() {
    // TeamVeganVampires\src\main\resources\edu\wpi\cs3733\d22\teamV
    String currentPath = System.getProperty("user.dir");
    if (currentPath.contains("teamV") || currentPath.contains("TeamVeganVampires")) {
      int position = currentPath.indexOf("teamV") + 65;
      if (currentPath.length() > position) {
        currentPath = currentPath.substring(0, position);
      }
      currentPath += "/src/main/resources/edu/wpi/cs3733/d22/teamV";
      System.out.println(currentPath);
    }
    return currentPath;
  }

  @Override
  public void stop() throws Exception {
    log.info("Shutting Down");
  }
}
