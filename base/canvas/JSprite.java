package base.canvas;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;

import javax.swing.*;

import base.swing.DoubleDimension;

public class JSprite extends Thread {
	private JComponent jc;
	private Canvas c;
	private double x, y, w, h;
	
	public JSprite(double x, double y, double w, double h, JComponent jc, Canvas c) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.jc = jc;
		this.c = c;
	}
	
	@Override
	public void run() {}
	
	public JComponent getJComponent() {return jc;}
	
	public void setIndex(int zindex) {c.setComponentZOrder(jc, zindex);}

	public int getIndex() {return c.getComponentZOrder(jc);}
	
	public void setAbsoluteLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public DoubleDimension getAbsoluteLocation() {
		return new DoubleDimension(x, y);
	}

	public Canvas getCanvas() {return c;}

	public void repaint() {jc.repaint();}

	public void goTo(double x, double y) {setAbsoluteLocation(x, y);}

	public double getX() {return x;}

	public double getY() {return y;}

	public void setX(double x) {this.x = x;}

	public void setY(double y) {this.y = y;}
	
	public double getWidth() {return w;}

	public double getHeight() {return h;}

	public void setWidth(double w) {this.w = w;}

	public void setHeight(double h) {this.h = h;}

	public void changeXBy(double offset) {this.x += offset;}

	public void changeYBy(double offset) {this.y += offset;}

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
	
	public void setVisible(boolean v) {jc.setVisible(v);}

	public boolean isVisible() {return jc.isVisible();}
	
	public boolean isShowing() {return jc.isShowing();}
}
