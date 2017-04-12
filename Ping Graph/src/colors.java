import java.awt.Color;

public class colors {
	int r;
	int g;
	int b;
	int alpha;
	public colors(int r, int g, int b, int alpha) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = alpha;
	}
	
	public Color getColor () {
		return new Color(r,g,b);
	}

}
