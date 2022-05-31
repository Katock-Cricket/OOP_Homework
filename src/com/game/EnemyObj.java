package com.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class EnemyObj extends GameObj{

	@Override
	public void paintSelf(Graphics g) {
		// TODO Auto-generated method stub
		super.paintSelf(g);
		y += speed;
		if(this.getRec().intersects(this.frame.planeobj.getRec())){
			if(GameWindow.mode==4) {
				PlaneObj.life-=2;
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
				ExplodeObj explodeObj = new ExplodeObj(x,y);
				GameUtils.explodeObjList.add(explodeObj);
				GameUtils.removeObjList.add(explodeObj);
				shellObj.setX(-100);
				shellObj.setY(100);
				this.x=-200;
				this.y=200;
				GameWindow.score++;
				GameUtils.removeObjList.add(shellObj);
				GameUtils.removeObjList.add(this);
			}
		}
		if(y>600) {
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
	
	public EnemyObj() {
		super();
	}
	
	public EnemyObj(Image img, int x, int y, int width, int height, double speed, GameWindow frame) {
		super(img, x, y, width, height, speed, frame);
	}
}
