package edu.wpi.cs3733.d22.teamV.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Pathfinder {
    //For testing purposes
    public static void main(String[] args){

    }

    //We need to have every node have a list of connects to other nodes, and the weight between them
    private class Link{
        @Getter
        @Setter
        private Node node;
        private double distance;
        public Link(Node node, double distance){
            this.node = node;
            this.distance = distance;
        }
    }

    //Nodes contain a list of links and distances to other nodes, which will be used in the pathfinding algorithm
    private class Node{
        @Getter
        @Setter
        private ArrayList<Link> links;
        private String name;
        private boolean visited = false; //All nodes must be marked unvisited to start
        private double weight = Double.MAX_VALUE;
        public Node(String name, ArrayList<Link> links){
            this.name = name;
            this.links = links;
        }
    }

    private ArrayList<Node> nodes;
    public Pathfinder(ArrayList<Node> nodes){
        this.nodes = nodes;
    }

    public Node getNodeFromName(String name){
        for(Node node : nodes){
            if(node.name.equals(name)){
                return node;
            }
        }
        return null;
    }

    //Pathfind from the start node to the end node, and return a queue of the shortest path

    /* Taken from Javatpoint https://www.javatpoint.com/dijkstra-algorithm-java
    Step1: All nodes should be marked as unvisited.

    Step2: All the nodes must be initialized with the "infinite" (a big number) weight. The starting node must be initialized with zero.

    Step3: Mark starting node as the current node.

    Step4: From the current node, analyze all of its neighbors that are not visited yet, and compute their distances by adding the weight of the edge, which establishes the connection between the current node and neighbor node to the current weight of the current node.

    Step5: Now, compare the recently computed weight with the weight allotted to the neighboring node, and treat it as the current weight of the neighboring node,

    Step6: After that, the surrounding neighbors of the current node, which has not been visited, are considered, and the current nodes are marked as visited.

    Step7: When the ending node is marked as visited, then the algorithm has done its job; otherwise,

    Step8: Pick the unvisited node which has been allotted the minimum weight and treat it as the new current node. After that, start again from step4.
     */
    public Queue<Node> pathfind(String startNodeName, String endNodeName){
        //Get the start and end node from their names
        Node startNode = getNodeFromName(startNodeName);
        Node endNode = getNodeFromName(endNodeName);
        //We can't have either be null and the destination cannot be equal to the starting position
        assert (startNode != null && endNode != null && startNode != endNode);
        //Create the queue that will return the shortest path
        Queue<Node> path = new LinkedList<>();
        path.add(startNode);

        Node lowestNode = startNode;

        while(!endNode.visited){
            double currentWeight;
            double lowestWeight = Double.MAX_VALUE;
            Node currentNode = lowestNode;
            currentNode.visited = true;
            for(Link link : currentNode.links){
                //the currentWeight is simply the sum of the weight of the current node and the link of the distance
                currentWeight = currentNode.weight + link.distance;
                link.node.weight = currentWeight;
                //determine our next node based on what has the lowest weight
                if(currentWeight < lowestWeight){
                    lowestNode = link.node;
                    lowestWeight = currentWeight;
                }
            }
            //Add the lowest node to the path
            path.add(lowestNode);
        }
        return path;
    }



}
