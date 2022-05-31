package com.game;

import java.awt.*;

public class ExplodeObj extends GameObj{
	
	static Image[] pic = new Image[16];
	
	int explodeCount=0;
	
	static {
		for(int i=1;i<=pic.length;i++) {
			pic[i-1]=Toolkit.getDefaultToolkit().getImage("imgs/explode/e"+i+".gif");
		}
	}
	
	public ExplodeObj(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void paintSelf(Graphics g) {
		// TODO Auto-generated method stub
		if(explodeCount<16) {
			img = pic[explodeCount];
			super.paintSelf(g);
			explodeCount++;
		}
	}
	
}
