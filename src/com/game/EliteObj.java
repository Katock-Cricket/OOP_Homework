package com.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class EliteObj extends GameObj{
	
	int life=5;
	
	@Override
	public void paintSelf(Graphics g) {
		// TODO Auto-generated method stub
		super.paintSelf(g);
		y += speed;
		if(this.getRec().intersects(this.frame.planeobj.getRec())){
			if(GameWindow.mode==4) {
				PlaneObj.life-=3;
				ExplodeObj explodeObj = new ExplodeObj(x,y);
				GameUtils.explodeObjList.add(explodeObj);
				GameUtils.removeObjList.add(explodeObj);
				this.x=-200;
				this.y=200;
				GameWindow.score+=5;
				GameUtils.removeObjList.add(this);
			}
			else GameWindow.state = 3;
		}
		for(ShellObj shellObj:GameUtils.shellObjList) {
			if(this.getRec().intersects(shellObj.getRec())) {
				life--;
				shellObj.setX(-100);
				shellObj.setY(100);
				GameUtils.removeObjList.add(shellObj);
			}
			if(life==0) {
				ExplodeObj explodeObj = new ExplodeObj(x,y);
				GameUtils.explodeObjList.add(explodeObj);
				GameUtils.removeObjList.add(explodeObj);
				this.x=-200;
				this.y=200;
				GameWindow.score+=5;
				GameUtils.removeObjList.add(this);
			}
		}
		g.setColor(Color.white);
		g.fillRect(this.x+10,this.y+50,50,10);
		g.setColor(Color.red);
		g.fillRect(this.x+10,this.y+50,life*10,10);
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
}
