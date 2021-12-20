import java.util.ArrayList;
import java.util.List;

/**
* This kind of verification object is generated during a visit of
* a MI-tree leaf node.
*
* @author Matteo Loporchio, 491283
*/
public class VLeaf implements VObject {

  /**
  * This is the set of records included in the verification object.
  */
  private List<Transaction> records;

  /**
  * This method constructs a new leaf verification object from a given
  * set of records.
  * @param records the list of records
  */
  public VLeaf(List<Transaction> records) {
    this.records = new ArrayList<Transaction>();
    this.records.addAll(records);
  }

  /**
  * This method returns the list of records associated to the verification
  * object.
  * @return the list of points of the verification object
  */
  public List<Transaction>getRecords() {
    return records;
  }
}
