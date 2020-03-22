package lightrasterwindow.demos;

import java.awt.event.KeyEvent;

import lightrasterwindow.Keyboard;
import lightrasterwindow.Mouse;
import lightrasterwindow.Screen;
import lightrasterwindow.Window;

//Example of creating window and placing some things


public class FirstWindow implements Runnable{

	private Window window;//Class of window
	private Screen screen = null;//Screen class used for rendering
	
	int rect_x = 0;//X position of rectangle
	int rect_y = 0;//X position of rectangle
	int rect_width = 30;//width of rectangle
	int rect_height = 20;//height of rectangle
	
	public FirstWindow() {
		window = new Window(
				800, 600,//Width and height of the window
				4,//size of pixel in the window image buffer, in this case 4 means each pixel is 4 pixels width and height on hardware display
				"This is name of my window",
				false,//is full screen enabled (if true than width and height of the window will be overwritten)
				this,//Runnable for rendering (necessary)
				null,//Runnable for update (unnecessary)
				null);//Runnable for Graphics class for using build in java for rendering text,shapes etc. (unnecessary)
		
		
		//Init here other things
		
		
		screen = window.screen;//Create reference to window screen class that is used for rendering. After screen is assigned render will start
		
		
	}
	
	
	//Render method
	public void run() {
		if(screen == null)return;//Render after everything is properly initialized
		screen.clear(//Clears screen to given color in hex
				screen.RGBToHexColor(0.5, 0.3, 0.2));//convert RGB values to hex
		
		int x = Mouse.Xpixel;//Get X mouse cursor postion (in big pixels) there is also Mouse.X witch returns value in real pixels precision
		int y = Mouse.Ypixel;//Get Y mouse cursor position
		
		
		screen.pixel(x, y, 0xffffcc77);//Fill pixel at x and y position witch color given in hex 0xAARRGGBB
		
		
		
		if(Keyboard.getKey(KeyEvent.VK_P)//While pressing key 'P' 
			|| Mouse.button_left	){//Or pressing left mouse button rectangle will be moved to cursor position
			rect_x = x;rect_y = y;
		}
		
		if(Keyboard.getKeyOnce(KeyEvent.VK_O)//After pressing key 'O' once 
			|| Mouse.button_clicked_right){  //Or clicking right mouse button rectangle will be moved to cursor position
			rect_x = x;rect_y = y;
		}
		
		
		screen.frect(rect_x, rect_y, rect_width, rect_height, 0xffffffff);//render fill rectangle
		
		
	}
	
	public static void main(String[] args) {
		new FirstWindow();//Start program
	}

}
