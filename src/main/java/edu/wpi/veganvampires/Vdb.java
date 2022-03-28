package edu.wpi.veganvampires;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Vdb {
  static List<Location> locations;
  static List<Equipment> equipment;

  public static void CreateDB() throws Exception {
    String line = ""; // receives a line from br
    String splitToken = ","; // what we split the csv file with
    String currentPath = System.getProperty("user.dir");
    if (currentPath.contains("TeamVeganVampires")) {
      int position = currentPath.indexOf("TeamVeganVampires") + 17;
      if (currentPath.length() > position) {
        currentPath = currentPath.substring(0, position);
      }
      currentPath += "\\src\\main\\resources\\edu\\wpi\\veganvampires";
    }
    FileReader fr = new FileReader(currentPath + "\\TowerLocations.csv");
    BufferedReader br = new BufferedReader(fr);

    locations = new ArrayList<>();
    equipment = new ArrayList<>();
    String headerLine = br.readLine();

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

    fr = new FileReader(currentPath + "\\MedEquipReq.CSV");
    br = new BufferedReader(fr);
    headerLine = "";
    headerLine = br.readLine();
    line = "";

    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);
      Equipment e = new Equipment(data[0], data[1], Integer.valueOf(data[2]));
      equipment.add(e);
    }
    System.out.println("Database made");
  }
}
