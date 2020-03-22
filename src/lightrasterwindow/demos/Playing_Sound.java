package lightrasterwindow.demos;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import lightrasterwindow.Keyboard;
import lightrasterwindow.Mouse;
import lightrasterwindow.Screen;
import lightrasterwindow.Window;
import lightrasterwindow.experimental_1.SoundFile;

public class Playing_Sound implements Runnable{

	private Window w;
	private Screen s;
	private SoundFile f;
	
	
	public Playing_Sound() {
		
		//Example of using graphics runnable
		Runnable graphics = new Runnable() {

			public void run() {
				if(w != null)//<---------------|
					if(w.graphics != null)//<--|---- double check for full init of the window
					{
						w.graphics.setColor(Color.WHITE);
						w.graphics.setFont(new Font("Arial", 0, 14));
						w.graphics.drawString("Press 'P ' to play sound", 0, 25);
						w.graphics.setFont(new Font("Arial", 0, 9));
						w.graphics.drawString("Or just click anywhere", 28, 50);
					}
				
			}
		};
		
		w = new Window(600, 400, 4, "Sound playing program", false, this, null, graphics);
		
		
		f = new SoundFile("/sound.wav");//Loading sound from res folder
		
		
		s = w.screen;
	}
	
	public void run() {
		if(s == null)return;
		
		if(Keyboard.getKeyOnce(KeyEvent.VK_P) || Mouse.button_clicked_left) 
			f.play();
		
	}
	
	public static void main(String[] s) {
		new Playing_Sound();
		
	}

}
