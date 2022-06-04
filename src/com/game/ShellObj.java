package com.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ShellObj extends GameObj{

	@Override
	public Image getImg() {
		// TODO Auto-generated method stub
		return super.getImg();
	}

	@Override
	public void paintSelf(Graphics g) {
		// TODO Auto-generated method stub
		super.paintSelf(g);
		y -= speed;
		if(y<0) {
			this.x=-100;
			this.y=100;
			GameUtils.removeObjList.add(this);
		}
	}

	@Override
	public Rectangle getRec() {
		// TODO Auto-generated method stub
		return super.getRec();
	}
	
	public ShellObj() {
		super();
	}
	
	public ShellObj(Image img, int x, int y, int width, int height, double speed, GameWindow frame) {
		super(img, x, y, width, height, speed, frame);
	}
}
