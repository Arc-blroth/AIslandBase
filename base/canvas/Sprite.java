package base.canvas;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import base.swing.DoubleDimension;

public interface Sprite extends Runnable {
	
	void setIndex(int zindex);
	int getIndex();
	void setImage(Image img);
	Image getImage();
	void setPosition(double x, double y);
	DoubleDimension getPosition();
	void setSize(double s);
	double getSize();
	public Canvas getCanvas();
	abstract void paint(int x, int y, int w, int h, Graphics g);

	
	public void goTo(int x, int y);
	public void moveSteps(int s);
	double getX();
	double getY();
	void setX(double x);
	void setY(double y);
	public void changeXBy(double offset);
	public void changeYBy(double offset);
	public void turnClockwise(int deg);
	public void turnCounterclockwise(int deg);
	public void setHorizontalFlip(boolean hf);
	public void setVerticalFlip(boolean vf);
	public boolean getHorizontalFlip();
	public boolean getVerticalFlip();
	public void setDirection(int deg);
	public int  getDirection();
	public void glideTo(int fx, int fy, double sec);
	public void setVisible(boolean v);
	public boolean isVisible();

}
