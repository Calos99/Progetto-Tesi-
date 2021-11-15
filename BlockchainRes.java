/**
* This class represents the objects returned by the <code>readChainB</code>
* method in the <code>Utility</code> class.
* It is produced after the construction of a blockchain and contains
* a reference to the data structure and statistic information about the
* construction process.
*
* @author Matteo Loporchio, 491283
*/
public class BlockchainRes {
  /**
  * This is a reference to the chain we have just constructed.
  */
  public final Blockchain chain;

  /**
  * This is an integer value representing the average index construction
  * time for the blocks of the chain.
  */
  public final long indexTime;

  /**
  * This is an integer value representing the average skip list construction
  * time for each block of the chain.
  */
  public final long skipTime;

  /**
  * This is the default constructor of the class.
  * @param chain a reference to the chain
  * @param indexTime the average MI-tree construction time
  * @param skipTime the average skip list construction time
  */
  public BlockchainRes(Blockchain chain, long indexTime, long skipTime) {
    this.chain = chain;
    this.indexTime = indexTime;
    this.skipTime = skipTime;
  }
}
