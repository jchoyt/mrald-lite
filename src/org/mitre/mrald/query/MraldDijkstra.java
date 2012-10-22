/*
 *  Copyright 2008 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mitre.mrald.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.MraldOutFile;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    March 14, 2001
 */
public class MraldDijkstra
{
    /**
     *  Return value for testJoinPath() that indicates that the join path is not
     *  complete
     */
    public final static int JOIN_PATH_NOT_COMPLETE = 1;
    /**
     *  Return value for testJoinPath() that indicates that the join path is not
     *  unique
     */
    public final static int JOIN_PATH_NOT_COMPLETE_OR_UNIQUE = 3;
    /**
     *  Return value for testJoinPath() that indicates that the join path is not
     *  unique and not complete
     */
    public final static int JOIN_PATH_NOT_UNIQUE = 2;
    /**
     *  Return value for testJoinPath() that indicates that the join path both
     *  complete and unique
     */
    public final static int JOIN_PATH_OK = 0;
    /**
     *  All known Edges
     *
     *@since
     */
    ArrayList<Edge> edges;
    /**
     *  All known Nodes
     *
     *@since
     */
    ArrayList<Node> nodes;
    /**
     *  An ArrayList of Edges that are necessary to connect all tables in the
     *  From list
     *
     *@since
     */
    ArrayList quals;


    /**
     *  Constructor for the MraldDijkstra object
     *
     *@param  msg  Description of Parameter
     *@since
     */
    public MraldDijkstra( MsgObject msg )
    {
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
        quals = null;
        /*
         *  from MsgObject, get a set of all possible nodes and all edges
         */
        ArrayList links = msg.getLinks();
        /*
         *  build all cross references between nodes and edges
         */
        Node existingNode1;
        Node existingNode2;
        LinkElement currentElement;
        String nodeName;
        for ( int i = 0; i < links.size(); i++ )
        {
            currentElement = ( LinkElement ) links.get( i );
            nodeName = currentElement.getPrimaryTable();
            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "MraldDijkstra: constructor: primary Table " + nodeName);

            existingNode1 = findNode( nodeName );
            if ( existingNode1 == null )
            {
                existingNode1 = new Node( nodeName );
                nodes.add( existingNode1 );
            }
            nodeName = currentElement.getSecondaryTable();
            existingNode2 = findNode( nodeName );
            if ( existingNode2 == null )
            {
                existingNode2 = new Node( nodeName );
                nodes.add( existingNode2 );
            }
            edges.add( new Edge( existingNode1, existingNode2, currentElement.getQual() ) );
        }
    }


    /**
     *  Gets the QualString attribute of the MraldDijkstra object
     *
     *@return    The QualString value
     *@since
     */
    public String getQualString()
    {
        if ( quals.size() == 0 )
        {
            return "";
        }
        StringBuffer ret = new StringBuffer();
        for ( int i = 0; i < quals.size(); i++ )
        {
            ret.append( ( ( Edge ) quals.get( i ) ).getQual() );
            if ( i < ( quals.size() - 1 ) )
            {
                ret.append( " AND " );
            }
        }
        return ret.toString();
    }


    /**
     *  Searches from the nodes passed in the LinkElements for a node with a
     *  name that matches the name passed in.
     *
     *@param  nodeName  Name of the node you want retrieved
     *@return           The full Node object with a name matching the name
     *      passed
     *@since
     */
    public Node findNode( String nodeName )
    {
        if(nodeName==null)
        {
            NullPointerException e = new NullPointerException("Passed a 'null' nodeName to MraldDijkstra.findNode()");
            throw e;
        }
        Node currentNode;
        for ( int i = 0; i < nodes.size(); i++ )
        {
            currentNode = nodes.get( i );
            if ( currentNode.getName().equalsIgnoreCase( nodeName ) )
            {
                return currentNode;
            }
        }
        return null;
    }


    /**
     *  Note: this method will add Strings to the startingFromList.
     *
     *@param  startingFromList            Description of Parameter
     *@return                             Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public String runDijkstra( ArrayList<String> startingFromList )
        throws MraldException
    {
        /*
         *  get From clause list - input as an ArrayList of Strings
         *  if #From clause < 2, you're done - no links necessary
         */
        if ( startingFromList.size() < 2 )
        {
            return "";
        }
        /*
         *  This is Dijkstra's algorithm  We start from a random
         *  node (that is in the FromList) and connect them all using
         *  the shortest path.
         */
        HashSet<Node> knownPath = new HashSet<Node>();
        HashSet<Node> unknownPath = new HashSet<Node>( nodes );
        Node cheapestNode;
        /*
         *  set start node
         */
        cheapestNode = findNode( startingFromList.get( 0 ) );
        if ( cheapestNode == null )
        {
            throw new MraldException( "You are attempting to build a form that has more than one table and no joins between tables.  All tables included must be joined to at least one other table, or the results of the query will not make sense (you will get a Cartesian product - a lot of unrelated data" );
        }
        cheapestNode.setCostToReach( 0 );
        while ( unknownPath.size() > 0 )
        {
            cheapestNode = findCheapest( unknownPath );
            knownPath.add( cheapestNode );
            unknownPath.remove( cheapestNode );
            relax( cheapestNode );
        }
        /*
         *  Store the needed quals:
         *
         *  for each String in the FromList, find the corresponding node
         *  and add the path to reach it to the qualList HashSet.  Also have
         *  to add any new tables used to the startingFromList.
         *
         *  use the HashSet to fill the quals ArrayList
         */
        HashSet<Edge> qualList = new HashSet<Edge>();//set of edges used
        Node temp;
//        ArrayList tempPath;
        for ( int i = 0; i < startingFromList.size(); i++ )
        {
            temp = findNode( startingFromList.get( i ) );
            if ( temp == null )
            {
                StringBuffer ret = new StringBuffer();
                Node currentNode;
                for ( int j = 0; j < nodes.size(); j++ )
                {
                    currentNode = nodes.get( j );
                    ret.append( currentNode.getName() );
                    ret.append( "\n" );
                }
                throw new MraldException( "There has been an error in analysing the link structure in your request.\n\nFor debugging purposes, table '" +
                        startingFromList.get( i ) +
                        "' was not found in list of available nodes.\nThe list includes:\n" +
                        ret.toString() );
            }
            qualList.addAll( temp.getPath() );
        }
        quals = new ArrayList<Edge>( qualList );
        /*
         *  add tables as necessary to the startingFromList
         */
//        HashSet fromTables = new HashSet();
        Edge currentEdge = new Edge();
        Node[] nodes = new Node[2];
        Iterator fromIt = quals.iterator();
        while ( fromIt.hasNext() )
        {
            currentEdge = ( Edge ) fromIt.next();
            nodes = currentEdge.getNodes();
            if ( !startingFromList.contains( nodes[0].getName() ) )
            {
                startingFromList.add( nodes[0].getName() );
            }
            if ( !startingFromList.contains( nodes[1].getName() ) )
            {
                startingFromList.add( nodes[1].getName() );
            }
        }
        /*
         *  return the QualString containing all the necessary links
         */
        return getQualString();
    }


    /**
     *  Tests the join path of a created MraldDijkstra object to determine
     *  whether or not the join path is complete. An incomplete join path occurs
     *  when there is not a complete join path amongst all the tables included
     *  on the form.
     *
     *@param  tableList  List of tables used in the form or query
     *@return            integer value corresponding to the state of the join
     *      paths. See static fields in this class
     *@see               org.mitre.mrald.query
     *@see               #JOIN_PATH_OK
     */
    public int testCompleteJoinPath( ArrayList tableList )
    {
        /*
         *  test to ensure all tables in the tableList are also in the nodes list
         */
        for ( int i = 0; i < tableList.size(); i++ )
        {
            boolean ok = false;
            for ( int x = 0; x < nodes.size(); x++ )
            {
                if ( tableList.get( i ).equals( nodes.get( x ).getName() ) )
                {
                    ok = true;
                }
            }
            if ( !ok )
            {
                return JOIN_PATH_NOT_COMPLETE;
            }
        }
        /*
         *  now test to see whether all nodes can be reached from all other nodes
         */
        Set<Node> visitedNodes = new HashSet<Node>();
        Set<Edge> usedEdges = new HashSet<Edge>();
        Set<Edge> unUsedEdges = new HashSet<Edge>();
        /*
         *  visit all nodes from the first node in nodes and make sure they all get visited
         */
        Node currentNode = nodes.get( 0 );
        visitedNodes.add( currentNode );
        unUsedEdges.addAll( currentNode.getAttachedEdges() );
        Edge currentEdge = null;
        while ( unUsedEdges.size() != 0 )
        {
            /*
             *  get the first Edge and get the attached node and set it to visited
             */
            currentEdge = unUsedEdges.iterator().next();
            if ( !visitedNodes.contains( currentEdge.getNodes()[0] ) )
            {
                currentNode = currentEdge.getNodes()[0];
            }
            else if ( !visitedNodes.contains( currentEdge.getNodes()[1] ) )
            {
                currentNode = currentEdge.getNodes()[1];
            }
            ArrayList<Edge> edgesToAdd = currentNode.getAttachedEdges();
            for ( int i = 0; i < edgesToAdd.size(); i++ )
            {
                if ( !usedEdges.contains( edgesToAdd.get( i ) ) )
                {
                    unUsedEdges.add( edgesToAdd.get( i ) );
                }
            }
            visitedNodes.add( currentNode );
            /*
             *  remove the processed edge from the set and put it in the usedEdges set
             */
            unUsedEdges.remove( currentEdge );
            usedEdges.add( currentEdge );
        }
        return visitedNodes.size() == nodes.size() ? JOIN_PATH_OK : JOIN_PATH_NOT_COMPLETE;
    }


    /**
     *  Tests the join path of a created MraldDijkstra object to determine
     *  whether or not the join path is both unique and complete. See the
     *  package documentation on MRALD form restrictions for more information.
     *
     *@param  tableList  Description of the Parameter
     *@return            Description of the Return Value
     *@see               org.mitre.mrald.query
     */
    public int testJoinPath( ArrayList tableList )
    {
        if ( tableList.size() == 1 )
        {
            return JOIN_PATH_OK;
        }
        else
        {
            return testCompleteJoinPath( tableList ) + testUniqueJoinPath();
        }
    }


    /**
     *  Tests the join path of a created MraldDijkstra object to determine
     *  whether or not the join path is unique. A non-unique join path occurs
     *  when there is more than one join path between any two tables. When this
     *  happens, which join path to use, and therefore the query result, is
     *  indeterminate. Therefore, this condition should be avoided.
     *
     *@return    JOIN_PATH_OK or JOIN_PATH_NOT_UNIQUE
     *@see       org.mitre.mrald.query, #JOIN_PATH_OK, #JOIN_PATH_NOT_UNIQUE
     */
    public int testUniqueJoinPath()
    {
        if ( edges.size() < 2 )
        {
            return JOIN_PATH_OK;
        }
        else
        {
            /*
             *  make sure all compound joins go one way, i.e., when there is
             *  more than one join between any two tables, the PK's are all
             *  on one side of the join
             */
            Edge currentEdge;
            Edge testingEdge;
            for ( int i = 0; i < edges.size(); i++ )
            {
                currentEdge = edges.get( i );
                for ( int x = i; x < edges.size(); x++ )
                {
                    testingEdge = edges.get( x );
                    if ( testingEdge.getNodes()[0] == currentEdge.getNodes()[1] && testingEdge.getNodes()[1] == currentEdge.getNodes()[0] )
                    {
                        return JOIN_PATH_NOT_UNIQUE;
                    }
                }
            }
            /*
             *  visit all the nodes and make sure you can't get to one more than once
             */
            ArrayList<Node> visitedNodes = new ArrayList<Node>();
            ArrayList<Edge> usedEdges = new ArrayList<Edge>();
            Node startNode = nodes.get( 0 );
            visitedNodes.add( startNode );
            return traverseUniqueJoinPath( startNode, usedEdges, visitedNodes );
        }
    }


    /**
     *  Builds a list of all Nodes and Edges in the graph.
     *
     *@return    a list of all Nodes and Edges in the graph.
     */
    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append( "List of all Nodes:" );
        for ( int i = 0; i < nodes.size(); i++ )
        {
            ret.append( "\n\t" );
            ret.append( nodes.get( i ).getName() );
        }
        ret.append( "\nList of all Edges:" );
        for ( int i = 0; i < edges.size(); i++ )
        {
            ret.append( "\n\t" );
            ret.append( edges.get( i ).getQual() );
        }
        return ret.toString();
    }


    /**
     *  Recursively traverses the join path looking for nodes it has visited
     *  before. If it tries to visit a node it used before via an edge it used
     *  before, that's OK (it's just trying to go backwards - we are concerned
     *  about a different way to get to the same node).
     *
     *@param  currentNode   The node we have just arrived at
     *@param  usedEdges     All the edges we have used in our travels so far
     *@param  visitedNodes  All the nodes we have visited in our travels so far
     *@return               result of our most recent step if JOIN_PATH_OK,
     *      JOIN_PATH_NOT_UNIQUE otherwise
     */
    public int traverseUniqueJoinPath( Node currentNode, List<Edge> usedEdges, List<Node> visitedNodes )
    {
//        int joinPathStatus = JOIN_PATH_OK;
        Map adjacentNodes = currentNode.getAdjacentNodes();
        Iterator iter = adjacentNodes.keySet().iterator();
        Node neighborNode;
        Edge neighborsEdge;
        while ( iter.hasNext() )
        {
            neighborNode = ( Node ) iter.next();
            neighborsEdge = ( Edge ) adjacentNodes.get( neighborNode );
            if ( usedEdges.contains( neighborsEdge ) )
            {
                //done - need to go to next edge
                continue;
            }
            if ( visitedNodes.contains( neighborNode ) )
            {
                return JOIN_PATH_NOT_UNIQUE;
            }
            else
            {
                visitedNodes.add( neighborNode );
                usedEdges.add( neighborsEdge );
                if ( traverseUniqueJoinPath( neighborNode, usedEdges, visitedNodes ) == JOIN_PATH_NOT_COMPLETE )
                {
                    return JOIN_PATH_NOT_COMPLETE;
                }
            }
        }
        return JOIN_PATH_OK;
    }


    /**
     *  Description of the Method
     *
     *@param  set                     Description of Parameter
     *@return                         Description of the Returned Value
     *@exception  ClassCastException  Description of Exception
     */
    private Node findCheapest( HashSet<Node> set )
        throws ClassCastException
    {
        TreeSet<Node> sortedSet = new TreeSet<Node>( set );
        return sortedSet.first();
    }


    /**
     *  "Relaxes" a node - For each adjacent node, find whether the weight of
     *  the path to this node (which was just determined to be the shortest),
     *  plus the weight of the edge connecting them is less than the existing
     *  shortest path for the adjacent node. If it is, replace that node's path
     *  with the path of this node, plus this node, and set the weight
     *  accordingly
     *
     *@param  justSet                     Description of Parameter
     *@exception  MraldException  Description of Exception
     *@since
     */
    private void relax( Node justSet )
        throws MraldException
    {
        ArrayList<Edge> attachedEdges = justSet.getAttachedEdges();
        Node[] edgeNodes = new Node[2];
        /*
         *  find adjacent nodes
         */
        for ( int i = 0; i < attachedEdges.size(); i++ )
        {
            Edge edgeChecking = attachedEdges.get( i );
            edgeNodes = edgeChecking.getNodes();
            ArrayList<Edge> newPath;
            /*
             *  compare weights and change if appropriate
             *  Change involves setting the new weight and path of edges used
             */
            if ( justSet == edgeNodes[0] )
            {
                if ( ( justSet.getCostToReach() + edgeChecking.getWeight() ) < edgeNodes[1].getCostToReach() )
                {
                    edgeNodes[1].setCostToReach( justSet.getCostToReach() + edgeChecking.getWeight() );
                    newPath = new ArrayList<Edge>( justSet.getPath() );
                    /*
                     *  add the new, shorter path and any other parts of a compound key
                     */
                    newPath.add( edgeChecking );
                    for ( int j = 0; j < attachedEdges.size(); j++ )
                    {
                        if ( edgeChecking != attachedEdges.get( j ) && edgeChecking.hasSameNodes( attachedEdges.get( j ) ) )
                        {
                            newPath.add( attachedEdges.get( j ) );
                        }
                    }
                    /*
                     *  set the new path to the path
                     */
                    edgeNodes[1].setPath( newPath );
                }
            }
            else if ( justSet == edgeNodes[1] )
            {
                if ( ( justSet.getCostToReach() + edgeChecking.getWeight() ) < edgeNodes[0].getCostToReach() )
                {
                    edgeNodes[0].setCostToReach( justSet.getCostToReach() + edgeChecking.getWeight() );
                    newPath = new ArrayList<Edge>( justSet.getPath() );
                    /*
                     *  add the new, shorter path and any other parts of a compound key
                     */
                    newPath.add( edgeChecking );
                    for ( int j = 0; j < attachedEdges.size(); j++ )
                    {
                        if ( edgeChecking != attachedEdges.get( j ) && edgeChecking.hasSameNodes( attachedEdges.get( j ) ) )
                        {
                            newPath.add( attachedEdges.get( j ) );
                        }
                    }
                    /*
                     *  set the new path to the path
                     */
                    edgeNodes[0].setPath( newPath );
                }
            }
            else
            {
                throw new MraldException( "MraldDijkstra.relax() not functioning properly" );
            }
        }
    }


    /**
     *  Description of the Method
     *
     *@param  known    Description of Parameter
     *@param  unknown  Description of Parameter
     */
    /* PM: Never used!
    private void status( HashSet known, HashSet unknown )
    {
        // MraldOutFile.appendToFile( Config.getProperty( "LOGFILE" ), "*******New Loop********\r\n" + unknown.size() + " unknown left\r\nKnown: " );
        Node c;
        Iterator it = known.iterator();
        while ( it.hasNext() )
        {
            c = ( Node ) it.next();
            // MraldOutFile.appendToFile( Config.getProperty( "LOGFILE" ), c.toString() );
        }
        // MraldOutFile.appendToFile( Config.getProperty( "LOGFILE" ), "Unknown: " );
        it = unknown.iterator();
        while ( it.hasNext() )
        {
            c = ( Node ) it.next();
            // MraldOutFile.appendToFile( Config.getProperty( "LOGFILE" ), c.toString() );
        }
    }*/

}

/**
 *  A node is a representation of a database table and all the joins to and from
 *  it via the attachedEdges memeber. <br>
 *  <br>
 *  "Note: this class has a natural ordering that is inconsistent with equals."
 *
 *@author     jchoyt
 *@created    March 16, 2001
 */
class Node extends Object implements Comparable
{


    private Map<Node,Edge> adjacentNodes;

    private ArrayList<Edge> attachedEdges;
    private int costToReach;
    private String name;
    private ArrayList<Edge> pathToReach;


    /**
     *  Constructor for the Node object
     *
     *@since
     */
    public Node()
    {
        attachedEdges = new ArrayList<Edge>();
        costToReach = Integer.MAX_VALUE;
        pathToReach = new ArrayList<Edge>();
        adjacentNodes = null;
        name = "";
    }


    /**
     *  Constructor for the Node object
     *
     *@param  nodeName  Description of Parameter
     *@since
     */
    public Node( String nodeName )
    {
        attachedEdges = new ArrayList<Edge>();
        costToReach = Integer.MAX_VALUE;
        pathToReach = new ArrayList<Edge>();
        adjacentNodes = null;
        name = nodeName;
    }


    /**
     *  Sets the CostToReach attribute of the Node object
     *
     *@param  cost  The new CostToReach value
     *@since
     */
    public void setCostToReach( int cost )
    {
        costToReach = cost;
    }


    /**
     *  Sets the Name attribute of the Node object
     *
     *@param  nameVal  The new Name value
     *@since
     */
    public void setName( String nameVal )
    {
        name = nameVal;
    }


    /**
     *  Calculates and returns a list of adjacent nodes
     *
     *@param  newPath  The new Path value
     *@since
     */
    public void setPath( ArrayList<Edge> newPath )
    {
        pathToReach = new ArrayList<Edge>( newPath );
    }


    /**
     *  Gets the adjacentNodes attribute of the Node object
     *
     *@return    A map of the adjacent Node and the Edge that got it there. The
     *      Node is the Key.
     */
    public Map getAdjacentNodes()
    {
        if ( adjacentNodes == null )
        {
            adjacentNodes = new HashMap<Node,Edge>();
            Node edgeNode1;
            Node edgeNode2;
            for ( int i = 0; i < attachedEdges.size(); i++ )
            {
                edgeNode1 = attachedEdges.get( i ).getNodes()[0];
                edgeNode2 = attachedEdges.get( i ).getNodes()[1];
                if ( edgeNode1 != this )
                {
                    adjacentNodes.put( edgeNode1, attachedEdges.get( i ) );
                }
                else
                {
                    // if node 1 is this node, then node 2 isn't so add it
                    adjacentNodes.put( edgeNode2, attachedEdges.get( i ) );
                }
            }
        }
        return adjacentNodes;
    }


    /**
     *  Gets the AttachedEdges attribute of the Node object
     *
     *@return    The AttachedEdges value
     *@since
     */
    public ArrayList<Edge> getAttachedEdges()
    {
        return attachedEdges;
    }


    /**
     *  Gets the CostToReach attribute of the Node object
     *
     *@return    The CostToReach value
     *@since
     */
    public int getCostToReach()
    {
        return costToReach;
    }


    /**
     *  gets the name attribute of the node object
     *
     *@return    The Name value
     *@since
     */
    public String getName()
    {
        return name;
    }


    /**
     *  Gets the Path attribute of the Node object
     *
     *@return    The Path value
     *@since
     */
    public ArrayList<Edge> getPath()
    {
        return pathToReach;
    }


    /**
     *  Attaches a new edge to this node. It now looks for redundant edges
     *  before adding them.
     *
     *@param  newEdge  Description of Parameter
     *@since
     */
    public void attachEdge( Edge newEdge )
    {
        for ( int i = 0; i < attachedEdges.size(); i++ )
        {
            if ( attachedEdges.get( i ).equals( newEdge ) )
            {
                return;//Don't add
            }
        }
        attachedEdges.add( newEdge );
        // need to reset this to null so it'll get recalculated next time
        adjacentNodes = null;
    }


    /**
     *  Determines a sort order based on the cost to reach a particular node.
     *  Orders ascending by cost to reach the path.
     *
     *@param  o  Description of Parameter
     *@return    Description of the Returned Value
     *@since
     */
    public int compareTo( Object o )
    {
        int returnVal = this.costToReach - ( ( Node ) o ).getCostToReach();
        if ( returnVal != 0 )
        {
            return returnVal;
        }
        else
        {
            returnVal = pathToReach.size() - ( ( ( Node ) o ).getPath().size() );
        }
        if ( returnVal == 0 )
        {
            returnVal = name.compareTo( ( ( Node ) o )
                    .getName() );
        }
        return returnVal;
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Returned Value
     *@since
     */
    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append( "Node: " + name );
        ret.append( " Cost to reach: " + costToReach );
        ret.append( "\nPath to Reach: " );
        for ( int i = 0; i < pathToReach.size(); i++ )
        {
            ret.append( pathToReach.get( i ).getQual() );
            if ( i < ( pathToReach.size() - 1 ) )
            {
                ret.append( ", " );
            }
        }
        ret.append( "\nAttached Edges: " );
        for ( int i = 0; i < attachedEdges.size(); i++ )
        {
            ret.append( "\n\t" + attachedEdges.get( i ).getQual() );
        }
        ret.append( "\n" );
        return ret.toString();
    }
}

/**
 *  An Edge is the representation of a join between two database tables.
 *
 *@author     jchoyt
 *@created    June 3, 2002
 */
class Edge extends Object
{


    String qual;
    private boolean needed;

    private Node node1;
    private Node node2;
    private int weight;


    /**
     *  Constructor for the Edge object. Don't use this constructor unless you
     *  want an empty Edge.
     *
     *@since
     */
    public Edge()
    {
        node1 = null;
        node2 = null;
        weight = Integer.MAX_VALUE;
        needed = false;
        qual = null;
    }


    /**
     *  Constructor for the Edge object This one sets the internal variables.
     *
     *@param  primaryTable    Description of Parameter
     *@param  secondaryTable  Description of Parameter
     *@param  qualValue       Description of Parameter
     *@since
     */
    public Edge( Node primaryTable, Node secondaryTable, String qualValue )
    {
        node1 = primaryTable;
        node2 = secondaryTable;
        weight = 1;
        needed = false;
        qual = qualValue;
        node1.attachEdge( this );
        node2.attachEdge( this );
    }


    /**
     *  Sets the Needed attribute of the Edge object
     *
     *@param  neededVal  The new Needed value
     *@since
     */
    public void setNeeded( boolean neededVal )
    {
        needed = neededVal;
    }


    /**
     *  Sets the Nodes attribute of the Edge object
     *
     *@param  newNode1  The new Nodes value
     *@param  newNode2  The new Nodes value
     *@since
     */
    public void setNodes( Node newNode1, Node newNode2 )
    {
        node1 = newNode1;
        node2 = newNode2;
    }


    /**
     *  Sets the Qual attribute of the Edge object
     *
     *@param  qualVal  The new Qual value
     *@since
     */
    public void setQual( String qualVal )
    {
        qual = qualVal;
    }


    /**
     *  Sets the Weight attribute of the Edge object
     *
     *@param  weightVal  The new Weight value
     *@since
     */
    public void setWeight( int weightVal )
    {
        weight = weightVal;
    }


    /**
     *  Gets the Needed attribute of the Edge object
     *
     *@return    The Needed value
     *@since
     */
    public boolean getNeeded()
    {
        return needed;
    }


    /**
     *  Gets the Nodes attribute of the Edge object
     *
     *@return    The Nodes value
     *@since
     */
    public Node[] getNodes()
    {
        Node[] returnNodes = new Node[2];
        returnNodes[0] = node1;
        returnNodes[1] = node2;
        return returnNodes;
    }


    /**
     *  Gets the Qual attribute of the Edge object
     *
     *@return    The Qual value
     *@since
     */
    public String getQual()
    {
        return qual;
    }


    /**
     *  Gets the Weight attribute of the Edge object
     *
     *@return    The Weight value
     *@since
     */
    public int getWeight()
    {
        return weight;
    }



    /**
     *  Description of the Method
     *
     *@param  edge  Description of the Parameter
     *@return       Description of the Returned Value
     */
    public boolean equals( Edge edge )
    {
        return this.getQual().equals( edge.getQual() );
    }


    /**
     *  Description of the Method
     *
     *@param  edge  Description of Parameter
     *@return       Description of the Returned Value
     */
    public boolean hasSameNodes( Edge edge )
    {
        if ( node1 == edge.getNodes()[0] && node2 == edge.getNodes()[1] )
        {
            return true;
        }
        if ( node1 == edge.getNodes()[1] && node2 == edge.getNodes()[0] )
        {
            return true;
        }
        return false;
    }


    /**
     *  Useful String value for debugging purposes
     *
     *@return    Description of the Returned Value
     *@since
     */
    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append( "Edge: " + qual );
        ret.append( "\nWeight: " + weight + " Needed: " + needed + "\n" );
        return ret.toString();
    }

}

