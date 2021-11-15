import java.util.*;

/**
* This class represents a generic entry of a skip list.
* In each entry we store information that is exploited to speed up
* the search along the entire blockchain, while guaranteeing the
* authenticity of the results.
*
* @author Matteo Loporchio, 491283
*/
public class SkipListEntry {
  /**
  * The hash of the destination block.
  */
  private byte[] ref;

  /**
  * The minimum bounding interval of all skipped blocks.
  */
  private Interval MBI;

  /**
  * The aggregate hash of all skipped blocks.
  */
  private byte[] aggHash;

  /**
  * This is the default constructor for the skip list entry.
  * It returns an entry with all fields equal to null.
  */
  public SkipListEntry() {
    this.ref = null;
    this.MBI = null;          //altrimenti dichiararlo come new Interval(0, 0); e considerarlo come caso vuoto
    this.aggHash = null;
  }

  /**
  * Returns the reference of the target block.
  * @return the hash of the target block
  */
  public byte[] getRef() {
    return ref;
  }

  /**
  * This method can be used to set the reference value for the target block.
  * @param ref the hash value of a previous block
  */
  public void setRef(byte[] ref) {
    this.ref = ref;
  }

  /**
  * Returns the minimum bounding interval of the entry.
  * @return the minimum bounding interval of the entry
  */
  public Interval getMBI() {
    return MBI;
  }

  /**
  * This method can be used to set the bounding interval of the entry.
  * @param MBI the bounding interval of all skipped blocks
  */
  public void setMBI(Interval MBI) {
    this.MBI = MBI;
  }

  /**
  * Returns the aggregate hash for the entry.
  * @return the aggregate hash of all skipped blocks
  */
  public byte[] getAggHash() {
    return aggHash;
  }

  /**
  * This method can be used to set the aggregate hash of the entry.
  * @param aggHash the aggregate hash value of all skipped blocks
  */
  public void setAggHash(byte[] aggHash) {
    this.aggHash = aggHash;
  }

}
