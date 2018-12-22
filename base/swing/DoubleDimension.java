package base.swing;

import java.awt.Dimension;

public class DoubleDimension extends Dimension {
	public double dwidth;
	public double dheight;
	
	
	public DoubleDimension(double dwidth, double dheight) {
		super((int)Math.round(dwidth), (int)Math.round(dheight));
		this.dwidth = dwidth;
		this.dheight = dheight;
	}
	
}
