/**
* This class is used as a wrapper for the results of the <code>append</code>
* method in the <code>Blockchain</class>.
*
* @author Matteo Loporchio, 491283
*/
public class BlockRes {
  /**
  * This is the hash of the newly constructed block.
  */
  public final byte[] hash;

  /**
  * This is the construction time for the MI-tree index of the block.
  */
  public final long indexTime;

  /**
  * This is the construction time for the skip list of the block.
  */
  public final long skipTime;

  /**
  * This is the default constructor for the <code>BlockRes</class>.
  * @param hash hash value of the block
  * @param indexTime construction time for the MI-tree index
  * @param skipTime construction time for the skip list index
  */
  public BlockRes(byte[] hash, long indexTime, long skipTime) {
    this.hash = hash;
    this.indexTime = indexTime;
    this.skipTime = skipTime;
  }
}
