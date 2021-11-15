import java.util.*;

/**
* This class contains an implementation of the algorithm that constructs
* the skip list data structure.
*
* @author Matteo Loporchio, 491283
*/
public final class SkipList {

  /**
  * This represents the default skip list size for each block.
  */
  public static final int DEFAULT_SIZE = 8;

  /**
  * This method constructs the skip list for the last block of the chain.
  * @param b the blockchain
  * @param m number of entries for the skip list
  * @return an array with the skip list entries
  */
  public static SkipListEntry[] buildSkip(Blockchain b, int m) {
    // Create a new array of m entries.
    SkipListEntry[] skip = new SkipListEntry[m];
    for (int i = 0; i < skip.length; i++) skip[i] = new SkipListEntry();
    // Check the last inserted block.
    byte[] last = b.getLast();
    // If the hash is null, the chain is currently empty and
    // there is nothing to compute for the skip list of the next block.
    if (last == null) return skip;
    // Otherwise we obtain a reference to the last block.
    Block curr = b.getBlock(last);
    Interval currRect = curr.getIndex().getMBI();
    // We update the cache with the hash of the last block.
    b.addToCache(last);
    // If the last block of the chain is also the only one we are done.
    if (curr.getPrev() == null) return skip;
    // Otherwise the first entry will point to block i-2.
    skip[0].setRef(curr.getPrev());
    skip[0].setMBI(currRect);
    skip[0].setAggHash(last);
    // Now we must fill the other entries...
    for (int i = 1; i < skip.length; i++) {
      curr = b.getBlock(skip[i-1].getRef());
      SkipListEntry[] currSkip = curr.getSkip();
      byte[] ref = currSkip[i-1].getRef();
      if (ref == null) break;
      // Set the reference.
      skip[i].setRef(ref);
      // Compute the MBI for all skipped blocks.
      skip[i].setMBI(Interval.enlarge(Arrays.asList(skip[i-1].getMBI(),
      curr.getIndex().getMBI(), currSkip[i-1].getMBI())));
      // Compute the aggregate hash.
      skip[i].setAggHash(Hash.aggregate(b.getCache(), i));
    }
    return skip;
  }
}
