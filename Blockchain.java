import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
* This class implements the blockchain data structure.
* Blocks can be accessed by means of their hash value, which must be
* known in advance.
* @author	Matteo Loporchio, 491283
*/
public class Blockchain {

  /**
  * Page capacity for the index inside each block.
  */
  private int c;

  /**
  * Skip list size for each block.
  */
  private int m;

  /**
  * The blockchain is implemented with a hash map of blocks.
  * Each block is retrieved by means of its hash value.
  */
  private Map<byte[], Block> storage;

  /**
  * This field stores the hash value of the last block.
  */
  private byte[] last;

  /**
  * This is the hash cache. It is implemented as a list that
  * contains the hash values of the last <code>maxSize</code> blocks.
  * It is used to compute the skip list for each new block.
  */
  private List<byte[]> cache;

  /**
  * This is the maximum number of hash values that can be stored in
  * the <code>cache</code> data structure.
  */
  private int maxSize;

  /**
  * This is the default constructor for the Blockchain class.
  * @param c page capacity for the index of each block
  * @param m size of the skip list for each block
  */
	public Blockchain(int c, int m) {
    this.c = c;
    this.m = m;
    this.storage = new HashMap<byte[], Block>();
    this.last = null;
    this.cache = new ArrayList<byte[]>();
    this.maxSize = ((int) Math.pow(2, m)) - 1;
	}

  /**
  * Returns the hash of the last block of the chain.
  * @return hash of the last block
  */
  public byte[] getLast() {
    return last;
  }

  /**
  * Returns a reference to the block with the given address.
  * @param ref address of the block
  * @return reference to the block
  */
  public Block getBlock(byte[] ref) {
    return storage.get(ref);
  }

  /**
  * Returns the number of currently inserted blocks
  * (i.e. the length of the blockchain).
  * @return number of inserted blocks
  */
  public int getSize() {
    return storage.size();
  }

  /**
  * Returns a reference to the cache storing the hash values of
  * the last <code>maxSize</code> blocks.
  * @return reference to the cache
  */
  public List<byte[]> getCache() {
    return cache;
  }


  /**
  * Updates the cache with a new hash value.
  * @param ref hash value of a new block
  */
  public void addToCache(byte[] ref) {
    int size = cache.size();
    // If we have reached the maximum size, we remove the last element.
    if (size == maxSize) cache.remove(size - 1);
    // We add new elements at the beginning of the list.
    cache.add(0, ref);
  }

  /**
  * Creates a new block containing the and appends it to the chain.
  * @param content the set of points to be included in the new block
  * @return the hash value of the new block
  */
  public BlockRes append(List<Integer> content) throws Exception {
    // Build the MI-tree index.
    long idxS = System.nanoTime();
    MITreeNode index = MITree.buildPacked(content, c);
		byte[] indexHash = index.getHash();
    long idxE = System.nanoTime();
    // Build the skip list index.
    long skipS = System.nanoTime();
    SkipListEntry[] skip = SkipList.buildSkip(this, m);
    byte[] skipHash = Hash.hashSkip(skip);
    long skipE = System.nanoTime();
    // Create the new block and compute its hash.
    int id = getSize()+1;
    Block b = new Block(last, indexHash, skipHash, index, skip, content, id);
    byte[] h = Hash.hashBlock(b);
    // Update the blockchain.
		last = h;
		storage.put(h, b);
    // Return the hash of the new block.
    return new BlockRes(h, idxE-idxS, skipE-skipS);
  }

}
