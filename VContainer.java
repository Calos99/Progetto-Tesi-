import java.util.ArrayList;
import java.util.List;

/**
* This class represents a verification object that acts as a container for
* other verification objects.
*
* @author Matteo Loporchio, 491283
*/
public class VContainer implements VObject {

  /**
  * This is the list of the verification objects in the container.
  */
  private List<VObject> list;

  /**
  * This method constructs an empty container.
  */
  public VContainer() {
    list = new ArrayList<VObject>();
  }

  /**
  * Returns the current size of the container.
  * @return the size of the container
  */
  public int size() {
    return list.size();
  }

  /**
  * This method appends a new verification object to the container.
  * @param e the verification object to be inserted
  */
  public void append(VObject e) {
    list.add(e);
  }

  /**
  * This method returns the i-th element of the container.
  * @param i index of the desired element
  * @return the i-th verification object inside the container
  */
  public VObject get(int i) {
    return list.get(i);
  }
}
