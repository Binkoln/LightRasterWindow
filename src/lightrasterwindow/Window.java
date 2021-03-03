package lightrasterwindow;


import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JColorChooser;
import javax.swing.JFrame;


public class Window extends Canvas implements Runnable{

	
	//20.07.2019
	public static final double LIBRARY_VERSION = 1.0;
	private static String TITLE = "";
	private static int WIDTH = 512,HEIGHT = 512;
	private int PIXEL_SIZE = 4;
	private static final int FRAMERATE = 60;
	
	private boolean RUNNING = false;
	private JFrame frame;

	public static boolean experimental_Static_Resolution = false;

	public Screen screen = null;
	public Graphics graphics;
	private Keyboard keyboard = new Keyboard();
	private Mouse mouse = new Mouse();
	
	
	private Runnable r;
	private Runnable u;
	private Runnable g;
	public boolean STABLE_UPS = true;
	public boolean STABLE_FPS = false;
	public boolean MANUAL_SCREEN_RENDERING = false;
	public boolean MANUAL_WINDOW_CLEARING = false;

	
	public static String getTitle() {return TITLE;}

	public static void setTitle(String title) {
		TITLE = title;
	}

	public int getWidth() {
		return WIDTH;
	}

//	public void setWidth(int width) {
//		WIDTH = width;
//	}

	public int getHeight() {
		return HEIGHT;
	}

//	public void setHeight(int height) {
//		HEIGHT = height;
//	}

	public int getPixelSize() {
		return PIXEL_SIZE;
	}


	public Window(int width,int height,int pixel_size,String title,boolean fullscreen,Runnable render,Runnable update,Runnable graphics){
		Window.WIDTH = width;
		Window.HEIGHT = height;
		Window.TITLE = title;
		setSize(new Dimension(WIDTH,HEIGHT));
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setMinimumSize(new Dimension(WIDTH,HEIGHT));
		setMaximumSize(new Dimension(WIDTH,HEIGHT));
		
		
		
		frame = new JFrame();
		
		if(fullscreen){
			frame.setUndecorated(true);
			//frame.setAlwaysOnTop(true);
			frame.setResizable(false);
		}
		
		frame.setTitle(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(keyboard);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		addMouseWheelListener(mouse);
		
		frame.add(this,new BorderLayout().CENTER);
		
	
		//frame.getContentPane().setPreferredSize(new Dimension(WIDTH,HEIGHT));
		frame.getContentPane().setMinimumSize(new Dimension(WIDTH,HEIGHT));
		frame.getContentPane().setMaximumSize(new Dimension(WIDTH,HEIGHT));

		frame.pack();
		frame.setSize(frame.getWidth() - 10,frame.getHeight() - 10);
		
		
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		if(fullscreen){
			Toolkit tk = Toolkit.getDefaultToolkit();
			frame.setLocation(0, 0);
			frame.setSize(tk.getScreenSize());
			if(experimental_Static_Resolution&& screen == null) {
					screen = new Screen(WIDTH / pixel_size,HEIGHT / pixel_size);
			}
			
			WIDTH = tk.getScreenSize().width;
			HEIGHT = tk.getScreenSize().height;
		}
		frame.setVisible(true);
		this.PIXEL_SIZE = pixel_size;
		if(screen == null)
		screen = new Screen(WIDTH / pixel_size,HEIGHT / pixel_size);
		

		this.r = render;
		this.u = update;
		this.g = graphics;
		mouse.update_vars(frame.getWidth() - WIDTH,frame.getHeight() - HEIGHT,WIDTH,HEIGHT,PIXEL_SIZE);
		start();

	}
	
	private void start(){
		if(RUNNING)return;
		RUNNING = true;
		new Thread(this,"Window: " + TITLE).start();
	}
	
	
	
	private void stop(){
		if(!RUNNING)return;
		RUNNING = false;
		frame.dispose();
		System.exit(0);
	}
	
	private double timerek = System.currentTimeMillis(); 
	private int UPS = 0;
	private int FPS = 0;
	private double  delta = 0;
	private double frametime = 1000000000 / FRAMERATE;
	private long timeNOW = System.nanoTime();
	private long timeLAST = System.nanoTime();
	
	public void run(){
		
		while(RUNNING ){
			timeNOW = System.nanoTime();
			delta += (timeNOW - timeLAST) / frametime;
			timeLAST = timeNOW;
			
			if(STABLE_UPS)
			while(delta >= 1){
				update();
				delta -= 1;
				UPS++;
				
				if(STABLE_FPS && delta < 2)
				{
					render();
					FPS++;
				}
			}
			else {
				update();
				UPS++;
			}
			if(!STABLE_FPS)
			{
				render();
				FPS++;
			}
			if(System.currentTimeMillis() - timerek >= 1000){
				timerek = System.currentTimeMillis();
				//System.out.println("FPS: " + FPS +"--- UPS: " + UPS);
				frame.setTitle(TITLE + " -- FPS: " + FPS +"--- UPS: " + UPS);
				FPS = 0;
				UPS = 0;
			}
		}
		stop();
		
	}
	

	

	private void update(){
		keyboard.update();
		Mouse.update();
		if(u != null)
		u.run();
	
		
	}
	
	private BufferStrategy shared_bs = null;
	private Graphics shared_g = null;
	
	public void start_manual_rendering() {
		shared_bs = getBufferStrategy();
		shared_g = shared_bs.getDrawGraphics();
	}
	
	public void renderScreenOnWindow(final Screen screen,int pos_x_on_window,int pos_y_on_window,int width_on_window,int height_on_window)
	{
		
		
		shared_g.drawImage(screen.getImage(),pos_x_on_window,pos_y_on_window,width_on_window,height_on_window,null);
		
	}
	
	public void stop_manual_rendering() {
		shared_g.dispose();
		shared_bs.show();
	}
	
	
	
	private void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		r.run();
		Graphics g = bs.getDrawGraphics();
		if(!MANUAL_WINDOW_CLEARING) 
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH + 10, HEIGHT + 10);
		}
		
		
		if(MANUAL_SCREEN_RENDERING)return;
		
		

		
		//screen.clear(0x000000);
		//gsm.render(screen);
		graphics = screen.getImage().getGraphics();
		if(this.g != null)
			this.g.run();
		
	
			g.drawImage(screen.getImage(),0,0,WIDTH,HEIGHT,null);
		
		
			g.dispose();
			bs.show();
		
	}
	
	

}
