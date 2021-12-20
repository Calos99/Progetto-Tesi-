/**
* This kind of verification object is generated during the visit of a
* pruned internal node.
*
* @author Matteo Loporchio, 491283
*/
public class VPruned implements VObject {

  /**
  * This is the bounding interval of the pruned node.
  */
  private Interval MBI;

  /**
  * This is the digest of the pruned node.
  */
  private byte[] hash;

  /**
  * This constructor builds a new pruned verification object.
  * @param MBI the bounding interval of the pruned node
  * @param hash the digest of the pruned node
  */
  public VPruned(Interval MBI, byte[] hash) {
    this.MBI = MBI;
    this.hash = hash;
  }

  /**
  * Returns the bounding interval associated to the pruned verification object.
  * @return the bounding interval of the verification object
  */
  public Interval getMBI() {
    return MBI;
  }

  /**
  * Returns the digest of the pruned verification object.
  * @return the hash value of the pruned verification object
  */
  public byte[] getHash() {
    return hash;
  }
}
