package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

	public static final void main(String[] args) {
		launch(args);
	}
	
	
	
	private OperationBar toolbar;
	private CommandLine cmdLine;
	
	
	
	@Override
	public void start(Stage primaryStage) {
		Pane root = new StackPane();
		Scene scene = new Scene(root);
		
		HBox layout = new HBox();
		root.getChildren().add(layout);
		
		toolbar = new OperationBar();
		layout.getChildren().add(toolbar.getNode());
		
		cmdLine = new CommandLine();
		layout.getChildren().add(cmdLine.getNode());
		
		primaryStage.setTitle("Math-Assist");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
