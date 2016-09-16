/**
 * 
 */
package maths;

import java.util.ArrayList;
import java.util.List;

/**
 * A two-dimensional grid of scalars, represented as a list of vectors.
 *
 * @author jkunimune
 */
public class Matrix extends Expression {

	private List<Vector> rows;
	
	
	
	public Matrix(List<Vector> e) {
		super(Operator.NULL, convert(e));
		rows = e;
	}
	
	
	
	@Override
	public String toString() {
		String output = "[";
		for (Vector row: rows) {
			output += row.toString();
			output += "\n";
		}
		return output;
	}
	
	
	
	public static final List<Expression> convert(List<Vector> lv) {	// casts List<Vector> to List<Expression>
		List<Expression> output = new ArrayList<Expression>();
		for (Vector v: lv)
			output.add((Expression) v);
		return output;
	}

}
