package edu.wpi.veganvampires;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Vdb {
  static List<Location> locations;

  public static void CreateDB() throws Exception {
    String line = ""; // receives a line from br
    String splitToken = ","; // what we split the csv file with
    BufferedReader br = new BufferedReader(new FileReader("Test.csv"));
    locations = new ArrayList<>();
    String headerLine = br.readLine();
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);
      Location newLoc =
          new Location(
              Integer.valueOf(data[0]),
              Integer.valueOf(data[1]),
              Integer.valueOf(data[2]),
              data[3],
              data[4],
              data[5],
              data[6],
              data[7]);
      locations.add(newLoc);
    }
  }
}
