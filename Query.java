
import java.util.*;

/**
* This class contains the implementation of the spatial query and verification
* algorithms, together with the lookup method to retrieve matching records
* along the entire chain.
*
* @author Matteo Loporchio, 491283
*/
public final class Query {

  /**
  * Given a list of integers and an interval, this method
  * returns all values inside the interval.
  * @param pts list of integers
  * @param query interval
  * @return list of values inside the interval
  */
  public static List<Long> filter(List<Long> pts, Interval query) {
    List<Long> result = new ArrayList<Long>();
    pts.forEach((p) -> {
      if (Interval.contains(query, p)) result.add(p);
    });
    return result;
  }

  /**
  * This method can be used to query the MI-tree index in order
  * to retrieve all values that belong to the query interval.
  * @param T the root of the MI-tree
  * @param query the query interval
  * @return a VO for the root
  */
  public static VObject treeSearch(MITreeNode T, Interval query) {
    // If the node is a leaf, we construct a VO with all its values.
    if (T.isLeaf()) {
      return new VLeaf(T.getData());
    }
    // Otherwise, we need to check if the MBI of the node intersects
    // the query interval.
    // If this is not the case, then the subtree will not contain any
    // interesting record.
    if (!Interval.intersect(T.getMBI(), query)) {
      return new VPruned(T.getMBI(), T.getHash());
    }
    // Otherwise, we need to explore recursively all subtrees rooted in
    // the current node.
    VContainer cont = new VContainer();
    T.getChildren().forEach((n) -> {
      VObject partial = treeSearch(n, query);
      cont.append(partial);
    });
    return cont;
  }

  /**
  * This method can be used to query the MI-tree index in order
  * to retrieve all values that belong to the query interval.
  * @param T the root of the MI-tree
  * @param query the query interval
  * @return a VO for the root
  */
  public static VObject treeSearchIt(MITreeNode T, Interval query) {
    Deque<Pair<MITreeNode, VObject>> q = new ArrayDeque<>();
    q.add(new Pair<MITreeNode, VObject>(T, null));
    VObject result = null;
    while (!q.isEmpty()) {
      Pair<MITreeNode, VObject> curr = q.remove();
      MITreeNode currNode = curr.getFirst();
      VObject parentVO = curr.getSecond();
      VObject currVO = null;
      // If the current node is a leaf, we construct a VO with all its values.
      if (currNode.isLeaf()) currVO = new VLeaf(currNode.getData());
      else {
        if (!Interval.intersect(currNode.getMBI(), query))
          currVO = new VPruned(currNode.getMBI(), currNode.getHash());
        else {
          currVO = new VContainer();
          for (MITreeNode n : currNode.getChildren())
            q.add(new Pair<MITreeNode, VObject>(n, currVO));
        }
      }
      // If the current node has a parent,
      if (parentVO != null) ((VContainer) parentVO).append(currVO);
      else result = currVO;
    }
    return result;
  }

  /**
  * The method can be used to reconstruct the root of the MI-tree index
  * from a given verification object. The output of this method is a
  * <code>VResult</code> object that contains the reconstructed result set
  * together with the bounding interval and digest of the root node.
  * @param vo a verification object
  * @return the reconstructed information
  */
  public static VResult verify(VObject vo) {
    // Reconstruct a leaf node.
    if (vo instanceof VLeaf) {
      List<Transaction> records = ((VLeaf) vo).getRecords();
      Interval MBI = Interval.MBI(records);
      byte[] h = Hash.hashTransactions(records);
      return new VResult(records, MBI, h);
    }
    // Reconstruct a pruned internal node.
    if (vo instanceof VPruned) {
      VPruned pr = ((VPruned) vo);
      return new VResult(new ArrayList<Transaction>(), pr.getMBI(), pr.getHash());
    }
    // Otherwise we must reconstruct a non-pruned internal node.
    // This node is represented by means of a VO container.
    List<Transaction> records = new ArrayList<Transaction>();
    List<Interval> rects = new ArrayList<Interval>();
    List<byte[]> hashes = new ArrayList<byte[]>();
    // Obtain the VO container.
    VContainer cont = (VContainer) vo;
    // Recursively examine each VO in the container.
    for (int i = 0; i < cont.size(); i++) {
      VResult partial = verify(cont.get(i));
      // Take all the matching records and add them to the result set.
      records.addAll(partial.getContent());
      // Collect all intervals and hashes.
      rects.add(partial.getMBI());
      hashes.add(partial.getHash());
    }
    //
    Interval u = Interval.enlarge(rects);
    byte[] hash = Hash.reconstruct(rects, hashes);
    return new VResult(records, u, hash);
  }


  /**
  * The method can be used to reconstruct the root of the MI-tree index
  * from a given verification object. The output of this method is a
  * <code>VResult</code> object that contains the reconstructed result set
  * together with the bounding interval and digest of the root node.
  * @param vo a verification object
  * @return the reconstructed information
  */
  public static VResult verifyIt(VObject vo) {
    VResult result = null;
    Stack<Pair<VObject, VObject>> s = new Stack<>();
    Map<VObject, Boolean> visited = new HashMap<>();
    Map<VObject, List<VResult>> content = new HashMap<>();
    s.push(new Pair<>(vo, null));
    visited.put(vo, false);
    while (!s.isEmpty()) {
      Pair<VObject, VObject> el = s.peek();
      VObject curr = el.getFirst();
      VObject parent = el.getSecond();
      // Check the nature of the current node.
      // If it is a container...
      if (curr instanceof VContainer) {
        // We check if this node has already been visited.
        // If this is not the case, we need to push its children
        // on the stack for exploration.
        if (!visited.get(curr)) {
          content.put(curr, new ArrayList<VResult>());
          VContainer cont = ((VContainer) curr);
          for (int i = cont.size()-1; i >= 0; i--) {
            VObject child = cont.get(i);
            s.push(new Pair<>(child, curr));
            visited.put(child, false);
          }
          visited.put(curr, true);
        }
        // On the other hand, we have to reconstruct.
        else {
          List<VResult> currContent = content.get(curr);
          List<Transaction> records = new ArrayList<Transaction>();
          List<Interval> rects = new ArrayList<Interval>();
          List<byte[]> hashes = new ArrayList<byte[]>();
          for (VResult r : currContent) {
            records.addAll(r.getContent());
            rects.add(r.getMBI());
            hashes.add(r.getHash());
          }
          Interval u = Interval.enlarge(rects);
          byte[] h = Hash.reconstruct(rects, hashes);
          VResult partial = new VResult(records, u, h);
          if (parent != null) content.get(parent).add(partial);
          else result = partial;
          s.pop();
        }
      }
      // Otherwise we have reached a leaf or pruned node.
      else {
        VResult partial = null;
        // If the node is a leaf...
        if (curr instanceof VLeaf) {
          List<Transaction> records = ((VLeaf) curr).getRecords();
          partial = new VResult(records, Interval.MBI(records),
          Hash.hashTransactions(records));
        }
        // Otherwise it is a pruned node...
        else {
          VPruned pr = ((VPruned) curr);
          partial = new VResult(new ArrayList<Transaction>(), pr.getMBI(),
          pr.getHash());
        }
        if (parent != null) content.get(parent).add(partial);
        else result = partial;
        // In both cases, we pop the node from the stack.
        s.pop();
      }
    }
    return result;
  }


  /**
  * This function implements the lookup algorithm.
  * The method traverses the entire blockchain with the help of skip lists
  * and generates a verification object for each visited block.
  * Verification objects for visited blocks are grouped together in a single
  * object which is returned as a result.
  * @param b reference to the blockchain object
  * @param query query interval
  * @return the verification object for the whole chain and the number of
  * visited blocks
  */
  /*public static Pair<VObject, Integer> lookup(Blockchain b, Interval query) {
    VContainer result = new VContainer();
    byte[] curr = b.getLast();
    int count = 0;
    while (curr != null) {
      Block currBlock = b.getBlock(curr);
      SkipListEntry[] skip = currBlock.getSkip();
      SkipListEntry e = null;
      // We inspect the current block to find matching records.
      VObject currVO = treeSearchIt(currBlock.getIndex(), query);
      // Then we examine the skip list entries in reversed order.
      for (int j = skip.length - 1; j >= 0; j--) {
        // We skip all entries with a null reference.
        if (skip[j].getRef() == null) continue;
        // The search terminates if we find an entry whose MBI
        // does not intersect the query interval.
        Interval rect = skip[j].getMBI();
        if (!Interval.intersect(rect, query)) {
          e = skip[j];
          break;
        }
      }
      // At this point, we construct the verification object for the
      // current block and choose the next one to be examined.
      List<byte[]> entryHashes = new ArrayList<>();
      if (e != null) {
        for (int j = 0; j < skip.length; j++)
          if (skip[j] != e) entryHashes.add(Hash.hashSkipEntry(skip[j]));
        curr = e.getRef();
      }
      else {
        for (int j = 0; j < skip.length; j++)
          entryHashes.add(Hash.hashSkipEntry(skip[j]));
        curr = currBlock.getPrev();
      }
      // Update the content of the VO.
      result.append(new VBlock(currVO, e, entryHashes));
      count++;
    }
    return new Pair<>(result, count);
  }*/

  /**
  * This function can be used to extract the set of returned records
  * from the verification object returned by the lookup algorithm.
  * @param vo verification object
  * @return list of records
  */
  public static List<Transaction> extract(VObject vo) {
    List<Transaction> result = new ArrayList<>();
    VContainer cont = ((VContainer) vo);
    for (int i = 0; i < cont.size(); i++) {
      VBlock vb = ((VBlock) cont.get(i));
      VResult vr = verifyIt(vb.block);
      result.addAll(vr.getContent());
    }
    return result;
  }

}
