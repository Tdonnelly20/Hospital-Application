package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.map.Pathfinder;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import java.io.*;
import java.util.ArrayList;
import java.util.Queue;
import lombok.Getter;
import lombok.Setter;

public class PathfindingDao {

  private Pathfinder pathfinder = new Pathfinder();

  @Getter private static ArrayList<Edge> edges = new ArrayList<>();

  public PathfindingDao() {
    loadFromCSV();
  }

  public Pathfinder getPathfinder() {
    return pathfinder;
  }

  private boolean loading = true; // To make sure we don't override the CSV when we load in

  public boolean isWriting() {
    return writing;
  }

  private boolean writing = false;

  public void pathfind() {
    Queue<Pathfinder.Node> path = pathfinder.pathfind("vHALL00401", "vSERV00101");

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
      String headerLine = br.readLine();
      // int ID, String name, floor,double x, double y, String description, Boolean isDirty) {
      while ((line = br.readLine()) != null) // should create a database based on csv file
      {
        String[] data;
        data = line.split(splitToken);
        addPathNode(data[1], data[2]);
      }
      saveToCSV();
      loading = false;

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Getter
  @Setter
  private class Edge {
    private String name, nodeOne, nodeTwo;

    public Edge(String nodeOne, String nodeTwo) {
      this.name = nodeOne + "_" + nodeTwo; // Name based on CSV edgeID
      this.nodeOne = nodeOne;
      this.nodeTwo = nodeTwo;
      edges.add(this);
    }
  }

  /**
   * Add a pathfinding edge to both the arraylist and to the pathfinding class. Requires two
   * locations
   *
   * @param nodeOne a location ID
   * @param nodeTwo a location ID
   */
  public void addPathNode(String nodeOne, String nodeTwo) {

    // Create and store the edge
    Edge edge = new Edge(nodeOne, nodeTwo);

    if (!loading) {
      saveToCSV();
    }

    // Find the locations
    Location startLocation = Vdb.requestSystem.getLocationDao().getLocationPathfinding(nodeOne);
    Location endLocation = Vdb.requestSystem.getLocationDao().getLocationPathfinding(nodeTwo);

    if (!(startLocation == null || endLocation == null)) {

      // Create start and end nodes, where nodeOne is the start and nodeTwo is the end
      Pathfinder.Node startNode;
      Pathfinder.Node endNode;

      // Check and see if we already made these nodes before
      startNode = Pathfinder.getNodeFromName(nodeOne);
      endNode = Pathfinder.getNodeFromName(nodeTwo);

      // System.out.println(nodeOne + " " + nodeTwo);

      if (startNode == null) {
        startNode = new Pathfinder.Node(nodeOne);
        Pathfinder.addNode(startNode);
      }
      if (endNode == null) {
        endNode = new Pathfinder.Node(nodeTwo);
        Pathfinder.addNode(endNode);
      }
      // Get the X and Y coordinates
      double x1 = startLocation.getXCoord();
      double x2 = endLocation.getXCoord();
      double y1 = startLocation.getYCoord();
      double y2 = startLocation.getYCoord();

      // Distance formula
      double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

      // Add a link between the two of them so they can pathfind to each other
      startNode.addLink(new Pathfinder.Link(endNode, distance));
      endNode.addLink(new Pathfinder.Link(startNode, distance));

    } else {
      // Could not find node locations!
    }
  }

  /** Save the contents of the arraylist edges into Pathfinding.CSV */
  public void saveToCSV() {
    try {
      writing = true;
      FileWriter fw = new FileWriter(VApp.currentPath + "/Pathfinding.csv");
      BufferedWriter bw = new BufferedWriter(fw);
      bw.append("edgeID,startNode,endNode");
      for (Edge edge : getEdges()) {

        String[] outputData = {edge.name, edge.nodeOne, edge.nodeTwo};
        bw.append("\n");
        for (String s : outputData) {
          bw.append(s);
          bw.append(',');
        }
      }

      bw.close();
      fw.close();
      writing = false;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
