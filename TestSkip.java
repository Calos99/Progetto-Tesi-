import java.util.*;

/**
* With this class we test the construction of the skip list index.
* We read a blockchain from a binary file and for each block we construct
* a skip list. We measure the average construction time in nanoseconds.
*
* In order to run it, the following parameters are required:
*
*   <filename> <page capacity> <skip size>
*
* @author Matteo Loporchio, 491283
*/
public class TestSkip {
  public static void main(String[] args) {
    try {
      // Read the file name.
      String filename = args[0];
      // Read the page capacity and size of the skip list from input.
      int c = Integer.parseInt(args[1]),
      m = Integer.parseInt(args[2]);
      // Build the blockchain.
      BlockchainRes res = Utility.readChainB(filename, c, m);
      // Print the results.
      System.out.println("Blocks read: " + res.chain.getSize());
      System.out.println("Index capacity: " + c);
      System.out.println("Skip list size: " + m);
      System.out.println("Avg index construction time: "
      + res.indexTime + " ns");
      System.out.println("Avg skip construction time: "
      + res.skipTime + " ns");
    }
    catch (Exception e) {
      System.err.println("Something went wrong!");
      e.printStackTrace();
    }
  }
}
