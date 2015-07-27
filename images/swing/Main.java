package com.por.ex.images.swing;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import scala.Console;


public class Main extends JFrame{

	public static void main(String[] args) 
	{	
		
		BufferedImage im = loadBufferedImage("C:\\test.jpg");
		System.out.println(im.getRGB(0, 0));
		increaseAlpha(im);
		System.out.println(im.getRGB(0, 0));
		showBufferedImage(im);
	}
	public static BufferedImage loadBufferedImage(String filename) 
	{	try {	return ImageIO.read(new File(filename));} 
		catch (Exception e) {System.out.println("The image was not loaded.");}
		return null;
	}
	
	public static void showBufferedImage(BufferedImage bufferedImage)
	{	if(bufferedImage !=null)
		{	JFrame frame = new JFrame();
			frame.getContentPane().setLayout(new FlowLayout());
			frame.getContentPane().add(new JLabel(new ImageIcon(bufferedImage)));
			frame.pack();
			frame.setVisible(true);
		}
	}
	
	public static void increaseAlpha(BufferedImage bufferedImage)
	{	for(int i=0; i<bufferedImage.getWidth(); i++)
		{	for(int j=0; j<bufferedImage.getHeight();j++)
			{	
				bufferedImage.setRGB(i, j, bufferedImage.getRGB(i, j) + 0x11000000);
			}
		}
	}
	
	public static int getRed(int i)		{return i&0xFF;}
	public static int getGreen(int i)	{return (i>>8)&0xFF;}
	public static int getBlue(int i)	{return (i>>16)&0xFF;}
	public static int getAlpha(int i)	{return (i>>24)&0xFF;}
	
}
