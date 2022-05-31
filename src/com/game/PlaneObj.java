package com.game;

import java.awt.*;
import java.awt.event.*;

public class PlaneObj extends GameObj{
	
	public static int life = 10;

	@Override
	public void paintSelf(Graphics g) {
		// TODO Auto-generated method stub
		super.paintSelf(g);
		if(this.frame.bossobj!=null&&this.getRec().intersects(this.frame.bossobj.getRec())) {
			GameWindow.state = 3;
		}
		if(life<=0) {
			GameWindow.state=3;
		}
	}

	@Override
	public Rectangle getRec() {
		// TODO Auto-generated method stub
		return super.getRec();
	}

	public Image getImg() {
		return super.getImg();
	}
	
	public PlaneObj() {
		super();
	}
	
	public PlaneObj(Image img, int x, int y, int width, int height, double speed, GameWindow frame) {
		super(img, x, y, width, height, speed, frame);
		this.frame.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				PlaneObj.super.x=e.getX()-11;
				PlaneObj.super.y=e.getY()-16;
			}
		});
	}
}
