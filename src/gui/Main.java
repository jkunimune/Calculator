package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	public static final void main(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		Pane root = new StackPane();
				Scene scene = new Scene(root);
		
		HBox layout = new HBox();
		root.getChildren().add(layout);
		
		VBox toolbar = new VBox();
		layout.getChildren().add(toolbar);
		
		SplitMenuButton vars = new SplitMenuButton();
		vars.setText("tau");
		toolbar.getChildren().add(vars);
		
		MenuItem e = new MenuItem("e");
		vars.getItems().add(e);
		
		MenuItem phi = new MenuItem("phi");
		vars.getItems().add(phi);
		
		SplitMenuButton sins = new SplitMenuButton();
		sins.setText("sin");
		toolbar.getChildren().add(sins);
		
		MenuItem csc = new MenuItem("csc");
		sins.getItems().add(csc);
		
		MenuItem sinh = new MenuItem("sinh");
		sins.getItems().add(sinh);
		
		MenuItem csch = new MenuItem("csch");
		sins.getItems().add(csch);
		
		MenuItem asin = new MenuItem("arcsin");
		sins.getItems().add(asin);
		
		MenuItem acsc = new MenuItem("arccsc");
		sins.getItems().add(acsc);
		
		MenuItem asinh = new MenuItem("arcsinh");
		sins.getItems().add(asinh);
		
		MenuItem acsch = new MenuItem("arccsch");
		sins.getItems().add(acsch);
		
		VBox cmdLine = new VBox();
		layout.getChildren().add(cmdLine);
		
		TextArea ta = new TextArea();
		ta.setEditable(false);
		ta.setText("\n\n\n\n\n\n\n\n2+2=\n         4");
		cmdLine.getChildren().add(ta);
		
		TextField tf = new TextField();
		cmdLine.getChildren().add(tf);
		tf.requestFocus();
		
		Canvas disp = new Canvas(500, 50);
		GraphicsContext g = disp.getGraphicsContext2D();
		g.fillText("Hello, Worcestor!", 5, 50);
		g.fillText("Hello, Worcestor!", 15, 50);
		g.fillText("Hello, Worcestor!", 5, 40);
		g.fillText("Hello, Worcestor!", 15, 40);
		g.lineTo(420, 42);
		cmdLine.getChildren().add(disp);
		
		primaryStage.setTitle("Math-Assist");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
