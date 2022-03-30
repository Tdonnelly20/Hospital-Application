package edu.wpi.veganvampires;

import edu.wpi.veganvampires.Features.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Vdb {
  static List<Location> locations;

  public static void CreateDB() throws Exception {
    String line = ""; // receives a line from br
    String splitToken = ","; // what we split the csv file with
    String currentPath = System.getProperty("user.dir");
    currentPath += "\\src\\main\\java\\edu\\wpi\\veganvampires";
    System.out.println(currentPath);
    FileReader fr = new FileReader(currentPath + "\\TowerLocations.csv");
    BufferedReader br = new BufferedReader(fr);

    locations = new ArrayList<>();

    String headerLine = br.readLine();

    System.out.println("Generating database, please wait...");
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);
      Location newLoc =
          new Location(
              data[0],
              Integer.valueOf(data[1]),
              Integer.valueOf(data[2]),
              data[3],
              data[4],
              data[5],
              data[6],
              data[7]);
      locations.add(newLoc);
    }
    System.out.println("Database made");
    // V1();
  }

  public static void V1() {
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
      System.out.println("Apache Derby connection established!");
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
            state = 7;
            break;
          case 7:
            break;
          default:
            System.out.println(
                "1-Location Information\n2-Change Floor and Type\n3-Enter Location\n4-Delete Location\n5-Save Locations to CSV File\n6-Exit Program");
            state = scanner.nextInt();
        }
        if (loop) {
          state = scanner.nextInt();
        }
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      return;
    } catch (Exception e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }

    // for (Location location : Vdb.locations) {
    //  System.out.println("ID: " + location.nodeID);
    //  }
  }

  public static void addMedicineDelivery(MedicineDelivery newMedicineDelivery) {
    // Code for adding a medicine delivery to the database
    System.out.println("Arrived at database!");
  }

  public static void addSanitationRequest(SanitationRequest newSanitationRequest) {
    // Code for adding a sanitation request to the database
    System.out.println("Arrived at database!");
  }

  public static void addEquipmentDelivery(EquipmentDelivery newEquipmentDelivery) {
    // Code for adding a equipment request to the database
    System.out.println("Arrived at database!");
  }

  public static void addLabRequest(LabRequest labRequest) {
    // Code for adding a lab request to the database
    System.out.println("Arrived at database!");
  }

  public static void addLocations(Location new_location) {
    System.out.println("Arrived at database!");
  }

  public List<Location> getLocations() {
    return locations;
  }
}
