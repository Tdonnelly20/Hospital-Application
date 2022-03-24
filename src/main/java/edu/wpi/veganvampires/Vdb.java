package edu.wpi.veganvampires;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

public class Vdb {

  public static void CreateDB() throws Exception {
    String line = ""; // receives a line from br
    String splitToken = ","; // what we split the csv file with
    BufferedReader br = new BufferedReader(new FileReader("Test.csv"));
    List<Location> locations=new ArrayList<>();
    String headerLine = br.readLine();
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);
      Location newLoc = new Location();
      newLoc.nodeID=data[0];
      newLoc.xCoord= Integer.valueOf (data[1]);
      newLoc.yCoord= Integer.valueOf (data[2]);
      newLoc.floor= Integer.valueOf (data[3]);
      newLoc.building= data[4];
      newLoc.nodeType=data[5];
      newLoc.longName=data[6];
      newLoc.shortName=data[7];
      locations.add(newLoc);
    }
  }
}
