/**
* This class represents a generic pair of elements.
*
* @author Matteo Loporchio, 491283
*/

public class Pair<T, U> {
  /**
  * The first element of the pair.
  */
  private T first;

  /**
  * The second element of the pair.
  */
  private U second;

  /**
  * Constructs a new pair given its two elements.
  * @param first the first element of the pair
  * @param second the second element of the pair
  */
  public Pair(T first, U second) {
    this.first = first;
    this.second = second;
  }

  /**
  * Returns the first element of the current pair.
  * @return the value of the first element
  */
  public T getFirst() {
    return first;
  }

  /**
  * Returns the second element of the current pair.
  * @return the value of the second element
  */
  public U getSecond() {
    return second;
  }

}
