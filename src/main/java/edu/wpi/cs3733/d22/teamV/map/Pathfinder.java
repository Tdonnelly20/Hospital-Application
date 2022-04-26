package edu.wpi.cs3733.d22.teamV.map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// Test example and some inspiration/code taken from https://www.baeldung.com/java-dijkstra
public class Pathfinder {

  private static ArrayList<Node> allNodes = new ArrayList<>();

  public Pathfinder() {}

  public static void main(String[] args) {
    // See https://www.baeldung.com/wp-content/uploads/2017/01/initial-graph.png for a picture of
    // what I'm creating here
    Node nodeA = new Node("A");
    Node nodeB = new Node("B");
    Node nodeC = new Node("C");
    Node nodeD = new Node("D");
    Node nodeE = new Node("E");
    Node nodeF = new Node("F");

    // Add links to each of the nodes (one directional but I could make it bi-directional if you
    // need it)
    nodeA.addLink(new Link(nodeB, 10));
    nodeA.addLink(new Link(nodeC, 15));
    nodeB.addLink(new Link(nodeA, 10));
    nodeC.addLink(new Link(nodeA, 15));

    nodeB.addLink(new Link(nodeD, 12));
    nodeB.addLink(new Link(nodeF, 15));
    nodeD.addLink(new Link(nodeB, 12));
    nodeF.addLink(new Link(nodeB, 15));

    nodeC.addLink(new Link(nodeE, 10));
    nodeE.addLink(new Link(nodeC, 10));

    nodeF.addLink(new Link(nodeD, 1));
    nodeE.addLink(new Link(nodeD, 2));
    nodeD.addLink(new Link(nodeF, 1));
    nodeD.addLink(new Link(nodeE, 2));

    nodeE.addLink(new Link(nodeF, 5));
    nodeF.addLink(new Link(nodeE, 5));

    // Add all the nodes to the list
    ArrayList<Node> nodes = new ArrayList<>();

    nodes.add(nodeA);
    nodes.add(nodeB);
    nodes.add(nodeC);
    nodes.add(nodeD);
    nodes.add(nodeE);
    nodes.add(nodeF);

    // Create the pathfinder object
    Pathfinder pathfinder = new Pathfinder(nodes);
    // Feel free to mess around with the destinations, just keep in mind that nodes are
    // one-directional, not bi-directional. (A has a link to C, C does not have a link to A)
    Queue<Node> path = pathfinder.pathfind("A", "F");
    Queue<Node> pathBackwards = pathfinder.pathfind("F", "A");
  }

  public static void printLink(Queue<Node> path) {
    // Prints out from the destination to the starting position
    for (Node node : path) {
      System.out.println(" " + node.getName());
    }

    // Polling the first element in the queue gives us the distance of the entire journey
    System.out.println("Distance = " + path.poll().getWeight());
  }
  // We need to have every node have a list of connects to other nodes, and the weight between them
  @Getter
  @Setter
  public static class Link {
    private Node node;
    private double distance;

    public Link(Node node, double distance) {
      this.node = node;
      this.distance = distance;
    }
  }

  // Nodes contain a list of links and distances to other nodes, which will be used in the
  // pathfinding algorithm
  @Getter
  @Setter
  public static class Node {
    private ArrayList<Link> links = new ArrayList<>();
    private String name;
    private Node previous = null;
    private boolean visited = false; // All nodes must be marked unvisited to start
    private double weight = Double.MAX_VALUE;

    public Node(String name) {
      this.name = name;
      allNodes.add(this);
    }

    public void addLink(Link link) {
      links.add(link);
    }

    public void removeLink(Link link) {
      links.remove(link);
    }
  }

  private static ArrayList<Node> nodes = new ArrayList<>();

  public Pathfinder(ArrayList<Node> nodes) {
    this.nodes = nodes;
  }

  public static ArrayList<Node> getNodes() {
    return nodes;
  }

  public static Node getNodeFromName(String name) {
    for (Node node : nodes) {
      if (node.name.equals(name)) {
        return node;
      }
    }
    return null;
  }

  // Pathfind from the start node to the end node, and return a queue of the shortest path

  public Queue<Node> pathfind(String startNodeName, String endNodeName) {
    // System.out.println("Path Find Start: " + startNodeName + "\nPath Find End: " + endNodeName);
    if (startNodeName != null && endNodeName != null) {
      // Get the start and end node from their names
      Node startNode = getNodeFromName(startNodeName);
      Node endNode = getNodeFromName(endNodeName);
      // We can't have either be null and the destination cannot be equal to the starting position
      assert (startNode != null && endNode != null && startNode != endNode);
      // Create a list of nodes that are still unsettled
      ArrayList<Node> unsettledNodes = new ArrayList<>();
      // Add the starting node to the list of unsettled nodes
      unsettledNodes.add(startNode);
      System.out.println(allNodes.size());
      // Make sure that all nodes are marked as unvisited and that their weight is the max value
      for (Node node : allNodes) {
        node.visited = false;
        node.previous = null;
        node.weight = Double.MAX_VALUE;
      }

      // Set the starting weight of the current node to 0 (since we're already there)
      startNode.weight = 0;
      /*System.out.println(
                startNode.name
                    + " "
                    + startNode.visited
                    + " "
                    + startNode.previous
                    + " "
                    + startNode.weight);
      */
      // The actual algorithm, iterate through all unsettled nodes, from closest to farthest
      while (!unsettledNodes.isEmpty()) {
        Node currentNode = getLowestNode(unsettledNodes);
        unsettledNodes.remove(currentNode);

        double currentNodeWeight = currentNode.getWeight();
        for (Link link : currentNode.getLinks()) {
          Node destinationNode = link.getNode();
          double distance = link.getDistance();
          double destinationNodeWeight = destinationNode.getWeight();

          double distanceFromCurrentNode = currentNodeWeight + distance;

          if (distanceFromCurrentNode < destinationNodeWeight) {
            destinationNode.setWeight(distanceFromCurrentNode);
            destinationNode.setPrevious(currentNode);
            unsettledNodes.add(destinationNode);
          }
        }
      }

      // Create the queue that will return the shortest path
      Queue<Node> path = new LinkedList<>();

      // return the shortest path from the end node
      return getPath(path, endNode);
    }
    return null;
  }

  public Node getLowestNode(ArrayList<Node> nodes) {
    Node closestNode = null;
    double shortestDistance = Double.MAX_VALUE;
    for (Node node : nodes) {
      if (node.weight < shortestDistance) {
        shortestDistance = node.weight;
        closestNode = node;
      }
    }
    System.out.println(closestNode.name + " " + closestNode.weight);
    return closestNode;
  }

  // Recursive algorithm that gets the path from one node to another
  public Queue<Node> getPath(Queue<Node> path, Node node) {
    path.add(node);
    if (node.previous != null) {
      return getPath(path, node.previous);
    } else {
      printLink(path);
      return path;
    }
  }

  public static void addNode(Node node) {
    nodes.add(node);
  }

  public static void removeNode(Node node) {
    nodes.remove(node);
  }
}
