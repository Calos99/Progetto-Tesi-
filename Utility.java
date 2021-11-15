import java.io.*;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
* This class contains a set of static utility methods that can be used to
* load blockchains and sets of records from files. 
*
* @author	Matteo Loporchio, 491283
*/
public final class Utility {

  /**
  * This function reads a binary file containing 2D points and returns
  * a list that contains them.
  * @param filename path of the input file
  * @return a list of points
  */
  public static List<Integer> readIntegersB(String filename) throws Exception {
    Path path = Paths.get(filename);
    byte[] content = Files.readAllBytes(path);
    IntBuffer buf = ByteBuffer.wrap(content).asIntBuffer();
    List<Integer> pts = new ArrayList<Integer>();
    while (buf.hasRemaining()) {
      int x = buf.get();
      pts.add(x);
    }
    return pts;
  }

  /**
  * This function creates a new blockchain by reading its content
  * from a given binary file. The index inside each block is built
  * with a page capacity equal to c.
  * @param filename path of the input file
  * @param c page capacity of the block index
  * @param m size of the skip list
  * @return a blockchain result object with the chain and construction times
  */
  public static BlockchainRes readChainB(String filename, int c, int m)
  throws Exception {
    long indexAvg = 0, skipAvg = 0;
    Path path = Paths.get(filename);
    byte[] content = Files.readAllBytes(path);
    ByteBuffer buf = ByteBuffer.wrap(content);
    // Create a new blockchain object.
    Blockchain chain = new Blockchain(c, m);
    // Read the number of blocks.
    int nblocks = buf.getInt();
    // Now, for each block...
    for (int i = 0; i < nblocks; i++) {
      // Read the number of records it contains.
      int nrec = buf.getInt();
      // Now collect all records and insert them into a list.
      List<Integer> records = new ArrayList<Integer>();
      for (int j = 0; j < nrec; j++) {
        int x = buf.getInt();
        records.add(x);
      }
      // Create a new block and append it to the chain.
      BlockRes res = chain.append(records);
      indexAvg += res.indexTime;
      skipAvg += res.skipTime;
    }
    return new BlockchainRes(chain, indexAvg/nblocks, skipAvg/nblocks);
  }

  /*
  public static BlockchainRes readChainLarge(String filename, int c, int m)
  throws Exception {
    long indexAvg = 0, skipAvg = 0;
    // Open the file.
    File f = new File(filename);
    InputStream is = new BufferedInputStream(new FileInputStream(f));
    // Create a new blockchain object.
    Blockchain chain = new Blockchain(c, m);
    // Read the number of blocks.
    byte[] buf = new byte[4];
    is.read(buf);
    int nblocks = ByteBuffer.wrap(buf).getInt();
    // Now, for each block...
    for (int i = 0; i < nblocks; i++) {
      System.out.println("reading block " + i);
      // Read the number of records it contains.
      is.read(buf);
      int nrec = ByteBuffer.wrap(buf).getInt();
      // Read the content of the block.
      byte[] block = new byte[nrec * 16];
      is.read(block);
      ByteBuffer blockBuf = ByteBuffer.wrap(block);
      // Now collect all records and insert them into a list.
      List<Point> records = new ArrayList<Point>();
      for (int j = 0; j < nrec; j++) {
        double x = blockBuf.getDouble(), y = blockBuf.getDouble();
        records.add(new Point(x, y));
      }
      // Create a new block and append it to the chain.
      BlockRes res = chain.append(records);
      indexAvg += res.indexTime;
      skipAvg += res.skipTime;
    }
    is.close();
    return new BlockchainRes(chain, indexAvg/nblocks, skipAvg/nblocks);
  }
  */

  /**
  * This function chops a list into sublists of a given length.
  * @param l the list to be divided
  * @param k the number of elements in each sublist
  * @return a list containing all the sublists
  */
  public static <T> List<List<T>> split(List<T> l, int k) {
    List<List<T>> parts = new ArrayList<List<T>>();
    for (int i = 0; i < l.size(); i += k) {
      //parts.add(new ArrayList<T>(l.subList(i, Math.min(l.size(), i + k))));
      parts.add(l.subList(i, Math.min(l.size(), i + k)));
    }
    return parts;
  }
}
