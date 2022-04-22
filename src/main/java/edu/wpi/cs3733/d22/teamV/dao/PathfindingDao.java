package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.map.Pathfinder;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;

public class PathfindingDao {

  private Pathfinder pathfinder = new Pathfinder();

  public PathfindingDao() {
    loadFromCSV();
  }

  public void pathfind() {
    Queue<Pathfinder.Node> path = pathfinder.pathfind("FHALL00401", "FSERV00101");

    // Prints out from the destination to the starting position
    for (Pathfinder.Node node : path) {
      System.out.println(node.getName());
    }

    // Polling the first element in the queue gives us the distance of the entire journey
    System.out.println("Distance = " + path.poll().getWeight());
  }

  public void loadFromCSV() {
    try {

      String line = "";
      FileReader fr = new FileReader(VApp.currentPath + "/Pathfinding.CSV");
      BufferedReader br = new BufferedReader(fr);
      String splitToken = ",";
      // int ID, String name, floor,double x, double y, String description, Boolean isDirty) {
      while ((line = br.readLine()) != null) // should create a database based on csv file
      {
        String[] data;
        data = line.split(splitToken);

        // Get the locations from the locationDao
        Location startLocation = Vdb.requestSystem.getLocationDao().getLocationPathfinding(data[1]);
        Location endLocation = Vdb.requestSystem.getLocationDao().getLocationPathfinding(data[2]);

        if (!(startLocation == null || endLocation == null)) {

          Pathfinder.Node startNode;
          Pathfinder.Node endNode;

          startNode = Pathfinder.getNodeFromName(data[1]);
          endNode = Pathfinder.getNodeFromName(data[2]);

          System.out.println(data[1] + " " + data[2]);

          if (startNode == null) {
            startNode = new Pathfinder.Node(data[1]);
          }
          if (endNode == null) {
            endNode = new Pathfinder.Node(data[2]);
          }
          // Get the X and Y coordinates
          double x1 = startLocation.getXCoord();
          double x2 = endLocation.getXCoord();
          double y1 = startLocation.getYCoord();
          double y2 = startLocation.getYCoord();

          // Distance formula
          double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
          startNode.addLink(new Pathfinder.Link(endNode, distance));
          endNode.addLink(new Pathfinder.Link(startNode, distance));

          Pathfinder.addNode(startNode);
          Pathfinder.addNode(endNode);
        }
      }
      System.out.println(Pathfinder.getNodes().size());
      pathfind();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
