package com.game;

import java.awt.*;

public class BgObj extends GameObj{
	public BgObj() {
		super();
	}

	public BgObj(Image img,int x,int y,double speed) {
		super(img,x,y,speed);
	}

	@Override
	public void paintSelf(Graphics g) {
		// TODO Auto-generated method stub
		super.paintSelf(g);
		y+=speed;
		if(y>=0) {
			y = -2000;
		}
	}
	
}
