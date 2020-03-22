package lightrasterwindow;


import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Spritesheet {

	public 	int WIDTH,HEIGHT;
	public  int[] pixels;
	
	public Spritesheet(String path){
		try {
			BufferedImage image = ImageIO.read(Spritesheet.class.getResource(path));
			WIDTH = image.getWidth();
			HEIGHT = image.getHeight();
			pixels = new int[WIDTH * HEIGHT];
			System.out.println(image.getType());
			image.getRGB(0,0,WIDTH,HEIGHT,pixels,0,WIDTH);
			//image.getData().getPixel(WIDTH, HEIGHT, pixels);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
