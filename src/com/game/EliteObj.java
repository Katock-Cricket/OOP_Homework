package com.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class EliteObj extends GameObj{
	
	int life=5;
	int originlife=5;
	int dead=0;
	@Override
	public void paintSelf(Graphics g) {
		// TODO Auto-generated method stub
		super.paintSelf(g);
		y += speed;
		if(this.getRec().intersects(this.frame.planeobj.getRec())){
			if(GameWindow.mode==4||GameWindow.mode==3) {
				PlaneObj.life-=3;
				ExplodeObj explodeObj = new ExplodeObj(x,y);
				GameUtils.explodeObjList.add(explodeObj);
				GameUtils.removeObjList.add(explodeObj);
				this.x=-200;
				this.y=200;
				GameUtils.removeObjList.add(this);
			}
			else GameWindow.state = 3;
		}
		for(ShellObj shellObj:GameUtils.shellObjList) {
			if(this.getRec().intersects(shellObj.getRec())) {
				life-=PlaneObj.atk;
				shellObj.setX(-100);
				shellObj.setY(100);
				GameUtils.removeObjList.add(shellObj);
			}
			if(life<=0) {
				ExplodeObj explodeObj = new ExplodeObj(x,y);
				GameUtils.explodeObjList.add(explodeObj);
				GameUtils.removeObjList.add(explodeObj);
				this.x=-200;
				this.y=200;
				if(dead==0) {
					GameWindow.score+=5;
					GameWindow.money+=5;
					dead=1;
				}
				GameUtils.removeObjList.add(this);
			}
		}
		g.setColor(Color.white);
		g.fillRect(this.x+10,this.y+50,50,10);
		g.setColor(Color.red);
		g.fillRect(this.x+10,this.y+50,life*50/originlife,10);
		if(y>600) {
			if(life>0&&GameWindow.mode==4) {
				PlaneObj.life-=2;
			}
			this.x=-200;
			this.y=200;
			GameUtils.removeObjList.add(this);
		}
	}

	@Override
	public Rectangle getRec() {
		// TODO Auto-generated method stub
		return super.getRec();
	}
	
	public EliteObj(Image img, int x, int y, int width, int height, double speed, GameWindow frame) {
		super(img, x, y, width, height, speed, frame);
	}
	
	public EliteObj(Image img, int x, int y, int width, int height, double speed, GameWindow frame,int life) {
		this.img = img;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.frame = frame;
		this.life = life;
		this.originlife = life;
	}
}
