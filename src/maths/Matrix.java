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

	private List<Vector> columns;
	
	
	
	public Matrix(List<Vector> e) {
		super(Operator.VECTOR, convert(e));
		columns = e;
	}
	
	
	
	public static final List<Expression> convert(List<Vector> lv) {
		List<Expression> output = new ArrayList<Expression>();
		for (Vector v: lv)
			output.add((Expression) v);
		return output;
	}

}
