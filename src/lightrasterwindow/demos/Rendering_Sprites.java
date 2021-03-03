package lightrasterwindow.demos;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lightrasterwindow.Keyboard;
import lightrasterwindow.Mouse;
import lightrasterwindow.Screen;
import lightrasterwindow.Sprite;
import lightrasterwindow.Spritesheet;
import lightrasterwindow.Window;

public class Rendering_Sprites implements Runnable{

	private Window window;//Class of window
	private Screen screen = null;//Screen class used for rendering
	
	int sprite_x = 0;//X position of rectangle
	int sprite_y = 0;//X position of rectangle

	Spritesheet my_spritesheet;
	Sprite my_sprite_medal;
	
	
	public Rendering_Sprites() {
		window = new Window(800, 600,4,"This is name of my window",false,this,null,null);
		
		//Init here other things
		my_spritesheet = new Spritesheet("/sprites.png");//Load sprite sheet sprites.png from res folder
		
		
		my_sprite_medal = new Sprite(16 * 1,			//y position on sprite sheet (16- size of 1 tile) * position on sprite sheet counting from 0
									16 * 1,				//y position on sprite sheet
									16,16,				//width and height
									my_spritesheet);
		
		
		screen = window.screen;
		
	}
	
	
	//Render method
	public void run() {
		if(screen == null)return;//Render after everything is properly initialized
		//screen.clear(screen.RGBToHexColor(0.5, 0.3, 0.2));
		screen.clear(0x44ff0000);
		
		int x = Mouse.Xpixel;
		int y = Mouse.Ypixel;
		
		
		screen.pixel(x, y, 0xffffcc77);
		
		//When ChromKey is used the pixels with given color will be skipped which will increase performance
		screen.setChromaKeyColor(0x00ff00ff);
		screen.useChromaKey(true);
		
		
		
		
		if(Keyboard.getKey(KeyEvent.VK_P) || Mouse.button_left){
			sprite_x = x;sprite_y= y;
		}
		
		if(Keyboard.getKeyOnce(KeyEvent.VK_O)|| Mouse.button_clicked_right){ 
			sprite_x = x;sprite_y = y;
		}
		
		screen.renderSprite(0, 0, my_spritesheet);//rendering whole sprite sheet
		
		screen.renderSprite(sprite_x, sprite_y, my_sprite_medal);//rendering sprite
		
		//WARNING renderSpriteRot and others rendering functions witch 'Rot' in name are not optimalized and might not work very well
		//Do not use if not necessary
		double rot = Math.atan2(100- Mouse.Xpixel,100- Mouse.Ypixel);
		
		screen.renderSpriteRot(100,100,
				128,128, //sprite size on screen
				rot,//rotation TODO:Write better rotations :-(
				my_sprite_medal);
		
		if(Keyboard.getKey(KeyEvent.VK_S)) {
			File outputfile = new File("image.png");
			try {
				ImageIO.write(screen.getImage(), "png", outputfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) {
		new Rendering_Sprites();
	}

}
