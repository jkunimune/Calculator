/**
 * MIT License
 *
 * Copyright (c) 2016 Justin Kunimune
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.ToolBar;

/**
 * The tool-bar thing that contains information on all built-in constants,
 * operators, and functions
 *
 * @author jkunimune
 */
public class OperationBar {

	private static final String[][] ITEMS = {
			{"\u03C0","\u03C4","e","\u03C6"},
			{"sin","csc","arcsin","arccsc","sinh","csch","arcsinh","arccsch"},
			{"cos","sec","arccos","arcsec","cosh","sech","arccosh","arcsech"},
			{"tan","cot","arctan","arccot","tanh","coth","arctanh","arccoth"},
			{"ln","log","log2","e^","10^","2^"},
			{"\u00d7"},
			{"\u03b4/\u03b4","\u222B","Tn"},
			{},
			{"\u2192","^","\u2022","\u2022\u2022"},
			{"\u03B1","\u03B2","\u03B3","\u03B8","\u03BB","\u03C9"}
	};
	
	
	private CommandLine cmd;
	private ToolBar container;
	
	
	
	public OperationBar(CommandLine c) {
		cmd = c;
		
		final EventHandler<ActionEvent> handler =
				new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				handleAction(event);
			}
		};
		
		container = new ToolBar();
		container.setOrientation(Orientation.VERTICAL);
		
		for (String[] item: ITEMS) {
			if (item.length == 0) {
				container.getItems().add(new Separator());
				continue;
			}
			final SplitMenuButton smb = new SplitMenuButton();
			smb.setText(item[0]);
			for (String cmd: item) {
				MenuItem mi = new MenuItem(cmd);
				mi.setOnAction(handler);
				smb.getItems().add(mi);
			}
			smb.setOnAction(handler);
			smb.setAlignment(Pos.CENTER);
			smb.setPrefWidth(75);
			container.getItems().add(smb);
		}
	}
	
	
	
	public Node getNode() {	// get all components in JavaFX format
		return container;
	}
	
	
	private void buttonRespond(String button) {
		cmd.typeText(button);
	}
	
	
	private void handleAction(ActionEvent event) {
		if (event.getSource() instanceof MenuItem) {
			final MenuItem source = (MenuItem) event.getSource();
			buttonRespond(source.getText());
		}
		else if (event.getSource() instanceof SplitMenuButton) {
			final SplitMenuButton source =
					(SplitMenuButton) event.getSource();
			buttonRespond(source.getText());
		}
		cmd.requestFocus();
	}

}
