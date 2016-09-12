/**
 * The tool-bar thing that contains information on all built-in constants,
 * operators, and functions
 */
package gui;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.ToolBar;

/**
 * @author jkunimune
 */
public class OperationBar {

	private static final String[][] ITEMS = {
			{"\u03C0","\u03C4","e","\u03C6"},
			{"sin","csc","arcsin","arccsc","sinh","csch","arcsinh","arccsch"},
			{"cos","sec","arccos","arcsec","cosh","sech","arccosh","arcsech"},
			{"tan","cot","arctan","arccot","tanh","coth","arctanh","arccoth"},
			{"ln","log","log2","e^","10^","2^"},
			{"\u03b4/\u03b4","\u222B","Tn"},
			{},
			{"\u2192","^","\u2022","\u2022\u2022"},
			{"\u03B1","\u03B2","\u03B3","\u03B8","\u03BB","\u03C9"}
	};
	
	
	private ToolBar container;
	
	
	
	public OperationBar() {
		
		container = new ToolBar();
		container.setOrientation(Orientation.VERTICAL);
		
		for (String[] item: ITEMS) {
			if (item.length == 0) {
				container.getItems().add(new Separator());
				continue;
			}
			final SplitMenuButton smb = new SplitMenuButton();
			smb.setText(item[0]);
			for (String cmd: item)
				smb.getItems().add(new MenuItem(cmd));
			smb.setAlignment(Pos.CENTER);
			smb.setPrefWidth(75);
			container.getItems().add(smb);
		}
	}
	
	
	
	public Node getNode() {	// get all components in JavaFX format
		return container;
	}

}
