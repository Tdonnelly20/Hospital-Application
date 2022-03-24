package edu.wpi.veganvampires;
import java.sql.*;
import java.util.Scanner;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) {}

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
  public static void main(String[] args) {
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
      connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
      Statement exampleStatement = connection.createStatement();
      exampleStatement.execute("CREATE TABLE Locations(nodeID int, xCoord int, yCoord int, floor char(10), building char(20), nodeType char(10), longName char(60), shortName char(30))");
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      return;
    }
    System.out.println("Apache Derby connection established!");

    Scanner scanner = new Scanner(System.in);
    int state = 0;
    boolean flag = true;
    while(flag) {
      switch (state) {
        case 1:
          try
          {
            Statement stmt = connection.createStatement();
            String query = "select * from Locations";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next())
            {
              System.out.println("ID: " + rs.getString("nodeID"));
              System.out.println("Coordinates: " + rs.getInt("xCoord") + " " + rs.getInt("yCoord"));
              System.out.println("Floor: " + rs.getInt("floor"));
              System.out.println("Building: " + rs.getString("building"));
              System.out.println("nodeType: " + rs.getString("nodeType"));
              System.out.println("longName: " + rs.getString("longName"));
              System.out.println("shortName: " + rs.getString("shortName"));
              System.out.println(" ");
            }
          }
          catch(Exception e)
          {
            System.out.println("Exception occurred");
          }


          break;
        case 2:
          break;
        case 3:
          break;
        case 4:
          System.out.println("Location ID?");
          int ID = scanner.nextInt();

          break;
        case 5:
          break;
        case 6:
          flag = false;
          break;
        default:
          System.out.println("1-Location Information\n2-Change Floor and Type\n3-Enter Location\n4-Delete Location\n5-Save Locations to CSV File\n6-Exit Program");
          state = scanner.nextInt();
      }
    }
  }
}
