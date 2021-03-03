package lightrasterwindow;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


public class Mouse implements MouseListener,MouseMotionListener,MouseWheelListener{

	public static int X,Y,Xpixel,Ypixel;
	public static boolean button_left,button_right,button_clicked_left,button_clicked_right,button_relesed_clicked_left,button_relesed_clicked_right;
	public static int mouse_wheel_notches = 0;
	private static boolean b1 = false,b2 = false;//previous states
	private static int prev_mouse_wheel_notches = 0;
	private int difrence_width = 0;
	private int difrence_height = 0;
	private int width = 0;
	private int height = 0;
	private int pixel_size = 0;
	
	
	
	public void update_vars(int difrence_width,int difrence_height,int width,int height,int pixel_size) {
		this.difrence_width = difrence_width;
		this.difrence_height = difrence_height;
		this.width = width;
		this.height = height;
		this.pixel_size = pixel_size;
	}
	
	public static void update() {
		
		//Mouse wheel reseting
		if(prev_mouse_wheel_notches == mouse_wheel_notches)
			prev_mouse_wheel_notches = mouse_wheel_notches = 0;
		
		prev_mouse_wheel_notches = mouse_wheel_notches;
		
		//Clicking left mouse button
		if(!b1 && button_left) {
			b1 = true;
			button_clicked_left = true;
		}
		else{
			button_clicked_left = false;
		}
		if(!button_left && b1) {
			b1 = false;
			//relesing mouse button
			button_relesed_clicked_left = true;
		}else {
			button_relesed_clicked_left = false;
		}
		
		//Clicking right mouse button
		if(!b2 && button_right) {
			b2 = true;
			button_clicked_right = true;
		}
		else{
			button_clicked_right = false;
		}
		if(!button_right && b2) {
			b2 = false;
			button_relesed_clicked_right = true;
		}
		else {
			button_relesed_clicked_right = false;
		}
	
		
		
	
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		X = (int)(e.getX() );
		
		Y = (int)(e.getY() );
		
		
		
		Xpixel = (int)((float)X/pixel_size);
		Ypixel = (int)((float)Y/pixel_size);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseMoved(e);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseMoved(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseMoved(e);
		if(e.getButton() == e.BUTTON1) {//Left
			button_left = true;
		}
		
		if(e.getButton() == e.BUTTON3) {//Prawy
			button_right = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseMoved(e);
		
		if(e.getButton() == e.BUTTON1) {//Left
			button_left = false;
		}
		
		if(e.getButton() == e.BUTTON3) {//Prawy
			button_right = false;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent event)
	{
		mouse_wheel_notches = event.getWheelRotation();
		
	}

}
