package lightrasterwindow;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener{

	public static final int Count = 300;
	private static boolean keys[] = new boolean[Count];
	private static boolean keys_prev[] = new boolean[Count];
	
	private static String text_buffer = "";
	private static boolean textInsertMode = false;
	public static void setTextInsertMode(boolean b) {
		textInsertMode = b;
	}
	
	public static String getTextBuffer() {return text_buffer;}
	public static void clearTextBuffer() { text_buffer = "";}
	
	public Keyboard(){
		for(int i = 0; i < Count;i++)
			keys[i] = false;
		for(int i = 0; i < Count;i++)
			keys_prev[i] = false;
	}
	
	public void keyPressed(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = true;
		
	}

	
	public void keyReleased(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = false;
	}

	public void update(){
		for(int i = 0; i < Count;i++){
			if(!keys[i])
				keys_prev[i] = false;
		}
		
		if(textInsertMode) {
			for(int i = 32;i < Count;i++) {
				int avoid_list[] = {KeyEvent.VK_ENTER,KeyEvent.VK_ESCAPE,KeyEvent.VK_ALT,KeyEvent.VK_CONTROL};
				for(int j = 0;j < avoid_list.length;j++)
					if(i == avoid_list[j])continue;

				if(getKeyOnce(i)) {
					if(i <= 126) {
						if(i == ' ')
							text_buffer += ' ';
						else if(getKey(KeyEvent.VK_SHIFT) && i >= 65 && i <= 90)
							text_buffer += (char)i;
						else if(i >= 65 && i <= 90)
							text_buffer += (char)(i + 32);
						else if(i >= 48 && i <=57 && getKey(KeyEvent.VK_SHIFT)){
							char a = ' ';
							switch(i) {
							case 48: a = ')';break;
							case 49: a = '!';break;
							case 50: a = '@';break;
							case 51: a = '#';break;
							case 52: a = '$';break;
							case 53: a = '%';break;
							case 54: a = '^';break;
							case 55: a = '&';break;
							case 56: a = '*';break;
							case 57: a = '(';break;
							case 58: a = ')';break;
							
							
							}
							text_buffer += a;
						}
						else
							text_buffer += (int)i;
					}
				}
			}
		}
		
	}
	
	public static boolean getKey(int Key){
		return keys[Key];
	}
	
	public static boolean getKeyOnce(int Key){
		if(!keys_prev[Key] && keys[Key]){
			keys_prev[Key] = true;
			return true;
		}
		
		return false;
	}
	
	public void keyTyped(KeyEvent arg0) {}
		

}
