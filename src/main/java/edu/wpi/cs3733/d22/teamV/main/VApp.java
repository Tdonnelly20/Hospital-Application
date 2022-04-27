package edu.wpi.cs3733.d22.teamV.main;

import edu.wpi.cs3733.d22.teamV.controllers.Controller;
import edu.wpi.cs3733.d22.teamV.face.Camera;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
      Vdb vdb = new Vdb();
      vdb.createAllDB();

      copy(
          getClass()
              .getResourceAsStream(
                  new File("") + "/edu/wpi/cs3733/d22/teamV/xml/haarcascade_frontalface_alt.xml"),
          new File("").getAbsolutePath() + "/haarcascade_frontalface_alt.xml");
      copy(
          getClass()
              .getResourceAsStream(
                  new File("")
                      + "/edu/wpi/cs3733/d22/teamV/faces/pytorch_models/facenet/facenet.pt"),
          new File("").getAbsolutePath() + "/facenet/facenet.pt");

    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean copy(InputStream source, String destination) {
    boolean succeess = true;

    try {
      Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ex) {
      ex.printStackTrace();
      succeess = false;
    }

    return succeess;
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
    Camera.stopAcquisition();
    log.info("Shutting Down");
  }
}
