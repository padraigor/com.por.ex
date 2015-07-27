package com.por.ex.images.swing;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageMarker extends JFrame
{	BufferedImage orignalImage;
	BufferedImage alteredImage;
	Short[][] markedmap;
	Short currentMarker;
	ImageIcon icon;
	
	ImageMarker(BufferedImage bufferedImage)
	{	super();	setDefaultCloseOperation(EXIT_ON_CLOSE);
		orignalImage = bufferedImage;
		alteredImage = deepCopy(bufferedImage);
		markedmap = new Short[orignalImage.getWidth()][orignalImage.getHeight()];
		currentMarker = 1;
		
		
		
		if(bufferedImage !=null)
		{	this.getContentPane().setLayout(new FlowLayout());
			this.icon = new ImageIcon(alteredImage);
			this.getContentPane().add(new JLabel(icon));
			this.pack();
			this.setVisible(true);
			System.out.println("Dimensions of image in memory: " + alteredImage.getWidth() +","+alteredImage.getHeight() );
			System.out.println("Dimensions of image on screen: " + icon.getIconWidth() + "," + icon.getIconHeight());
		}
		
		MouseAdapter mouseEvents = new MouseAdapter() 
		{	@Override
            public void mousePressed(MouseEvent e) 
			{	mark(e.getX(),e.getY());
                System.out.println(e.getX()+","+e.getY());
                repaint();
            }
			@Override
			public void mouseDragged(MouseEvent e)
			{	mark(e.getX(),e.getY());
	            repaint();
			}
			
			public void mark(int rawx, int rawy)
			{	int x = rawx - 14; int y = rawy - 36;
				markedmap[x][y] = currentMarker;
				alteredImage.setRGB(x, y, 1 );
			}
        };
        addMouseListener(mouseEvents);
		addMouseMotionListener(mouseEvents);
		
	}
	
	public void paint(Graphics2D g2)
	{	
		super.paint(g2);
		
	}
	
	
	public static void main(String[] args) 
	{	BufferedImage im = loadBufferedImage("C:\\test.jpg");
		increaseAlpha(im);
		ImageMarker imageMarker = new ImageMarker(im);
		
		//System.out.println(im.getMinX());
	}
	
	static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		}
	
	public static BufferedImage loadBufferedImage(String filename) 
	{	try {	return ImageIO.read(new File(filename));} 
		catch (Exception e) {System.out.println("The image was not loaded.");}
		return null;
	}
	
	public static void increaseAlpha(BufferedImage bufferedImage)
	{	for(int i=0; i<bufferedImage.getWidth(); i++)
		{	for(int j=0; j<bufferedImage.getHeight();j++)
			{	
				bufferedImage.setRGB(i, j, bufferedImage.getRGB(i, j) + 0x11000000);
			}
		}
	}


	

}
