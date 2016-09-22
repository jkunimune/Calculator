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

import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import maths.Expression;

/**
 * A mapping of Strings to Expressions that remembers all stored data.
 *
 * @author jkunimune
 */
public class Workspace {

	HashMap<String, Expression> heap;	// the mapping from Strings to Expressions
	ObservableList<String> varNames;	// the ordered list of Strings
	TableView<String> table;	// the nice display of all Strings and Expressions
	
	
	
	public Workspace() {
		heap = new HashMap<String, Expression>();
		varNames = FXCollections.observableList(new ArrayList<String>());
		table = new TableView<String>(varNames);
		
		final TableColumn<String, String> names =
				new TableColumn<String, String>("Variable");
		names.setCellValueFactory(
				new Callback<CellDataFeatures<String, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<String, String> p) {
				return new SimpleStringProperty(p.getValue());
			}
		});
		final TableColumn<String, String> values =
				new TableColumn<String, String>("Value");
		values.setCellValueFactory(
				new Callback<CellDataFeatures<String, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<String, String> p) {
				return new SimpleStringProperty(heap.get(p.getValue()).toString());
			}
		});
		values.setPrefWidth(150);
		
		table.getColumns().add(names);
		table.getColumns().add(values);
	}
	
	
	
	public Node getNode() {
		return table;
	}
	
	
	public boolean hasVariable(String s) {
		return heap.containsKey(s);
	}
	
	
	public Expression get(String s) {
		return heap.get(s);
	}
	
	
	public void put(String s, Expression e) {
		if (!heap.containsKey(s))
			varNames.add(s);
		heap.put(s, e);
	}
	
	
	public void remove(String s) {
		heap.remove(s);
		varNames.remove(s);
	}
	
	
	public HashMap<String, Expression> getHash() {
		return heap;
	}

}
