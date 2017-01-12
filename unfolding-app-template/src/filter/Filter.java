package filter;

import processing.core.PApplet;
import processing.core.PImage;

public class Filter {

	private PApplet pApplet;
	private int x;
	private int y;
	private int width;
	private int height;
	private PImage icon;
	
	public Filter(PApplet pApplet, int x, int y, int width, int height) {
		this.pApplet = pApplet;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.icon = this.pApplet.loadImage("img/filter.png");
	}
	
	public void draw() {
		this.pApplet.image(this.icon, this.x, this.y, this.width, this.height);
	}
	
}
