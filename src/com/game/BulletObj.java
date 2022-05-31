package com.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class BulletObj extends GameObj{

	@Override
	public void paintSelf(Graphics g) {
		// TODO Auto-generated method stub
		super.paintSelf(g);
		y += speed;
		if(this.getRec().intersects(this.frame.planeobj.getRec())) {
			if(GameWindow.mode==4) {
				PlaneObj.life-=5;
			}
			else GameWindow.state = 3;
		}
		if(y>600) {
			this.x=-300;
			this.y=300;
			GameUtils.removeObjList.add(this);
		}
	}

	@Override
	public Rectangle getRec() {
		// TODO Auto-generated method stub
		return super.getRec();
	}
	
	public BulletObj(Image img, int x, int y, int width, int height, double speed, GameWindow frame) {
		super(img, x, y, width, height, speed, frame);
	}
}
