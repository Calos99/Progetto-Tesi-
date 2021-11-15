import java.util.*;

/**
* This program tests the construction of a MI-tree index for
* a certain set of 2D records.
*
* In order to run it, the following parameters are required:
*
*   <filename> <capacity>
*
* @author Matteo Loporchio, 491283
*/
public class TestIndex {
  public static void main(String[] args) {
    try {
      // Read the contents from file.
      List<Integer> pts = Utility.readIntegersB(args[0]);
      // Read the number of records.
      int nrec = pts.size();
      // Read the page capacity from input.
      int c = Integer.parseInt(args[1]);
      // Time the execution of the construction algorithm.
      long tStart = System.nanoTime();
      MITreeNode T = MITree.buildPacked(pts, c);
      long tEnd = System.nanoTime();
      System.out.println("Number of records: " + nrec);
      System.out.println("Index capacity: " + c);
      System.out.println("Elapsed time: " + (tEnd-tStart) + " ns");
    }
    catch (Exception e) {
      System.err.println("Something went wrong!");
    }
  }
}
