package edu.wpi.veganvampires;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    System.out.println("MAKING");
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
  }
}
