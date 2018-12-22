package base.canvas;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.*;
import java.util.function.Consumer;

import javax.sound.*;
import javax.sound.sampled.*;
import javax.swing.*;
import base.swing.*;

public class Canvas extends JPanel {
	
	private Vector<Sprite> sprites = new Vector<Sprite>(10);
	private Vector<JSprite> jSprites = new Vector<JSprite>(10);
	private Vector<Background> bgs = new Vector<Background>(10);
	private Background currentBackground;
	private Grid g;
	private Init i;
	public Graphics2D graphics;
	
	public Canvas(Grid g, Init i) {
		super();
		
		this.g = g;
		this.i = i;
		
		if(Grid.isGridDebug()) {
			this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
		}
		
		this.setLayout(null);
		
		
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {repaint();}
			@Override
			public void componentResized(ComponentEvent e) {repaint();}
			@Override
			public void componentShown(ComponentEvent e)  {repaint();}
		});
	}
	
	public Init getInit() {
		return i;
	}

	public MainFrame getFrame() {
		return i.getMainframe();
	}
	
	public void addSprite(Sprite s, int zindex) {
		
		if(zindex > sprites.size()) sprites.setSize(zindex);
		
		sprites.insertElementAt(s, zindex);
		
		updateZIndexs();
		
		if(s instanceof AbstractSprite) {((AbstractSprite)(s)).start();}
		return;
	}
	
	public void removeSprite(int zindex) {
		
		if(zindex > sprites.size()) return;
		
		sprites.removeElementAt(zindex);
		
		updateZIndexs();
	}
	
	public void removeSprite(Sprite s) {
		if(sprites.contains(s)) sprites.removeElement(s);
	}
	
	public void removeAllSprites() {
		for (Iterator<Sprite> iterator = sprites.iterator(); iterator.hasNext(); ) {
			Sprite value = iterator.next();
		    iterator.remove();
		}
	}
	
	public void addJSprite(JSprite s) {
		jSprites.addElement(s);
		add(s.getJComponent());
		repaint();
		s.start();
	}

	public void removeJSprite(JSprite s) {
		if(jSprites.contains(s)) {
			jSprites.removeElement(s);
			remove(s.getJComponent());
		}
	}
	
	public void removeAllJSprites() {
		for (Iterator<JSprite> iterator = jSprites.iterator(); iterator.hasNext(); ) {
			JSprite js = iterator.next();
		    iterator.remove();
			remove(js.getJComponent());
		}
	}
	
	public Component add(Component c, int gridX, int gridY) {
		super.add(c);
		return c;
	}
	
	public void addBackground(Image image, String name) {
		for(Background b : bgs) {
			if(b.getName() == name) {throw new IllegalArgumentException("Background name is already reserved.");}
		}
		bgs.add(new Background(image, name));
	}
	
	public void setBackground(int i) {
		if(i < 0 || i > bgs.size()) throw new ArrayIndexOutOfBoundsException(i + " > " + bgs.size() + ": This background does not exist.");
		currentBackground = bgs.elementAt(i);
	}
	
	public void setBackground(String name) {
		for(Background b : bgs) {
			if(b.getName() == name) {currentBackground = b; return;}
		}
		throw new IllegalArgumentException("Background name does not match with any stored background.");
	}

	public Graphics getActualGraphics() {
		return graphics;
	}
	
	protected void paintComponent(Graphics ghpcs) {
		super.paintComponent(ghpcs);
		
		if(i != null) {
			Dimension d = new Dimension(getWidth(), getHeight());
			if(d != null) {

				if(bgs.size() > 0) drawBackground(ghpcs);
				if(sprites.size() > 0) updateHierarchy(); drawSprites(ghpcs);
				if(jSprites.size() > 0) drawJSprites(ghpcs);
				
				if(Grid.isGridDebug()) {
					double intervalx = d.width / this.g.getLength();
					double intervaly = ((double)d.height / (double)this.g.getWidth());
					drawGrid(intervalx, intervaly, d, Color.RED);
				}
			} 
		} else System.out.println("OH NOES");
	}

	public Grid getGrid() {
		return g;
	}

	private void drawSprites(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		graphics = g;
		for(int z = sprites.size() - 1; z  != 0 - 1; z--) {
			if(sprites.elementAt(z) != null) {
				Sprite s = sprites.elementAt(z);
				drawSprite(s, g);
			}
		}
	}

	private void drawSprite(Sprite s, Graphics2D g) {
		
		int x = (int)((double)((double)s.getPosition().dwidth) * (double)getWidth() / (double)this.g.getLength());
		int y = (int)((double)(((double)s.getPosition().dheight) * (double)getHeight() / (double)this.g.getWidth()));
		int w = (int)((double)getPxSize(this, s).dwidth / 2);
		int h = (int)((double)getPxSize(this, s).dheight / 2);
		
		if(s.isVisible() && s.getImage() != null) {
			
			if(s.getDirection() != 90 || s.getHorizontalFlip() || s.getVerticalFlip()) {
				AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(s.getDirection() - 90), x, y);
				tx.translate(x-w, y-h);
				
				byte hf, vf;
				int gh, gv;
				
				if(s.getHorizontalFlip() == true) {hf = -1; gh = -s.getImage().getWidth(null);} else {hf = 1; gh = 0;}
				if(s.getVerticalFlip() == true) {vf = -1; gv = -s.getImage().getHeight(null);} else {vf = 1; gv = 0;}
				tx.scale((double)hf * (double)getPxSize(this, s).width / (double)s.getImage().getWidth(null), (double)vf * (double)getPxSize(this, s).height / (double)s.getImage().getHeight(null));
				tx.translate(gh, gv);
				
				if(tx.getDeterminant() == 0) {System.err.println(s.getDirection() + ":   " + tx); return;}

				Image i = s.getImage();
				if(i != null) g.drawImage(i, tx, null);
				
			} else {
				Image i = s.getImage();
				if(i != null) g.drawImage(i, x-w, y-h, getPxSize(this, s).width, getPxSize(this, s).height, null);
			}
		}
		s.paint(x, y, w, h, g);
		
	}
	
	private DoubleDimension getPxSize(Canvas c, Sprite s) {
		if (s.getImage() != null && s.getCanvas() != null) {
			int ra = c.getWidth();
			int gd = s.getCanvas().getGrid().getLength();
		return new DoubleDimension( s.getSize() *  ((double)ra / (double)gd) * s.getImage().getWidth(null),
									s.getSize() *  ((double)ra / (double)gd) * s.getImage().getHeight(null));
		} else return new DoubleDimension(0,0);
	}
	
	private void drawBackground(Graphics ghpcs) {
		if(currentBackground != null) {
			Dimension d = new Dimension((int)ghpcs.getClipBounds().getWidth(), (int)ghpcs.getClipBounds().getHeight());
			ghpcs.drawImage(currentBackground.getImg(), (int)((double)((double)g.getLength() / 2) * (double)ghpcs.getClipBounds().getWidth() / (double)this.g.getLength()  - (double)((double)currentBackground.getImg().getWidth(null) / 2)),
														(int)((double)(((double)g.getWidth() / 2) * (double)ghpcs.getClipBounds().getHeight() / (double)this.g.getWidth())   - (double)((double)currentBackground.getImg().getHeight(null) / 2)), currentBackground.getImg().getWidth(null), currentBackground.getImg().getHeight(null), null);
		}
	}
	
	
	private void drawJSprites(Graphics ghpcs) {
		jSprites.forEach(s -> {
			JComponent jc = s.getJComponent();
			double oneSpaceX = (double)getWidth() / (double)this.g.getLength();
			double oneSpaceY = (double)getHeight() / (double)this.g.getWidth();
			jc.setBounds((int)Math.round((s.getX() - s.getWidth()/2D) * oneSpaceX), (int)Math.round((s.getY() - s.getHeight()/2D) * oneSpaceY), (int)Math.round(s.getWidth() * oneSpaceX), (int)Math.round(s.getHeight() * oneSpaceY));
			jc.repaint();
		});
	}
	
	public void drawGrid(double intervalx, double intervaly, Dimension bounds, Color c) {
		if(graphics != null) {
			graphics.setColor(c);
			//System.out.println("\n"+d.height);
			
			for(int ax = 1; ax <= (this.g.getLength()); ax++) {
				graphics.drawLine((int)Math.round(intervalx * ax), 0, (int)Math.round(intervalx * ax), bounds.height);
			}
			for(int ay = 1; ay <= (this.g.getWidth()); ay++) {
				graphics.drawLine(0, (int)Math.round(intervaly * ay), bounds.width, (int)Math.round(intervaly * ay));
				//System.out.println(ay * intervaly);
			}
		}
	}
	
	private void updateZIndexs() {
		for(int z = 0; z < sprites.size(); z++) {
			if(sprites.elementAt(z) != null) sprites.elementAt(z).setIndex(z);
		}
	}
	
	protected void updateHierarchy() {
		for(int z = 0; z < sprites.size(); z++) {
			if(sprites.elementAt(z) != null) sprites.elementAt(z).setIndex(sprites.elementAt(z).getIndex());
		}
		updateZIndexs();
	}
	
	public void playSound(String filepath, Class c) {
		try {
	        Clip clip = AudioSystem.getClip();
	        AudioInputStream inputStream = AudioSystem.getAudioInputStream(c.getResourceAsStream(filepath));
	        clip.open(inputStream);
	        clip.start(); 
	        clip.drain();
	      } catch (Exception e) {
	        System.err.println(e.getMessage());
	        e.printStackTrace();
	      }

	}

	public void setIconImage(Image image) {
		i.getMainframe().setIconImage(image);
		
	}
	
}
