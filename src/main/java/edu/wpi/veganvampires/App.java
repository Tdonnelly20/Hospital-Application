package edu.wpi.veganvampires;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
    Connection connection = null;

    try {
      // substitute your database name for myDB
      connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
      Statement exampleStatement = connection.createStatement();
      exampleStatement.execute("CREATE TABLE TEST(id int primary key)");
      exampleStatement.execute("INSERT INTO TEST VALUES(2)");
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      return;
    }
    System.out.println("Apache Derby connection established!");

    Scanner scanner = new Scanner(System.in);
    int state = 0;
    while(true) {
      switch (state) {
        case 1:
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
        default:
          System.out.println("1-Location Information\n2-Change Floor and Type\n3-Enter Location\n4-Delete Location\n5-Save Locations to CSV File\n6-Exit Program");
          int x = scanner.nextInt();
          state = x;
      }
    }
  }
}
