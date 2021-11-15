import java.io.*;
import java.nio.*;
import java.security.*;
import java.util.*;

/**
*	This class contains several methods to compute the hash values
*	of entities (e.g. intervals) and data structures
*	(e.g. blocks, MI-trees, skip lists).
*	@author Matteo Loporchio, 491283
*/
public final class Hash {

	/**
	*	This function computes the hash value of an integer.
	*	@param p integer to be hashed
	*	@return an array of bytes representing the hash value
	*/
	public static byte[] hashInteger(int p) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] buf = ByteBuffer.allocate(8)
			.putInt(p).array();
			return digest.digest(buf);
		}
		catch (Exception e) {
			System.err.println("Something went wrong while hashing a point!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	*	This function computes the hash value of a list of integer values.
	*	@param pts the list of integers to be hashed
	*	@return the hash value of all values in the list
	*/
	public static byte[] hashIntegers(List<Integer> pts) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			ByteBuffer bb = ByteBuffer.allocate(8 * pts.size());
			for (int p : pts) bb.putInt(p);
			return digest.digest(bb.array());
		}
		catch (Exception e) {
			System.err.println("Something went wrong while hashing points!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	*	This function computes the hash value of an interval.
	*	@param r an interval
	*	@return the digest of the input interval
	*/
	public static byte[] hashInterval(Interval i) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] buf = ByteBuffer.allocate(32)
			.putInt(i.min).putInt(i.max).array();
			return digest.digest(buf);
		}
		catch (Exception e) {
			System.err.println("Something went wrong while hashing an interval!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	*	This function computes the hash value of a node,
	*	starting from its children.
	*	@param children the list of child nodes
	*	@return the digest of the node
	*/
	public static byte[] hashNode(List<MITreeNode> children) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			ByteArrayOutputStream strm = new ByteArrayOutputStream();
			// Read the content of all children.
			for (MITreeNode c : children) {
				Interval i = c.getMBI();
				byte[] rbuf = ByteBuffer.allocate(32).putInt(i.min)
				.putInt(i.max).array();
				strm.write(rbuf);
				strm.write(c.getHash());
			}
			return digest.digest(strm.toByteArray());
		}
		catch (Exception e) {
			System.err.println("Something went wrong while hashing a node!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	*	This function computes the hash value of a single skip list entry.
	*	@param entry a reference to the entry
	*	@return the digest of the entry
	*/
	public static byte[] hashSkipEntry(SkipListEntry entry) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			ByteArrayOutputStream strm = new ByteArrayOutputStream();
			Interval i = entry.getMBI();
			byte[] rbuf = ByteBuffer.allocate(32).putInt(i.min).
			putInt(i.max).array();
			byte[] ref = entry.getRef(), aggHash = entry.getAggHash();
			strm.write(((ref != null) ? ref : new byte[1]));
			strm.write(rbuf);
			strm.write(((aggHash != null) ? aggHash : new byte[1]));
			return digest.digest(strm.toByteArray());
		}
		catch (Exception e) {
			System.err.println("Something went wrong while hashing a skip list!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	*	This function computes the hash value of a skip list.
	*	@param skip a reference to the skip list
	*	@return the digest of the skip list
	*/
	public static byte[] hashSkip(SkipListEntry[] skip) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			ByteArrayOutputStream strm = new ByteArrayOutputStream();
			for (int i = 0; i < skip.length; i++) strm.write(hashSkipEntry(skip[i]));
			return digest.digest(strm.toByteArray());
		}
		catch (Exception e) {
			System.err.println("Something went wrong while hashing a skip list!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	*	This function computes the hash value of a block.
	*	The hash value of a block is obtained by concatenating:
	*		(1) the hash of the previous block
	*		(2) the hash of the root of the MI-tree index
	*		(3) the hash of the skip list
	*		(4) the hash value of all values in the block
	*	@param b a block of the chain
	*	@return the hash value of the block
	*/
	public static byte[] hashBlock(Block b) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			ByteArrayOutputStream strm = new ByteArrayOutputStream();
			strm.write((b.getPrev() != null) ? b.getPrev() : new byte[32]);
			strm.write((b.getIndexHash() != null) ? b.getIndexHash() : new byte[32]);
			strm.write((b.getSkipHash() != null) ? b.getSkipHash() : new byte[32]);
			strm.write(hashIntegers(b.getContent()));
			return digest.digest(strm.toByteArray());
		}
		catch (Exception e) {
			System.err.println("Something went wrong while hashing a block!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	*	This function returns the digest of the concatenation of all
	*	the hash values in the input list.
	*	@param hashes list of hash values
	*	@return the hash value of the concatenation of all values in the list
	*/
	public static byte[] aggregate(List<byte[]> hashes) {
		// If the list contains only one hash value, return it.
		if (hashes.size() == 1) return hashes.get(0);
		// Otherwise we compute the hash of their concatenation.
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			ByteArrayOutputStream strm = new ByteArrayOutputStream();
			for (byte[] h : hashes) strm.write(h);
			return digest.digest(strm.toByteArray());
		}
		catch (Exception e) {
			System.err.println("Something went wrong while aggregating hashes!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	*	Given a list of hash values and an integer i, this function
	*	returns the digest of the concatenation of the first 2^i hash values.
	*	@param hashes list of hash values
	*	@param i an integer value
	*	@return the hash value of the concatenation of the first 2^i values
	*/
	public static byte[] aggregate(List<byte[]> hashes, int i) {
		int j = ((int) Math.pow(2, i)) - 1;
		if (j > (hashes.size() - 1)) return null;
		return aggregate(hashes.subList(0, j));
	}

	/**
	*	This function can be used to reconstruct the hash of a MI-tree node
	*	starting from the list of minimum bounding intervals
	*	and the list of digests of its children.
	*	@param rects the list of intervals of the children
	* 	@param hashes the list of hash values of the children
	*	@return the digest of the node. We return a null value if the input
	*	lists do not have the same length.
	*/
	public static byte[] reconstruct(List<Interval> intervals, List<byte[]> hashes)
	{
		// It is required that the two lists have the same length.
		if (intervals.size() != hashes.size()) return null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			ByteArrayOutputStream strm = new ByteArrayOutputStream();
			for (int i = 0; i < intervals.size(); i++) {
				Interval in = intervals.get(i);
				byte[] rbuf = ByteBuffer.allocate(32).putInt(in.min).
				putInt(in.max).array();
				strm.write(rbuf);
				strm.write(hashes.get(i));
			}
			return digest.digest(strm.toByteArray());
		}
		catch (Exception e) {
			System.err.println("Something went wrong while hashing a node!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	* Converts an array of bytes to a human-readable hexadecimal string.
	*	@param hash array of bytes
	*	@return a readable string representing the content of the array
	*/
	public static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
