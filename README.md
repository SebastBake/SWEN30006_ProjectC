# SWEN30006_ProjectC
Repository for project C of the unimelb 2017, Semester 1 software engineering course.

Git Repo: https://github.com/SebastBake/SWEN30006_ProjectC

Trello: https://trello.com/b/TXyY6BX5/project-c


##Sebs strategy for navigating the maze
1. Generate a graph
2. Use graph to generate possible paths
3. Select the best path using a graph algorithm or some heuristic
4. Have the car move along the path.
5. Use new map information to update the graph or rebuild it.

* How to generate a path?
 * Only generate paths if:
  1. New blocks are uncovered and the graph is updated
  2. The car is stuck in a trap or trying to drive into a wall or something
 * A node might consist of:
  * Links to all of the other possible nodes to drive to.
  * some information about whether the node has been visited?
 * Some things to think about:
  * How to avoid the car getting stuck in loops?
  * How should the car go from one node to another eg:
   * Node occurs whenever the car needs to turn vs car heads directly towards a node.
   * When should the car:
    * Go straight ahead
	* Change direction
	* Reverse
	* Do a 3 point turn
	* Do a U turn
   * How can we implement the car, graph and map in Java.
   * What if the car get's stuck.
