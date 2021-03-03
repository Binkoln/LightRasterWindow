package lightrasterwindow;


import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Screen {

	private final int WIDTH;
	private final int HEIGHT;
	
	private BufferedImage image;
	private int[] pixels ;
	private double[] z_buffer;
	private double z_buffer_value=0;
	private boolean use_z_buffer = false;
	
	private boolean b_useChromaKey = false;
	private int i_chromaKeyColor = 0xff00ff;
	
	
	public void useChromaKey(boolean b) {b_useChromaKey = b;}
	
	public void setChromaKeyColor(double r,double g,double b) {setChromaKeyColor(RGBToHexColor(r, g, b));}
	
	public int getWidth() {return WIDTH;}
	public int getHeight() {return HEIGHT;}
	
	public void setChromaKeyColor(int c) {
		i_chromaKeyColor = c;
	}
	
	public void setZBufferValue(double d) {z_buffer_value = d;}
	public void useZBuffer(boolean set) {use_z_buffer = set;}
	public double getZBufferValue(int x,int y) {return z_buffer[x + y * WIDTH];}
	
	
	
	public Screen(int w,int h){
		WIDTH = w;
		HEIGHT = h;
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		z_buffer = new double[w*h];
		clear_z_buffer(0);
	}
	
	public void clear_z_buffer(double depth){
		for(int i = 0; i < WIDTH * HEIGHT;i++){
			z_buffer[i] = depth;
		}
	}
	
	public void clear(int color){
		for(int i = 0; i < WIDTH * HEIGHT;i++){
			pixels[i] = color;
		}
	}
	
	public BufferedImage getImage(){
		return image;
	}
	

	public void renderSprite(int px,int py,Sprite s){
	
		for(int y = 0;y < s.height;y++)
			for(int x = 0;x < s.width;x++){
				pixel((int)(px + x ), (int)(py  + y), s.sp.pixels[s.x + x + (s.y + y) * s.sp.WIDTH]);
			}
	}
	
	public void renderScreen(int px,int py,Screen s){
		for(int y = 0;y < s.HEIGHT;y++)
			for(int x = 0;x < s.WIDTH;x++){
				pixel((int)(px + x ), (int)(py  + y), s.pixels[x + y * s.WIDTH]);
			}
	}
	
	public void renderSprite(int px,int py,boolean flip_x,Sprite s){
		if(!flip_x) {
		for(int y = 0;y < s.height;y++)
			for(int x = 0;x < s.width;x++){
				pixel((int)(px + x ), (int)(py  + y), s.sp.pixels[s.x + x + (s.y + y) * s.sp.WIDTH]);
			}
		}else {
			for(int y = 0;y < s.height;y++)
				for(int x = 0;x < s.width;x++){
					pixel((int)(px + x ), (int)(py  + y), s.sp.pixels[s.x  + s.width-1- x + (s.y + y) * s.sp.WIDTH]);
				}
		}
	}
	
	public void renderSprite(int px,int py,Spritesheet s){
		renderSprite(px,py,0,0,s.WIDTH,s.HEIGHT,s);
	}
	public void renderSprite(int px,int py,int sx,int sy,int size,Spritesheet s) {
		renderSprite(px,py,sx,sy,size,size,s);
	}
	
	public void renderSprite(int px,int py,int sx,int sy,int s_width,int s_height,Spritesheet s){
		
		for(int y = 0;y < s_height;y++)
			for(int x = 0;x < s_width;x++){
				pixel(px + x, py + y, s.pixels[sx + x + ((sy + y) * s.WIDTH)]);
			}
	}
	
	public void renderScaledSprite(int px,int py,int width,int height,int sx,int sy,int s_width,int s_height,Spritesheet s){
		int nwid = width;
		if(nwid+px > WIDTH)
			nwid = WIDTH - px;
		
		int nhid = height;
		if(nhid+py > HEIGHT)
			nhid = HEIGHT - py;
		
		int ystart = (py < 0)?(-py):0;
		int xstart = (px < 0)?(-px):0;
		for(int y = ystart;y < nhid;y++)
			for(int x = xstart;x < nwid;x++){
				int x_on_sp = (int)(((double)x/(double)width)*(double)s_width);
				int y_on_sp = (int)(((double)y/(double)height)*(double)s_height);
				pixel(px + x, py + y, s.pixels[sx + x_on_sp + ((sy + y_on_sp) * s.WIDTH)]);
			}
	}
	
	public void renderScaledSprite(int px,int py,int width,int height,Spritesheet s) {
		renderScaledSprite(px,py,width,height,0,0,s.WIDTH,s.HEIGHT,s);
	}
	
	public void renderScaledSprite(int px,int py,int width,int height,Sprite s) {
		renderScaledSprite(px, py, width, height, s.x, s.y, s.width, s.height, s.sp);
	}
	
	public void line(int x1,int y1,int x2,int y2,int color) {
		double a = x2 - x1;
		double b = y2 - y1;
		double l = Math.sqrt((a * a) + (b * b));
		double angl = b/a;
		if(angl > 1 || angl < -1) {
			angl = a/b;
			if(b > 0)
			for(double i = 0;i < b;i += 1) {
				pixel((int)(x1 + i * angl), (int)(y1 + i), color);
			}
			else
				for(double i = 0;i > b;i -= 1) {
					pixel((int)(x1 + i * angl), (int)(y1 + i), color);
				}
		}
		else {	
			if(a > 0)
			for(double i = 0;i < a;i +=1) {
				pixel((int)(x1 + i), (int)(y1 + i*angl), color);
			}
			else
				for(double i = 0;i > a;i -= 1) {
					pixel((int)(x1 + i), (int)(y1 + i*angl), color);
				}
		}
	}
	
	public void frect(int px,int py,int w,int h,int color){
		if(w == 0 || h == 0)return;
		if(h > 0) {
			for(int y = 0;y < h;y++)
				if(w > 0){
					for(int x = 0;x < w;x++){
						pixel(x + px, y + py, color);
					}
				}else {
					for(int x = 0;x > w;x--){
						pixel(x + px, y + py, color);
					}
				}
		}
		else {
			for(int y = 0;y > h;y--)
				if(w > 0){
					for(int x = 0;x < w;x++){
						pixel(x + px, y + py, color);
					}
				}else {
					for(int x = 0;x > w;x--){
						pixel(x + px, y + py, color);
					}
				}
		}
	}
	
	public void rect(int x, int y, int w, int h, int color)
	{
		if(w == 0 || h == 0)return;
		int xx = (w > 0)?x:(x+w);
		int yy = (h > 0)?y:(y+h);
		int ww = Math.abs(w);
		int hh = Math.abs(h);
		
		for(int i = 0;i <= ww;i++) {
			pixel(xx + i, yy, color);
			pixel(xx + i, yy + hh, color);
		}
		
		for(int i = 0;i <= hh;i++) {
			pixel(xx , yy + i, color);
			pixel(xx + ww, yy + i, color);
		}
			
		
	}
	
	public void frect_rot(int x,int y,int w,int h,double rot,int color) {
		double cos_val = Math.cos(rot);
		double sin_val = Math.sin(rot);
		double sq2 = Math.sqrt(2.0);
		for(double j = -h/2;j < h/2;j+= 0.5)
			for(double i = -w/2;i < w/2;i+= 0.5) {
	
				
				pixel(
						x + (int)((i*(cos_val+sin_val) +j*(sin_val-cos_val))/sq2)
						,y - (int)((j*(sin_val+cos_val) + i*(cos_val-sin_val))/sq2)
						, color);
			}
		
		//s.pixel((int)(x + w * cos_val), (int)(y + h * sin_val), 0xffff00);
		
	}
	
	public void renderSpriteRot(int x,int y,int size,double rot,Sprite s) {
		renderSpriteRot( x, y, size,size, rot, s.x, s.y, s.sp) ;
	}
	public void renderSpriteRot(int x,int y,int w,int h,double rot,Sprite s) {
		renderSpriteRot( x, y, w,h, rot, s.x, s.y, s.sp) ;
	}
	
	public void renderSpriteRot(int x,int y,int size,double rot,int sx,int sy,Spritesheet s) {
		renderSpriteRot(x,y,size,size,rot,sx,sy,s);
	}
	
	public void renderSpriteRot(int x,int y,int w,int h,double rot,int sx,int sy,Spritesheet s) {
		double cos_val = Math.cos(-rot + Math.PI/4.0);
		double sin_val = Math.sin(-rot + Math.PI/4.0);
		double sq2 = Math.sqrt(2.0);
		
		int w_na_2 = w/2;
		int h_na_2 = h/2;
		
		for(double j = -h_na_2;j < h_na_2;j+= 0.5)
			for(double i = -w_na_2;i < w_na_2;i+= 0.5) {
				int px = (int)((i + w_na_2)/(double)w * 16)+sx;
				int py = (int)((j + h_na_2)/(double)h * 16)+sy;
				int px_color = s.pixels[(px) + py*s.WIDTH];
				
				pixel(
						x - (int)((i*(-cos_val-sin_val) +j*(sin_val-cos_val))/sq2)
						,y + (int)((j*(sin_val+cos_val) - i*(cos_val-sin_val))/sq2)
						, px_color);
			}
		
		//s.pixel((int)(x + w * cos_val), (int)(y + h * sin_val), 0xffff00);
		
	}
	
	//function returns 3 doubles in range from 0.0 to 1.0 that represents colors
	public double[] getColor(int x,int y) {
		if( x < 0 || x >= WIDTH ||
				y < 0 || y >= HEIGHT )return null;
		int color = pixels[x + y * WIDTH];
		
		double col[] = {(double)((color & 0xFF0000) >> 16) /  255.0 // red
						,(double)((color & 0xFF00) >> 8) / 255.0 //green
						,(double)(color & 0xFF) / 255.0}; //blue
		return col;
	}
	
	public int getPixel(int x,int y) {
		if( x < 0 || x >= WIDTH ||
				y < 0 || y >= HEIGHT )return 0;
		return pixels[x + y * WIDTH];
	}
	
	public static double[] hexColorToRGB(int color) {
		double col[] = {(double)((color & 0xFF0000) >> 16) /  255.0 // red
						,(double)((color & 0xFF00) >> 8) / 255.0 //green
						,(double)(color & 0xFF) / 255.0}; //blue
		return col;
	}
	
	public static double[] hexColorToARGB(int color) {
		double col[] = {(double)((color & 0xFF0000) >> 24) /  255.0  // alpha
						,(double)((color & 0xFF0000) >> 16) /  255.0 // red
						,(double)((color & 0xFF00) >> 8) / 255.0 //green
						,(double)(color & 0xFF) / 255.0}; //blue
		return col;
	}
	
	public static int RGBToHexColor(double r,double g,double b) {
		return RGBToHexColor(r,g,b,1.0);
	}
	
	public static int RGBToHexColor(double r,double g,double b,double a) {
		return  ((int)(a * 0xFF) << 24) + ((int)(r * 0xFF) << 16) + ((int)(g * 0xFF) << 8) + (int)(b * 0xFF);
	}
	
	public void pixel(int x,int y,double r,double g,double b) {
		if( x < 0 || x >= WIDTH ||
				y < 0 || y >= HEIGHT )return;
		int color = ((int)(r * 0xFF) << 16) + ((int)(g * 0xFF) << 8) + (int)(b * 0xFF);
		if(b_useChromaKey && color == i_chromaKeyColor)return;
		pixels[x + y  * WIDTH] = color;
	}
	
	public void pixel(int x,int y,int color){
		if( x < 0 || x >= WIDTH ||
			y < 0 || y >= HEIGHT || (b_useChromaKey && color == i_chromaKeyColor)
			)return;
		
		if(use_z_buffer) {
			if(z_buffer_value >= z_buffer[x + y  * WIDTH]) {
				z_buffer[x + y  * WIDTH] = z_buffer_value;
				pixels[x + y  * WIDTH] = color;
			}
		}
		else {
			int old_col_hex = pixels[x + y  * WIDTH];
			double[] old = getColor(x,y);
			double[] new_c = hexColorToRGB(color);
			double alpha_c = ((double)((old_col_hex>>24)&0xFF))/255.0;//alpha from screen buffor
			double alpha = ((double)((color>>24)&0xFF))/255.0;//alpha from color
			if(alpha > 1)alpha = 1;
			if(alpha > alpha_c)alpha_c = alpha;
			int new_col = RGBToHexColor((new_c[0]*alpha) + (old[0] * (1.0-alpha)),
					(new_c[1]*alpha) + (old[1] * (1.0-alpha))
					, (new_c[2]*alpha) + (old[2] * (1.0-alpha)),alpha_c);
			
			
			pixels[x + y  * WIDTH] = new_col;
		}
	}
	
	public void direct_pixel(int x,int y,int color){
		if( x < 0 || x >= WIDTH ||
				y < 0 || y >= HEIGHT)return;
		pixels[x + y  * WIDTH] = color;
	}
	
	public Screen clone() {
		Screen s = new Screen(WIDTH, HEIGHT);
		for(int i = 0;i < s.pixels.length;i++) {
			s.pixels[i] = pixels[i];
		}
		return s;
	}
	
	public void setScreen(Screen s) {
		for(int i = 0;i < s.pixels.length;i++) {
			pixels[i] = s.pixels[i];
		}
	}

	
}
