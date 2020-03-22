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



	public Screen screen;
	public Graphics graphics;
	private Keyboard keyboard = new Keyboard();
	private Mouse mouse = new Mouse();
	
	
	private Runnable r;
	private Runnable u;
	private Runnable g;
	public boolean STABLE_UPS = true;

	
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
			WIDTH = tk.getScreenSize().width;
			HEIGHT = tk.getScreenSize().height;
		}
		frame.setVisible(true);
		this.PIXEL_SIZE = pixel_size;
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
			}
			else
				update();
			render();
			FPS++;
			
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
	
	private void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		
		
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH + 10, HEIGHT + 10);
		r.run();
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
