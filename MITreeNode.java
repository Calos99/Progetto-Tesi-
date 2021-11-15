import java.lang.reflect.Array;
import java.net.InterfaceAddress;
import java.util.*;

/**
*	This class implements a generic MI-tree node, which can be
*	either a leaf or an internal node. Leaf nodes contain a set of records
*	while internal nodes contain a list of children.
*
* @author	Matteo Loporchio, 491283
*/
public class MITreeNode implements Comparable<MITreeNode> {
	/**
	*	This is the minimum bounding interval of the node.
	*	It is obtained as the minimum bounding union of the intervals of
	*	the children, or, in the case of a leaf, as the bounding interval
	*	of all points contained in the current node.
	*/
	private Interval MBI;

	/**
	* This is the digest of the node.
	*/
	private byte[] hash;

	/**
	*	This is a reference to the list of integers contained in the node.
	*/
	private List<Integer> data;

	/**
	*	This is the list of children for the current node.
	*/
	private List<MITreeNode> children;

	/**
	*	This is the default constructor for the MITreeNode class.
	*	@param MBI the minimum bounding interval of the node
	*	@param hash the digest of the node
	* 	@param data the list of integers contained in the node (if it is a leaf)
	*	@param children the list of child nodes for the current node
	*	(in the case of an internal node)
	*/
	public MITreeNode(Interval MBI, byte[] hash, List<Integer> data,
	List<MITreeNode> children) {
		this.MBI = MBI;
		this.hash = hash;
		this.data = data;
		this.children = children;
	}

	/**
	*	Returns the minimum bouding interval of the node.
	*	@return MBI of the node
	*/
	public Interval getMBI() {
		return MBI;
	}

	/**
	*	Returns the hash value of the node.
	*	@return hash value of the node
	*/
	public byte[] getHash() {
		return hash;
	}

	/**
	*	Returns the list of records contained in the node.
	* @return list of integers in the node
	*/
	public List<Integer> getData() {
		return data;
	}

	/**
	*	Returns the list of children of the node.
	*	@return list of children of the node
	*/
	public List<MITreeNode> getChildren() {
		return children;
	}

	/**
	*	Determines whether the current node is a leaf or an internal one.
	*	@return true if and only if the current node is a leaf
	*/
	public boolean isLeaf() {
		return (data != null && children == null);
	}

	/**
	*	Comparison function.
	*	@param n node to be compared with the current one
	*	@return an integer
	*/
	public int compareTo(MITreeNode n) {
		return MBI.compareTo(n.MBI);
	}

	/**
	*	Creates a new leaf node from a list of integers.
	*	@param pts the list of points
	*	@return a leaf MI-tree node
	*/
	public static MITreeNode nodeFromIntegers(List<Integer> pts) {
		return new MITreeNode(Interval.MBI(pts), Hash.hashIntegers(pts),
		pts, null);
	}

	/**
	*	Creates a new internal node from a list of child nodes.
	*	The new node is the parent of all these children.
	*	@param children the list of child nodes
	*	@return an internal MI-tree node
	*/
	public static MITreeNode nodeFromChildren(List<MITreeNode> children) {
		// Take all the bounding intervals of the children.
		List<Interval> intervals = new ArrayList<Interval>();
		children.forEach((c) -> intervals.add(c.getMBI()));
		return new MITreeNode(Interval.enlarge(intervals), Hash.hashNode(children),
		null, children);
	}

}
