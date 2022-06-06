package com.game;

import java.awt.*;

public class GameObj {
	Image img;
	int x;
	int y;
	int width;
	int height;
	double speed;
	GameWindow frame;
	public Image getImg() {
		return img;
	}
	public void setImg(Image img) {
		this.img = img;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public GameWindow getFrame() {
		return frame;
	}
	public void setFrame(GameWindow frame) {
		this.frame = frame;
	}
	public GameObj() {
		
	}
	public GameObj(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public GameObj(Image img, int x, int y, int width, int height, double speed, GameWindow frame) {
		super();
		this.img = img;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.frame = frame;
	}
	public GameObj(Image img, int x, int y, double speed) {
		super();
		this.img = img;
		this.x = x;
		this.y = y;
		this.speed = speed;
	}
	public void paintSelf(Graphics g) {
		g.drawImage(img,x,y,null);
	}
	public void altImg(Image img){
		this.img=img;
	}
	public Rectangle getRec(){//获取自身所在的矩形
		return new Rectangle(x,y,width,height);
	}
}
