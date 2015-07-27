package com.por.ex.images.markFX;


import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class RunFX extends Application{
	private Controller controller;
	public Stage primaryStage;

	public static void main(String[] args) 
	{	launch(args); //Calls start
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Form2.fxml"));
        Parent root = (Parent)fxmlLoader.load();
		this.primaryStage = primaryStage;
        controller = (Controller) fxmlLoader.getController();
        controller.setApp(this);
        
        
		primaryStage.setTitle("Image Mark JavaFX");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();	
		
		MarkedImage mim = new MarkedImage(new Image("file:\\C:\\test.jpg"));
		/*MarkedImage mim2;
		try
		{	MarkedImage.serialize(mim, "C:\\Users\\Padraig\\Documents\\85_eclipse_Scala_4\\com.porourke.examples\\src\\com\\porourke\\examples\\images\\javaFX\\delete.mim");
			
		}catch(Exception e){e.printStackTrace();}
		try
		{	mim2 = (MarkedImage) MarkedImage.deserialize("C:\\delete.mim");
		}catch(Exception e){e.printStackTrace();}
		*/
	}

}
