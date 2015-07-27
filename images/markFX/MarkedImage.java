package com.por.ex.images.markFX;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class MarkedImage implements Serializable
{	transient Image orignalImage; 			transient PixelReader oimRead; 
	transient WritableImage alteredImage;	transient PixelReader aimRead; 		transient PixelWriter aimWriter; 
											transient int imWidth, imHeight;
	Map<String,boolean[][]> markings;		transient boolean[][] currentMarkings;
	transient int zoom = 1; 				transient int	xPan = 0;			transient int yPan = 0;
	
	public MarkedImage(Image im)
	{	orignalImage = im; oimRead = im.getPixelReader(); imWidth = (int)im.getWidth(); imHeight = (int)im.getHeight();
		alteredImage = new WritableImage(imWidth, imHeight);
		aimRead = alteredImage.getPixelReader();	aimWriter = alteredImage.getPixelWriter();
		markings = new HashMap<String,boolean[][]>();
		addMarkingArray("default");
		currentMarkings = markings.get("default");
		for (int y = 0; y < imHeight; y++)
		{ 	for (int x = 0; x < imWidth; x++)
			{	Color color = oimRead.getColor(x, y);
		    	aimWriter.setColor(x, y, color);
			}
		}
	}
	public MarkedImage(Image im, Map<String, boolean[][]> markings)
	{	orignalImage = im; oimRead = im.getPixelReader(); imWidth = (int)im.getWidth(); imHeight = (int)im.getHeight();
		alteredImage = new WritableImage(imWidth, imHeight);
		aimRead = alteredImage.getPixelReader();	aimWriter = alteredImage.getPixelWriter();
		markings = markings;
		for (int y = 0; y < imHeight; y++)
		{ 	for (int x = 0; x < imWidth; x++)
			{	Color color = oimRead.getColor(x, y);
		    	aimWriter.setColor(x, y, color);
			}
		}
	}
	
	public void addMarkingArray(String name)
	{ markings.put(name, new boolean[imWidth][imHeight]);	
	}
	public void removeMarkingArray(String name)
	{ markings.remove(name);
	}
	public boolean setMarkings(String name)
	{	if(markings.containsKey(name))
		{ currentMarkings = markings.get(name);
			return true;
		}
		else
			return false;
	}
	public void updateCurrentImage()
	{	for (int y = 0; y < imHeight; y++)
		{ 	for (int x = 0; x < imWidth; x++)
			{	if(0<=x/zoom + xPan && x/zoom + xPan<imWidth && 0<=y/zoom + yPan && y/zoom + yPan < imHeight)
				{	Color color = oimRead.getColor(x/zoom + xPan, y/zoom + yPan);
					if(currentMarkings!=null&&currentMarkings[x/zoom+xPan][y/zoom+yPan])
						aimWriter.setColor(x, y, invertColor(color));
					else
						aimWriter.setColor(x, y, color);
				}
				else
				{		aimWriter.setColor(x, y, Color.GRAY);
					
				}
			}
		}	
	}
	public static Color getContrastColor(Color color) 
	{	double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		return y >= 128 ? Color.BLACK : Color.WHITE;
	}

	public Color invertColor(Color c)
	{	//System.out.println(c.getRed()+","+ (1.0-c.getRed()));
		return new Color(1.0-c.getRed(), 1.0-c.getGreen(), 1.0-c.getBlue(),1.0);
	}
	
	public void mark(int x, int y)
	{	if(-1<x&&x<imWidth&&-1<y&&y<imHeight)
		{ 	currentMarkings[x/zoom+xPan][y/zoom+yPan] = true;
			Color color = oimRead.getColor(x/zoom+xPan, y/zoom+yPan);
			aimWriter.setColor(x,y,invertColor(color));
		}
	
	}
	
	public void unmark(int x, int y)
	{	if(-1<x&&x<imWidth&&-1<y&&y<imHeight)
		{ 	currentMarkings[x/zoom+xPan][y/zoom+yPan] = false;
			Color color = oimRead.getColor(x/zoom+xPan, y/zoom+yPan);
			aimWriter.setColor(x,y,color);
		}
	}
	public static void saveMarkings(MarkedImage mi, File file)
	{	try
		{	FileOutputStream f = new FileOutputStream(file);
	    	ObjectOutputStream s = new ObjectOutputStream(f);
	    	s.writeObject(mi.markings);
	    	s.close();
		}catch(Exception e){e.printStackTrace();}
	}
	public static void loadMarkings(MarkedImage mi, File file)
	{	try
		{	FileInputStream f = new FileInputStream(file);
	    	ObjectInputStream s = new ObjectInputStream(f);
	    	mi.markings = (HashMap<String, boolean[][]>) s.readObject();
	    	s.close();
		}catch(Exception e){e.printStackTrace();}
	}
	
	    // deserialize to Object from given file
	    public static Object deserialize(File file) 
	    {	try
	    	{	FileInputStream fis = new FileInputStream(file);
	        	ObjectInputStream ois = new ObjectInputStream(fis);
	        	Object obj = ois.readObject();
	        	ois.close();
	        	return obj;
	    	}catch(Exception e){e.printStackTrace();}
	    	return null;
	    }
	 
	    // serialize the given object and save it to file
	    public static void serialize(Object obj, File file) 
	    {	try
	    	{	FileOutputStream fos = new FileOutputStream(file);
		        ObjectOutputStream oos = new ObjectOutputStream(fos);
		        oos.writeObject(obj);
		 
		        fos.close();
	    	}catch(Exception e){e.printStackTrace();}
	    }
	    
	    public Image getOrignalImage() {
			return orignalImage;
		}
		public void setOrignalImage(Image orignalImage) {
			this.orignalImage = orignalImage;
		}
		public Map<String, boolean[][]> getMarkings() {
			return markings;
		}
		public void setMarkings(Map<String, boolean[][]> markings) {
			this.markings = markings;
		}
}
