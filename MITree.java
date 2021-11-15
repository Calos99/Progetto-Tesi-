import java.util.*;

/**
* In this class we have implemented a static method that can be used
* to build the MI-tree index from a given set of integer values.
* The method is based on the packed MI-tree algorithm.
*
* @author	Matteo Loporchio, 491283
*/
public final class MITree{

  /**
  * This value is the default page capacity for the MI-tree.
  */
  public static final int DEFAULT_CAPACITY = 20;

  /**
  * Constructs a MI-tree index from a given list of integers with
  * the packed algorithm. The nodes of the tree have a fixed capacity.
  * @param pts list of integers
  * @param c page capacity
  * @return the root node of the constructed MI-tree index
  */
  public static MITreeNode buildPacked(List<Integer> pts, int c) {
    List<MITreeNode> current = new ArrayList<MITreeNode>();
    // Sort the values in ascending order.
    Collections.sort(pts);
    // Split the records into chunks of size c.
    // Then create a leaf node for each chunk.
    List<List<Integer>> leaves = Utility.split(pts, c);
    for (List<Integer> ck : leaves) current.add(MITreeNode.nodeFromIntegers(ck));
    // Start merging.
    while (current.size() > 1) {
      // Divide the list of current nodes into chunks.
      List<List<MITreeNode>> chunks = Utility.split(current, c);
      // For each chunk, merge all its nodes and create a new one.
      List<MITreeNode> merged = new ArrayList<MITreeNode>();
      for (List<MITreeNode> ck : chunks)
        merged.add(MITreeNode.nodeFromChildren(ck));
      // These nodes become the new working set.
      current = merged;
    }
    // Return the root of the MI-tree, i.e. the only node left.
    return current.get(0);
  }

}
