package edu.wpi.veganvampires;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
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
      URL xmlUrl = getClass().getClassLoader().getResource("FXML/home.fxml");
      loader.setLocation(xmlUrl);
      Parent root = loader.load();

      primaryStage.setScene(new Scene(root));
      primaryStage.show();
      this.main();
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
    }
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }

  public static void main() {

    System.out.println("-------Embedded Apache Derby Connection Testing --------");
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
    } catch (ClassNotFoundException e) {
      System.out.println("Apache Derby Driver not found. Add the classpath to your module.");
      System.out.println("For IntelliJ do the following:");
      System.out.println("File | Project Structure, Modules, Dependency tab");
      System.out.println("Add by clicking on the green plus icon on the right of the window");
      System.out.println(
          "Select JARs or directories. Go to the folder where the database JAR is located");
      System.out.println("Click OK, now you can compile your program and run it.");
      e.printStackTrace();
      return;
    }

    System.out.println("Apache Derby driver registered!");
    Connection connection;

    try {
      // substitute your database name for myDB
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();
      ResultSet set = meta.getTables(null, null, "LOCATIONS", new String[] {"TABLE"});
      if (!set.next()) {
        exampleStatement.execute(
            "CREATE TABLE Locations(nodeID int, xCoord int, yCoord int, floor char(10), building char(20), nodeType char(10), longName char(60), shortName char(30))");
      }
      ArrayList<Location> locList = new ArrayList<>();
      Scanner scanner = new Scanner(System.in);
      boolean loop = true;
      int state = 0;
      while (loop) {
        switch (state) {
          case 1:
            Statement stmt = connection.createStatement();
            String query = "select * from Locations";
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
              System.out.println("No data available");
            }
            while (rs.next()) {
              System.out.println("ID: " + rs.getString("nodeID"));
              System.out.println("Coordinates: " + rs.getInt("xCoord") + " " + rs.getInt("yCoord"));
              System.out.println("Floor: " + rs.getInt("floor"));
              System.out.println("Building: " + rs.getString("building"));
              System.out.println("nodeType: " + rs.getString("nodeType"));
              System.out.println("longName: " + rs.getString("longName"));
              System.out.println("shortName: " + rs.getString("shortName"));
              System.out.println(" ");
            }

            break;
          case 2:
            System.out.println("Location ID?");
            String ID1 = scanner.next();
            System.out.println("What equipment is at this location?");
            String equip = scanner.next();
            Statement newStatement1 = connection.createStatement();
            newStatement1.execute(
                "UPDATE Locations SET Room_Num = ID3, Contents = equip) WHERE Room_Num = ID3");
            Location newLoc = new Location(ID1);
            for (Location location : locList) {
              if (location.nodeID == ID1) location = newLoc;
            }
            break;
          case 3:
            System.out.println("New location ID?");
            String ID2 = scanner.next();
            Location loc = new Location(ID2);
            locList.add(loc);
            Statement newStatement2 = connection.createStatement();
            newStatement2.execute("INSERT INTO Locations VALUES(loc.nodeID, '')");
            break;
          case 4:
            System.out.println("Location ID?");
            String ID3 = scanner.next();
            Statement newStatement3 = connection.createStatement();
            newStatement3.execute("DELETE FROM Locations WHERE Room_Num = ID3");
            locList.removeIf(location -> location.nodeID == ID3);
            break;
          case 5:
            Vdb newBuffer = new Vdb();
            newBuffer.CreateDB();
            break;
          case 6:
            loop = false;
            break;
          default:
            System.out.println(
                "1-Location Information\n2-Change Floor and Type\n3-Enter Location\n4-Delete Location\n5-Save Locations to CSV File\n6-Exit Program");
            // state = scanner.nextInt();
        }
        state = scanner.nextInt();
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      return;
    } catch (Exception e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }
    System.out.println("Apache Derby connection established!");
    for (Location location : Vdb.locations) {
      System.out.println("ID: " + location.nodeID);
    }
  }
}
