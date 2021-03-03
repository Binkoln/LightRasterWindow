package lightrasterwindow.demos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import lightrasterwindow.*;

public class Hierarchy {

	public Window w;
	private Screen s = null;
	private Graphics g = null;
	
	private Object root;
	int pole_wybrane  = 0;
	int pole_docelowe  = 0;
	int delta_wybrane_pole = 0;
	boolean put_element_in_or_above = false;
	int tab_px = 40;
	
	private ArrayList<HierarchyIndex> hierarchyIndex = new ArrayList<HierarchyIndex>();
	
	public Hierarchy()
	{
		root = new Object("Root", null);
		root.childs.add(new Object("Obj1", root));
		root.childs.add(new Object("Obj2", root));
		root.childs.get(1).childs.add(new Object("Sub 1", root.childs.get(1)));
		root.childs.get(1).childs.add(new Object("Sub 2", root.childs.get(1)));
		root.childs.get(1).childs.get(1).childs.add(new Object("SubSub 1", root.childs.get(1).childs.get(1)));
		root.childs.get(1).childs.get(1).childs.add(new Object("SubSub 2", root.childs.get(1).childs.get(1)));
		root.childs.get(1).childs.get(1).childs.add(new Object("SubSub 3", root.childs.get(1).childs.get(1)));
		root.childs.get(1).childs.add(new Object("Sub 3", root.childs.get(1)));
		root.childs.get(1).childs.add(new Object("Sub 4", root.childs.get(1)));
		root.childs.get(1).childs.get(3).childs.add(new Object("SubSub 1", root.childs.get(1).childs.get(3)));
		root.childs.get(1).childs.get(3).childs.add(new Object("SubSub 2", root.childs.get(1).childs.get(3)));
		root.childs.get(1).childs.get(3).childs.add(new Object("SubSub 3", root.childs.get(1).childs.get(3)));
		root.childs.get(1).childs.add(new Object("Sub 5", root.childs.get(1)));
		root.childs.add(new Object("Obj3", root));
		root.childs.add(new Object("Obj4", root));
		
		
		
		Runnable update = new Runnable() {
			
			@Override
			public void run() 
			{
				if(Keyboard.getKeyOnce(KeyEvent.VK_A)) 
				{
					
					root.childs.add(pole_docelowe, new Object("Sub 1", null));
				}
				int my = Mouse.Ypixel - 30;
				int mx = Mouse.Xpixel - 10;
				
				if(Keyboard.getKey(KeyEvent.VK_SHIFT)) 
				{
					System.out.println("PD:"+delta_wybrane_pole );
					if(!Mouse.button_left) {
						if(Mouse.button_relesed_clicked_left) 
						{
							//dajemy ograniczenie
							//jeœli zle wybrane to zamiana pol to nie zamienia
							if(!(pole_wybrane < 0 || pole_wybrane >= hierarchyIndex.size() || pole_docelowe < 0 || pole_docelowe >= hierarchyIndex.size() || pole_docelowe == pole_wybrane)) 
							{
							
							
								if(pole_docelowe >= hierarchyIndex.size()) 
								{
									//ta sytuacja nie nastapi bo jest ograniczenie o 1 if wyzej
									root.childs.add(new Object("Hello fello element", root));
								}
								else 
								{
									HierarchyIndex hi_new_place = hierarchyIndex.get(pole_docelowe);
									HierarchyIndex hi_old_place = hierarchyIndex.get(pole_wybrane);
									Object object_to_move = hi_old_place.parent.childs.get(hi_old_place.id_on_parent_list);
									
									
										if(put_element_in_or_above && !object_to_move.incest(hi_new_place.parent.childs.get(hi_new_place.id_on_parent_list).id)) 
										{
											//put in
											object_to_move.parent = hi_new_place.parent.childs.get(hi_new_place.id_on_parent_list);
											hi_new_place.parent.childs.get(hi_new_place.id_on_parent_list).childs.add(object_to_move);
											hi_old_place.parent.childs.remove(hi_old_place.id_on_parent_list);
										}
										else 
										{
											//put above
											object_to_move.parent = hi_new_place.parent;
											hi_old_place.parent.childs.remove(hi_old_place.id_on_parent_list);
											hi_new_place.parent.childs.add(hi_new_place.id_on_parent_list,object_to_move);
										}
										
										
									
									
								}
								
							}
						}
						pole_wybrane = my / 24;
						
						
						
					}
					else 
					{
						delta_wybrane_pole = my - 24*(my/24);
						put_element_in_or_above = delta_wybrane_pole >= 8;
						pole_docelowe = my / 24;
					
					}
				}
				
				if(Keyboard.getKeyOnce(KeyEvent.VK_H))
					for(int i = 0;i < hierarchyIndex.size();i++)
						System.out.println("IDt:" + i + "  " + hierarchyIndex.get(i).get_string());
				
				hierarchyIndex.clear();
				update_chierarchy(root,50,30);
				
				
			}
		};
	
		Runnable render = new Runnable() 
		{
			
			@Override
			public void run() 
			{
				if(s == null)return;
				s.clear(0xff2a2a2a);
				
				
				if(Keyboard.getKey(KeyEvent.VK_SHIFT)) {
					if(Mouse.button_left && pole_wybrane >= 0 && pole_wybrane < hierarchyIndex.size())
					s.frect(hierarchyIndex.get(pole_wybrane).tab_px, 30 + pole_wybrane * 24, 300, 24, 0xff8c9d7a);
					if(Mouse.button_left && pole_docelowe >= 0 && pole_docelowe < hierarchyIndex.size())
						s.frect(hierarchyIndex.get(pole_docelowe).tab_px, 30 + pole_docelowe* 24, 300, (put_element_in_or_above)?24:5, 0xff8c5d4a);
				}
				//render hierarchii
				
				draw_chierarchy(root,50,30);
				
				
				
				
			}
		};
	
		
		Runnable graphics = new Runnable() 
		{
			
			@Override
			public void run() {
				if(s == null)return;
				if(g == null)return;
				
				g.setColor(new Color(0xffffff));
				int font_size = 24;
				g.setFont(new Font("Arial", 0, font_size));
								
			}
		};
		
		w = new Window(1600, 900, 1, "GUI PROTOTYPE", false, render, update, graphics);
		w.STABLE_UPS = true;
		w.STABLE_FPS = true;
		
		s= w.screen;
		g = s.getImage().getGraphics();
	}
	
	public static void main(String[] args) 
	{
		new Hierarchy();

	}

	
	private int draw_chierarchy(Object root,int pos_x,int pos_y) 
	{
		int last_y_iterator = 0;
		int y_iterator = 0;//iterator zwiêkszaj¹cy swoj¹ wielkosc z kazdym dowym elementem zeby przesuwac je w dol
		int y_iter_value = 24;//wielkoœ
		
		
		//g.drawString("Root" , 0 , 24);
		for(int i = 0;i < root.childs.size();i++) {
			
			//rozwijanie listy
			if(root.childs.get(i).childs.size() > 0)
			s.frect(pos_x - 12,4 + pos_y + y_iterator, 12, 22, (root.childs.get(i).show_childs)?0xff259f02:0xff5f2512);
			
			//checbox widocznoœci
			boolean visability = root.childs.get(i).visability();
			
				g.setColor(new Color((visability)?0xffffff:0xaaaaaa));
			
			s.frect(pos_x + 5,4 + pos_y + y_iterator, 17, 22, (root.childs.get(i).visability)?0xff55ff22:0xffff5522);
			g.drawString(root.childs.get(i).name , pos_x + 24,pos_y + 24 + y_iterator);
			
			s.line(pos_x ,pos_y+ 7 + last_y_iterator,pos_x ,pos_y+ 7 + y_iterator, 0xffffffff);
			s.line(pos_x ,pos_y+ 10 +y_iterator,pos_x + 10 ,pos_y+ 10 + y_iterator, 0xffffffff);
			
			
			last_y_iterator = y_iterator ;
			y_iterator += y_iter_value;
			if(root.childs.get(i).childs.size() > 0 && root.childs.get(i).show_childs)
				y_iterator += draw_chierarchy(root.childs.get(i), pos_x + tab_px,pos_y  + y_iterator);
			
		}
		
		return y_iterator;
	}
	
	private boolean aabb(int x1,int y1,int w1,int h1,int x2,int y2,int w2,int h2) 
	{
		return (x1 + w1 > x2 && x1 < x2+w2 && y1 + h1 > y2 && y1 < y2 + h2);
	}
	
	private int update_chierarchy(Object root,int pos_x,int pos_y) 
	{
		
		int y_iterator = 0;//iterator zwiêkszaj¹cy swoj¹ wielkosc z kazdym dowym elementem zeby przesuwac je w dol
		int y_iter_value = 24;//wielkoœ
		
		
		//g.drawString("Root" , 0 , 24);
		for(int i = 0;i < root.childs.size();i++) 
		{
			hierarchyIndex.add(new HierarchyIndex(root.childs.get(i).id,i,root,pos_x));
			if(root.childs.get(i).childs.size() > 0 && Mouse.button_relesed_clicked_left && aabb(Mouse.Xpixel,Mouse.Ypixel,1,1,pos_x - 12,4 + pos_y + y_iterator, 12, 22))
				root.childs.get(i).show_childs = !root.childs.get(i).show_childs;
			
			if(Mouse.button_relesed_clicked_left && aabb(Mouse.Xpixel,Mouse.Ypixel,1,1,pos_x + 5,4 + pos_y + y_iterator, 17, 22))
				root.childs.get(i).visability = !root.childs.get(i).visability;
			
			
			y_iterator += y_iter_value;
			if(root.childs.get(i).childs.size() > 0 && root.childs.get(i).show_childs)
				y_iterator += update_chierarchy(root.childs.get(i), pos_x + tab_px,pos_y  + y_iterator);
			
		}
		
		return y_iterator;
	}
	
	
}//class hierarchy



class Object{
	private static int id_iterator = 0;
	public final int id;
	public String name;
	public boolean show_childs;
	public boolean visability;
	
	public ArrayList<Object> childs;
	public Object parent;
	
	public Object(String name,Object parent) 
	{
		id = id_iterator++;
		this.name = "ID:" + id + " Name:"+name;
		this.parent = parent;
		show_childs = true;
		visability = true;
		childs = new ArrayList<Object>();
	}
	
	public boolean visability() 
	{
		if(parent != null)return (parent.visability() & visability);
		return visability;
	}
	
	public boolean incest(int id) 
	{
		if(parent != null)return (parent.incest(id) || id==this.id);
		return id==this.id;
	}
}

class HierarchyIndex{
	public int id,id_on_parent_list,tab_px;
	public Object parent;
	
	public HierarchyIndex(int id,int id_on_parent_list,Object parent,int tab_px) {
		this.parent = parent;
		this.id = id;
		this.id_on_parent_list = id_on_parent_list;
		this.tab_px = tab_px;
	}
	
	public String get_string() {
		return ("ID:" + id + " ID ol:" + id_on_parent_list);
	}
}

