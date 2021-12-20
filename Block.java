import java.util.List;

/**
* This class represents a generic block of the chain.
*	For each block we store a reference to the previous one, along
*	with a list of records, the MI-tree index to retrieve them and
*	the skip list data structure that enables fast traversal of the chain.
* @author	Matteo Loporchio, 491283
*/
public class Block {
	/**
	*	This is an unique numeric identifier associated to the block.
	*/
	private final long id;

	/**
	 * 	This is the time elapsed for the creation of the block's tree
	 */
	private long time;

	/**
	*	This is the hash of the previous block.
	*/
	private byte[] prev;

	/**
	*	This is the hash of the root node of the MI-tree index.
	*/
	private byte[] indexHash;

	/**
	*	This is a reference to the root of the MI-tree index.
	*/
	private MITreeNode index;

	/**
	 *  This is a reference to the list of transactions included in the block
	 */
	private List<Transaction> content;

	/**
	*	This is the default constructor for the block.
	*	@param prev hash of the previous block
	* 	@param indexHash hash of the MI-tree
	*	@param indexHash hash of the root node of the MI-tree
	*	@param index reference to the MI-tree root
	*	@param content list of integers to be included in the block
	*	@param id unique identifier for the block
	*/
	public Block(byte[] indexHash, MITreeNode index, List<Transaction> content, Long id) {
		this.prev = null;						//hai rimpiazzato prev
		this.indexHash = indexHash;
		this.index = index;
		this.content = content;
		this.id = id;
	}

	/**
	*	Returns the hash value of the predecessor.
	*	@return the hash value of the previous block
	*/
	public byte[] getPrev() {
		return prev;
	}

	/**
	*	Returns the hash value of the MI-tree index for this block.
	*	@return the hash value of the MI-tree index
	*/
	public byte[] getIndexHash() {
		return indexHash;
	}

	/**
	*	Returns the root node of the MI-tree index for this block.
	*	@return a reference to the root node of the MI-tree index
	*/
	public MITreeNode getIndex() {
		return index;
	}

	/**
	*	Returns the list of transactions included in the block.
	*	@return a reference to the list of transactions included in the block
	*/
	public List<Transaction> getContent() {
		return content;
	}

	/**
	 * Return the id of the block.
	 * @return id
	 */
	public long getID(){
		return this.id;
	}

	/**
	 * Return the creation time of the block's tree
	 * @return time
	 */
	public long getTime(){
		return this.time;
	}

	/**
	 * Set the creation time of the block's tree
	 */
	public void setTime(long time){
		this.time = time;
	}

	/**
	 * Add the transaction t in the transactions list
	 * @param t
	 */
	public void AddTransaction(Transaction t){
		this.content.add(t);
	}
}
