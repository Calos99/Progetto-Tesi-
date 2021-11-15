import java.util.List;

/**
* This class represents an interval which
*	is characterized by its minimum and maximum values.
*
* @author Matteo Loporchio, 491283
*/
public class Interval implements Comparable<Interval> {

	public final int min;			

	public final int max;

	public Interval(int min, int max) {
		this.min = min;
		this.max = max;
	}

	/**
	*	Comparison function for intervals.
	*	To compare two intervals, we just compare their values.
	* @param i interval to be compared with the current one
	* @return an integer
	*/
	public int compareTo(Interval i) {
		int s = Integer.compare(this.min, i.min);
		return ((s != 0) ? s : Integer.compare(this.max, i.max));
	}

	/**
	*	Prints a human-readable string representing the interval.
	* @return a string representing the interval
	*/
	public String toString() {
		return "[" + this.min + "," + this.max + "]";
	}

	/**
	 * returns a MBI from a list of integers
	 * @param lst
	 * @return an Interval object
	 */

	public static Interval MBI(List<Integer> lst){
		int minimum = lst.get(0);
		int maximum = 0;

		for (Integer integer : lst) {
			if(integer < minimum)
				minimum = integer;
			else{
				if(integer > maximum)
					maximum = integer;
			}	
		}

		return new Interval(minimum, maximum);
	}

	/**
	 * returns a MBI that includes all the ones who are in intervals
	 * @param intervals
	 * @return a Interval object
	 */
	public static Interval enlarge(List<Interval> intervals){
		int minimum = intervals.get(0).min;
		int maximum = intervals.get(0).max;

		for (Interval interval : intervals) {
			if(interval.min < minimum)
				minimum = interval.min;
			else{
				if(interval.max > maximum)
					maximum = interval.max;
			}
		}

		return new Interval(minimum, maximum);
	}

	/**
	 * check if the value is in the range
	 * @param query
	 * @param p
	 * @return a boolean
	 */
	public static boolean contains(Interval query, int p){
		return p >= query.min && p<= query.max;
	}

	public static boolean intersect(Interval T_MBI, Interval query){

		return contains(T_MBI, query.min) || contains(T_MBI, query.max);
	}
}
