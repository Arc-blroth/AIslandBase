package base.canvas;

import java.awt.Dimension;

public class Grid {
	public enum Anchors {TOP, CENTER, BOTTOM, TOPLEFT, TOPRIGHT, CENTERLEFT, CENTERRIGHT, BOTTOMLEFT, BOTTOMRIGHT}

	protected static boolean debug;
	
	private int l;
	private int w;
	
	public Grid(int l, int w) {
		this.l = l;
		this.w = w;
	}
	
	public static void setGridDebug(boolean d) {
		debug = d;
	}

	public static boolean isGridDebug() {
		return debug;
	}

	public int getLength() {
		return l;
	}

	public int getWidth() {
		return w;
	}

	
}
