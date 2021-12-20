import java.util.List;

/**
* This object contains the result set of the query, the minimum bounding
* interval of the root of the MI-tree index and the digest of the node,
* all reconstructed from the verification object.
*
* @author Matteo Loporchio, 491283
*/
public class VResult {
  /**
  * This is the result set of the query.
  */
  private List<Transaction> content;

  /**
  * This is the minimum bounding interval of the reconstructed root node.
  */
  private Interval MBI;

  /**
  * This is the hash value of the reconstructed root node.
  */
  private byte[] hash;

  /**
  * This constructor builds an empty <code>VResult</code> object.
  */
  public VResult() {
    this.content = null;
    this.MBI = null;
    this.hash = null;
  }

  /**
  * This is the default constructor for the <code>VResult</code> class.
  * @param content the result set
  * @param MBI the bounding interval of the root
  * @param hash the digest of the root
  */
  public VResult(List<Transaction> content, Interval MBI, byte[] hash) {
    this.content = content;
    this.MBI = MBI;
    this.hash = hash;
  }

  /**
  * Returns the reconstructed result set.
  * @return the reconstructed result set
  */
  public List<Transaction> getContent() {
    return content;
  }

  /**
  * Returns the reconstructed bounding interval for the MI-tree root.
  * @return the minimum bounding interval for the reconstructed root
  */
  public Interval getMBI() {
    return MBI;
  }

  /**
  * Returns the reconstructed digest for the MI-tree root.
  * @return the hash value of the reconstructed MI-tree root node
  */
  public byte[] getHash() {
    return hash;
  }
}
