package base.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import base.canvas.*;

public class Init {

	protected String version;
	protected String title;
	private Canvas MasterCanvas;
	private MainFrame mf;
	private Image icon;
	private boolean constricted = false;
	
	public MainFrame getMainframe() {
		return mf;
	}
	
	public void setCanvas(Canvas c) {
		MasterCanvas = c;
		mf.setVisible(false);
		
		mf.setVisible(true);
	}
	
	private Init(String title, String version, Image icon, Grid g, boolean constricted) {
		this.version = version;
		this.title = title;
		this.icon = icon;
		this.constricted = constricted;
		
		MasterCanvas = new Canvas(g, this);
		
		if(version != null) version = " | Version " + version;
		else version = "";
		
		mf = new MainFrame(title + version, MasterCanvas, null, null);
		if(icon != null) mf.setIconImage(icon);
		MasterCanvas.setBackground(Color.WHITE);
		
		if(constricted) {
			JPanel constricter = new JPanel();
			if(Grid.isGridDebug()) constricter.setBorder(BorderFactory.createLineBorder(new Color(198,255,76,200), 5));
			constricter.setLayout(new BoxLayout(constricter, BoxLayout.LINE_AXIS));
			JPanel cp = new JPanel();
			cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
			JPanel cp2 = new JPanel();
			if(Grid.isGridDebug()) cp2.setBorder(BorderFactory.createLineBorder(new Color(198,255,76,200), 5));
			cp2.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			
			constricter.setBackground(Color.BLACK);
			cp2.setBackground(Color.BLACK);
			cp.setBackground(Color.BLACK);
			
			cp2.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				
				int h = mf.getHeight();
				int w = mf.getWidth();
				double oh = g.getWidth();
				double ow = g.getLength();
				
				if(w < h) {
					w = (int)Math.round((double)h / oh * ow);
					if(w > mf.getWidth()) {
						w = mf.getWidth();
						h = (int)Math.round((double)w / ow * oh);
					}
				}
				else {
					h = (int)Math.round((double)w / ow * oh);
					if(h > mf.getHeight()) {
						h = mf.getHeight();
						w = (int)Math.round((double)h / oh * ow);
					}
				}
				
				constricter.setPreferredSize(new Dimension(w, h));
			}
			});
			
			constricter.add(MasterCanvas);
			cp2.add(constricter);
			cp.add(Box.createVerticalGlue()); cp.add(cp2);
			mf.setContentPane(cp);
			
			
		} else mf.setContentPane(MasterCanvas);
	}
	
	public static Init getInit(String title, String version, Image icon, int l, int w) {
		return new Init(title, version, icon, new Grid(l, w), false);
	}
	
	public static Init getInit(String title, String version, Image icon, int l, int w, boolean constrict) {
		return new Init(title, version, icon, new Grid(l, w), constrict);
	}
	
	public base.canvas.Canvas Go(boolean fullscreen) {
		return Go(fullscreen, true);
	}
	
	public base.canvas.Canvas Go(boolean fullscreen, boolean show) {
		if (fullscreen) {
			mf.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}

		mf.setBackground(Color.black);
		mf.setIconImage(icon);
		mf.setVisible(show);
		mf.toFront();
		return MasterCanvas;
	}
	
	public Dimension getMFDimensions() {
		return mf.getSize();
	}

	public synchronized String getVersion() {
		return version;
	}

	public synchronized String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public static void main(String argz[]) {
	}

}
