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
import java.util.List;
import java.util.Map;

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
import maths.Variable;

/**
 * A mapping of Strings to Expressions that remembers all stored data.
 *
 * @author jkunimune
 */
public class Workspace {

	public static final int PREF_WIDTH1 = 80;
	public static final int PREF_WIDTH2 = 220;
	public static final int PREF_HEIGHT = 200;
	
	
	private Map<String, Expression> outputs;	// the mapping from Strings to Expressions
	private Map<String, String[]> inputs;	// the inputs to each variable
	private ObservableList<String> keys;	// the ordered list of Strings
	private TableView<String> table;	// the nice display of all Strings and Expressions
	
	
	
	public Workspace() {
		outputs = new HashMap<String, Expression>();
		inputs = new HashMap<String, String[]>();
		keys = FXCollections.observableList(new ArrayList<String>());
		table = new TableView<String>(keys);
		
		final TableColumn<String, String> names =
				new TableColumn<String, String>("Variable");
		names.setCellValueFactory(
				new Callback<CellDataFeatures<String, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<String, String> p) {
				return new SimpleStringProperty(getCall(p.getValue()));
			}
		});
		names.setPrefWidth(PREF_WIDTH1);
		final TableColumn<String, String> values =
				new TableColumn<String, String>("Value");
		values.setCellValueFactory(
				new Callback<CellDataFeatures<String, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<String, String> p) {
				return new SimpleStringProperty(get(p.getValue()).toString());
			}
		});
		values.setPrefWidth(PREF_WIDTH2);
		
		table.getColumns().add(names);
		table.getColumns().add(values);
		table.setPrefHeight(PREF_HEIGHT);
	}
	
	
	private Workspace(Workspace w) {	// clone another workspace
		outputs = new HashMap<String, Expression>();
		inputs = new HashMap<String, String[]>();
		keys = FXCollections.observableList(new ArrayList<String>());
		
		for (String key: w.keys)	// workspaces instantiated this way have no Node
			this.put(key, w.getArgs(key), w.get(key));
	}
	
	
	
	public Node getNode() {
		return table;
	}
	
	
	public boolean containsKey(String name) {
		return outputs.containsKey(name);
	}
	
	
	public Expression get(String name) {
		return outputs.get(name);
	}
	
	
	public String[] getArgs(String name) {
		return inputs.get(name);
	}
	
	
	public String getCall(String name) {
		if (inputs.get(name) != null && inputs.get(name).length > 0) {
			String res = name+"(";
			for (String arg: inputs.get(name))
				res += arg+", ";
			return res.substring(0,res.length()-2)+")";
		}
		else
			return name;
	}
	
	
	public void put(String name, Expression val) {	// store a variable
		put(name, null, val);
	}
	
	
	public void put(String name, String[] args, Expression val) {
		if (val instanceof Variable && ((Variable) val).toString().equals(name))
			return; // this is a tricky exception where a variables becomes itself
		
		if (outputs.containsKey(name))
			keys.remove(name);// remove anything with the same name
		outputs.put(name, val);	// store it in the hash map with the name
		inputs.put(name, args);
		keys.add(name);	// store it in the visible list with the full declaration
	}
	
	
	public void remove(String name) {
		outputs.remove(name);
		inputs.remove(name);
		keys.remove(name);
	}
	
	
	public Workspace localize(List<String> toRemove) { // clone this and remove some variables
		if (toRemove == null || toRemove.isEmpty())
			return this;
		
		Workspace local = new Workspace(this);
		for (String var: toRemove)
			local.remove(var);
		return local;
	}
	
	
	public Workspace localize(String[] newVars, Expression[] args) { // clone this and add some new variables
		if (newVars == null || newVars.length == 0)
			return this;
		
		Workspace local = new Workspace(this);
		for (int i = 0; i < newVars.length; i ++)
			local.put(newVars[i], null, args[i]);
		return local;
	}
	
	
	public Workspace clone() {
		return new Workspace(this);
	}
	
	
	@Override
	public String toString() {
		if (keys.isEmpty())	return "{}";
		String str = "{";
		for (String key: keys)
			str += getCall(key)+":"+get(key)+"; ";
		return str.substring(0, str.length()-2)+"}";
	}

}
