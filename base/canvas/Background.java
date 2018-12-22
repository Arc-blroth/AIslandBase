package base.canvas;

import java.awt.Image;

public class Background {
	private Image img;
	private String name;
	
	public Image getImg() {
		return img;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public void finalize() {
		img.flush();
	}

	public Background(Image img, String name) {
		this.img = img;
		this.name = name;
	}
}
