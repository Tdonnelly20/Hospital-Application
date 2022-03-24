package edu.wpi.veganvampires;

import java.io.BufferedReader;
import java.io.FileReader;

public class Vdb {
  public static void CreateDB() throws Exception {
    String line = ""; // receives a line from br
    String splitToken = ","; // what we split the csv file with
    BufferedReader br = new BufferedReader(new FileReader("Test.csv"));
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);
    }
  }
}
