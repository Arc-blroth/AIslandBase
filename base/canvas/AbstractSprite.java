package base.canvas;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.TimerTask;

import base.canvas.Grid.Anchors;
import base.swing.DoubleDimension;
import base.swing.Unfinished;

public class AbstractSprite extends Thread implements Sprite {
	
	private int zindex;
	private Image img;
	private double x, y; //Grid Coordinates, not Pixel Coordinates
	private Grid.Anchors a;
	private double size = 1.00D;
	private final Canvas c;
	private int direction = 90;
	private boolean hf = false;
	private boolean vf = false;
	private boolean visible = true;
	
	
	//Basic Methods
	
	
	/**
	 * 
	 * Creates an AbstractSprite that implements <tt><i>interface</i></tt>  Sprite.
	 * 
	 * @param x - The x-location of the sprite, in grid coordinates.
	 * @param d - The y-location of the sprite, in grid coordinates.
	 * @param zindex - The z-layer on which this sprite is on relative to other sprites.
	 * @param size - The size of the sprite as a percent to the pixel size of the image.
	 * @param img - The Image used to display the sprite, will be scaled according to size constraints. A <tt>null</tt> image results in an invisible sprite.
	 * 
	*/
	public AbstractSprite(double x, double y, int zindex, double size, Image img, Canvas c) {
		setPosition(x, y);
		setIndex(zindex);
		setSize(size);
		setImage(img);
		this.c = c;
		direction = 90;
		
	}
	
	@Override
	public void setIndex(int zindex) {
		this.zindex = zindex;
	}

	@Override
	public void setImage(Image image) {
		this.img = image;
	}

	@Override
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Canvas getCanvas() {
		return c;
	}
	
	@Override
	public DoubleDimension getPosition() {
		return new DoubleDimension(x, y);
	}
	
	@Override
	public int getIndex() {
		return zindex;
	}

	@Override
	public Image getImage() {
		return img;
	}
	
	@Override
	public void setSize(double s) {
		size = s;
	}

	@Override
	public double getSize() {
		return size;
	}
	
	public void wait(int sec) {
		try {this.wait((long)(1000 * sec));} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	public void paint(int x, int y, int w, int h, Graphics g) {
		
	}
	
	//Movement
	
	public void goTo(int x, int y) {
		setPosition(x, y);
	}
	
	public void goTo(double d, double e) {
		setPosition(d, e);
	}
	
	public void moveSteps(int s) {
		goTo((int)Math.round(x + Math.sin(direction * Math.PI / 180) * s), (int)Math.round(y + Math.cos(direction * Math.PI / 180) * s));
	}
	
	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}
	
	@Override
	public void setX(double x) {
		this.x = x;
	}
	
	@Override
	public void setY(double y) {
		this.y = y;
	}
	
	public void changeXBy(double offset) {
		this.x += offset;
	}
	
	public void changeYBy(double offset) {
		this.y += offset;
	}
	
	public void turnClockwise(int deg) {
		direction = (direction + deg) % 360;
	}
	
	public void turnCounterclockwise(int deg) {
		direction = (direction - deg) % 360;
	}

	@Override
	public void setDirection(int deg) {
		direction = deg % 360;
	}

	@Override
	public int getDirection() {
		return direction % 360;
	}

	@Override
	public void glideTo(int fx, int fy, double sec) {
		
		long time = (long)(sec * 1000);
		double ox = x;
		double oy = y;
		
		new Thread() {
			
			public void run() {
				try {
					for(long t = 0; t <= time; t++) {
						goTo((ox + (((double)((double)fx - ox)) * ((double)t/(double)time))), 
							  oy +  (((double)((double)fy - oy)) * ((double)t/(double)time)));
						c.repaint();
						Thread.sleep(1);
					}
				} catch (InterruptedException e) {e.printStackTrace();}
				
				goTo(fx, fy);
			}
		}.run();
		
	}
	
	public void smoothGlideX(double target, double change, int interval) {
		Object o = new Object();
		java.util.Timer t = new java.util.Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				if(Math.round(getX() * 10) != Math.round(target * 10)) {
					changeXBy(Math.abs(getX() - target)/change);
				} else {
					t.cancel(); t.purge();
					synchronized(o) {o.notifyAll();}
				}
			}
		};
		t.scheduleAtFixedRate(tt, 0, interval);
		synchronized(o) {try {o.wait();} catch (InterruptedException e) {}}
	}
	
	public void smoothGlideY(double target, double change, long interval) {
		Object o = new Object();
		java.util.Timer t = new java.util.Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				if(Math.round(getY() * 10) != Math.round(target * 10)) {
					changeYBy(Math.abs(getY() - target)/change);
				} else {
					t.cancel(); t.purge();
					synchronized(o) {o.notifyAll();}
				}
			}
		};
		t.scheduleAtFixedRate(tt, 0, interval);
		synchronized(o) {try {o.wait();} catch (InterruptedException e) {}}
	}

	@Override
	public void setHorizontalFlip(boolean hf) {
		this.hf = hf;
		
	}

	@Override
	public void setVerticalFlip(boolean vf) {
		this.vf =vf;
		
	}
	
	@Override
	public boolean getHorizontalFlip() {
		return hf;
	}
	
	@Override
	public boolean getVerticalFlip() {
		return vf;
	}

	@Override
	public void setVisible(boolean v) {
		visible  = v;
		
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void run() {}


}
