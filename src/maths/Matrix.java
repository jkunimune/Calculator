/**
 * 
 */
package maths;

import java.util.ArrayList;
import java.util.List;

import gui.Workspace;
import javafx.scene.image.Image;

/**
 * A two-dimensional grid of scalars, represented as a list of vectors.
 *
 * @author jkunimune
 */
public class Matrix extends Expression {

	private final List<Vector> rows;
	
	
	
	public Matrix(List<Vector> e) {
		rows = e;
	}
	
	
	
	public static final List<Expression> convert(List<Vector> lv) {	// casts List<Vector> to List<Expression>
		List<Expression> output = new ArrayList<Expression>();
		for (Vector v: lv)
			output.add((Expression) v);
		return output;
	}
	
	
	@Override
	public int[] shape() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	protected Expression getComponent(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public List<String> getInputs(Workspace heap) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Expression replaced(String[] oldStrs, String[] newStrs) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Expression simplified(Workspace heap) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Image toImage() {
		// TODO Auto-generated method stub
		return null;
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
	
}
