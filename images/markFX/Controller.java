package com.por.ex.images.markFX;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.beans.value.*;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import scala.collection.parallel.ParIterableLike.Min;
import javafx.fxml.FXML;
import javafx.scene.image.*;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Controller implements Initializable
{	RunFX app; public void setApp(RunFX app){this.app = app;}
	MarkedImage mim;
	double brushRadius=5;
	boolean panChecked = false;
	int xDown, yDown;//Coordinates of mouse at MousePressed Event time.
	int xTempPan, yTempPan;

	@FXML public Button button;
	@FXML public Button removeMarkingButton;
	@FXML public ImageView imageView;
	@FXML public Slider brushSizeSlider;
	@FXML public TextField layerTextBox;
	@FXML public CheckBox seeMarkingsCheckBox;
	@FXML public CheckBox markUnmarkCheckBox;
	@FXML public ChoiceBox layersChoiceBox;
	@FXML public MenuItem loadImageMenuItem;
	@FXML public Slider zoomSlider;
	@FXML public CheckBox panCheckBox;
	
	final FileChooser fileChooser = new FileChooser();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{	System.out.println("Initialing Controller2");
	  	mim = new MarkedImage(new Image("file:\\C:\\test.jpg"));
	  	imageView.setFitWidth(mim.imWidth);
	  	imageView.setFitHeight(mim.imHeight);
        imageView.setImage(mim.alteredImage);
        setupBrushSizeSliderEvent();
        setupLayersChoiceBoxChange();
        setupZoomSliderEvent();
        setupImageView_MousePressed();
        setupImageView_MouseReleased();
        layersChoiceBox.getItems().add("default");
        layersChoiceBox.setValue("default");
	}
	public void actionFired()
	{ 	
		if(!layerTextBox.getText().equals("")&&!mim.markings.containsKey(layerTextBox.getText()));
		{	layersChoiceBox.getItems().add(layerTextBox.getText());
			layersChoiceBox.setValue(layerTextBox.getText());
			mim.addMarkingArray(layerTextBox.getText());
			mim.currentMarkings = mim.markings.get(layerTextBox.getText());
			mim.updateCurrentImage();
		}
	
	}
	public void removeMarkingButtonAction()
	{	System.out.println(layersChoiceBox.getValue());
		if(layersChoiceBox.getItems().size()>1)
		{	layersChoiceBox.getItems().remove((String)layersChoiceBox.getValue());
			mim.removeMarkingArray((String)layersChoiceBox.getValue());
		}
	}
	
	@FXML
    void imageView_MouseDragged(MouseEvent e) 
	{	if(panCheckBox.isSelected())
		{	mim.xPan = xTempPan - (int)e.getX() + xDown;
			mim.yPan = yTempPan - (int)e.getY() + yDown;
			mim.updateCurrentImage();
			System.out.println(mim.xPan);
		}
		else
		{	for(int ix =(int)(e.getX()-brushRadius); ix<=(int)(e.getX()+brushRadius);ix++)
			{ 	for(int iy =(int)(e.getY()-brushRadius); iy<=(int)(e.getY()+brushRadius);iy++)
				{	double dx = Math.abs(ix + 0.5 - e.getX());
					double dy = Math.abs(iy + 0.5 - e.getY());
					if(Math.sqrt(dx*dx + dy*dy)<=brushRadius)
					{	if(markUnmarkCheckBox.isSelected())
							mim.mark(ix,iy);
						else
							mim.unmark(ix, iy);
					}
				}
			}	}
    }
	
	void setupImageView_MousePressed() 
	{	imageView .addEventHandler
		(	MouseEvent.MOUSE_PRESSED, 
            new EventHandler<MouseEvent>() 
            {	public void handle(MouseEvent e) 
            	{ System.out.println("Mouse Pressed");
            		xDown = (int)e.getX(); yDown = (int)e.getY();
            		xTempPan = mim.xPan; yTempPan = mim.yPan;
            }	}
		);
	}
	
	void setupImageView_MouseReleased() 
	{	imageView.addEventHandler
		(	MouseEvent.MOUSE_RELEASED, 
            new EventHandler<MouseEvent>() 
            {	public void handle(MouseEvent e) 
            	{ System.out.println("Mouse Released");
            }	}
		);
	}
	
	
	@FXML
	void seeMarkingsCheckBoxChanged()
	{	if(seeMarkingsCheckBox.isSelected())
			imageView.setImage(mim.alteredImage);
		else
			imageView.setImage(mim.orignalImage);
	}
	void setupLayersChoiceBoxChange()
	{	layersChoiceBox.valueProperty().addListener(new ChangeListener<String>() 
		{ @Override
		    public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) 
			{	
				layersChoiceBox.setValue(newValue);
				mim.currentMarkings = mim.markings.get(newValue);
				mim.updateCurrentImage();
		    }
		});
		
	}
	
    void setupBrushSizeSliderEvent() 
	{	brushSizeSlider.valueProperty().addListener(new ChangeListener<Number>() 
		{ @Override
		    public void changed(ObservableValue<? extends Number> observable,Number oldValue, Number newValue) 
			{	System.out.println(newValue);
				brushRadius = (double) newValue;
				//outputTextArea.appendText("Slider Value Changed (newValue: " + newValue.intValue() + ")\n");
		    }
		});
	}
    @FXML
    void loadImageAction()
    {	System.out.println("loadImage");	
	    File file = fileChooser.showOpenDialog(app.primaryStage);
	    if (file != null)
			try {mim = new MarkedImage(new Image(new FileInputStream(file)));} catch (FileNotFoundException e) {e.printStackTrace();}
    	imageView.setImage(mim.alteredImage);	
    }
    
    @FXML void loadMarkingsAction()
    {	System.out.println("loadMarkings");
    	File file = fileChooser.showOpenDialog(app.primaryStage);
    
    	//mim = (MarkedImage)MarkedImage.deserialize(file);
    	MarkedImage.loadMarkings(mim, file);
    	if(mim!=null)
    	{	layersChoiceBox.getItems().clear();
    		for(String s:mim.markings.keySet())
    		{	layersChoiceBox.getItems().add(s);
    			
    		}
    	}
    	imageView.setImage(mim.alteredImage);
    	
    }
    
    @FXML void saveMarkingsAction()
    {	System.out.println("saveMarkings");
	    File file = fileChooser.showSaveDialog(app.primaryStage);
	    if (file != null)
	    {	//MarkedImage.serialize(mim, file);
	    	MarkedImage.saveMarkings(mim, file);
	    }
    }
   
    void setupZoomSliderEvent() 
   	{	zoomSlider.valueProperty().addListener(new ChangeListener<Number>() 
   		{ @Override
   		    public void changed(ObservableValue<? extends Number> observable,Number oldValue, Number newValue) 
   			{	System.out.println(newValue.intValue());
   				mim.zoom = newValue.intValue();
   				mim.updateCurrentImage();
   		    }
   		});
   	}
    @FXML void panCheckBoxAction()
    {	System.out.println("Pan Checked");
    	panChecked = panCheckBox.isSelected();
    }
	
}
