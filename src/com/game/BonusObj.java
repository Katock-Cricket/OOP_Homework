package com.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class BonusObj extends GameObj{
	
	int kind;
	
	@Override
	public void paintSelf(Graphics g) {
		// TODO Auto-generated method stub
		super.paintSelf(g);
		y += speed;
		if(y>1000) {
			this.x=-600;
			this.y=600;
			GameUtils.removeObjList.add(this);
		}
	}

	@Override
	public Rectangle getRec() {
		// TODO Auto-generated method stub
		return super.getRec();
	}
	
	public BonusObj() {
		super();
	}
	
	public BonusObj(Image img, int x, int y, int width, int height, double speed, GameWindow frame, int kind) {
		this.img = img;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.frame = frame;
		this.kind = kind;
	}
}
