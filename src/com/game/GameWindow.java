package com.game;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GameWindow extends JFrame{
	//游戏状态：0：主页 1：游戏状态 2：暂停 3：游戏失败 4：游戏成功 5：选择模式
	public static int state = 0;
	//得分
	public static int score = 0;
	//游戏模式 1:经典 2:无尽 4:生存
	public static int mode = 0;
	Image offScreenImg=null;
	int width=600;
	int height=600;
	//背景图片
	BgObj bgobj=new BgObj(GameUtils.bgImg,0,-2000,2); 
	//我方飞机
	public PlaneObj planeobj=new PlaneObj(GameUtils.planeImg,290,550,20,30,0,this); 
	//我方子弹
	ShellObj shellobj=new ShellObj(GameUtils.shellImg,planeobj.getX()+3,planeobj.getY()-16,14,29,5,this); 
	//boss
	public BossObj bossobj = null;
	//repaint 次数
	int count=1;
	int enemyCount=0;
	int bossCount=1;
	//启动
	public void launch() {
		//窗口初始化
		this.setVisible(true);
		this.setSize(width,height);
		this.setLocationRelativeTo(null);
		this.setTitle("飞机大战");
		GameUtils.gameObjList.add(bgobj);
		GameUtils.gameObjList.add(planeobj);
		//鼠标点击事件
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//鼠标左键点击开始
				if(e.getButton()==1) {
					if(state==0) {
						state = 5;
						repaint();
					}
					else if(state==5) {
						if(e.getX()>=210&&e.getX()<=390) {
							//监听鼠标位置判断模式
							if(e.getY()>=80&&e.getY()<=160) {
								state = 1; 
								mode = 1;
							}
							else if(e.getY()>=200&&e.getY()<=280) {
								state = 1; 
								mode = 2;
							}
							else if(e.getY()>=440&&e.getY()<=520) {
								state = 1; 
								mode = 4;
							}
							repaint();
						}
					}
					else if(state==3||state==4) {
						count=1;
						enemyCount=0;
						score=0;
						bossCount=1;
						GameUtils.shellObjList.clear();
						GameUtils.bulletObjList.clear();
						GameUtils.enemyObjList.clear();
						GameUtils.explodeObjList.clear();
						GameUtils.removeObjList.clear();
						GameUtils.gameObjList.clear();
						if(e.getX()>=210&&e.getX()<=390) {
							if(e.getY()>=400&&e.getY()<=480) {
								state = 0; 
								mode = 0;
							}
						}
						repaint();
					}
				}
			}
		});
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==32) {
					switch(state) {
						case 1:
							state=2;
							break;
						case 2:
							state=1;
							break;
						default:
					}
				}
			}
		});
		
		while(true) {
			if(state==1) {
				createObj();
				if(PlaneObj.life<=0) {
					state=3;
				}
				repaint();
			}
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		//游戏未开始
		if(offScreenImg==null) {
			offScreenImg=createImage(width,height);
		}
		Graphics gImage = offScreenImg.getGraphics();
		gImage.fillRect(0, 0, width, height);
		if(state==0) {
			gImage.drawImage(GameUtils.bgImg, 0, 0, this);
			gImage.drawImage(GameUtils.bossImg, 220, 120, this);
			gImage.drawImage(GameUtils.explodeImg, 270, 350, this);
			GameUtils.drawWord(gImage,"开始游戏", Color.yellow, 40, 218, 300);
		}
		
		//
		if(state==1) {
			if(bossobj!=null&&bossobj.dead==1) {
				bossobj=null;
				bossCount++;
				enemyCount=0;
			}
			gImage.drawImage(GameUtils.bgImg, 0, 0, this);
			gImage.drawImage(GameUtils.planeImg,planeobj.getX(),planeobj.getY(),this);
			GameUtils.gameObjList.addAll(GameUtils.explodeObjList);
			for(int i=0;i<GameUtils.gameObjList.size();i++) {
				GameUtils.gameObjList.get(i).paintSelf(gImage);
			}
			GameUtils.gameObjList.removeAll(GameUtils.removeObjList);
			GameUtils.drawWord(gImage, "得分: "+score+"分", Color.GREEN, 20, 30, 100);
			
			if(mode==1) 
				GameUtils.drawWord(gImage, "经典模式，按下空格键以暂停", Color.red, 20, 30, 570);
			else if(mode==2) 
				GameUtils.drawWord(gImage, "无尽模式，按下空格键以暂停", Color.red, 20, 30, 570);
			else if(mode==4) {
				GameUtils.drawWord(gImage, "生存模式，按下空格键以暂停", Color.red, 20, 30, 570);
				gImage.setColor(Color.white);
				gImage.fillRect(20,570,100,10);
				gImage.setColor(Color.red);
				gImage.fillRect(20,570,PlaneObj.life*10,10);
			}
		}
		if(state==3) {
			gImage.drawImage(GameUtils.bgImg, 0, 0, this);
			gImage.drawImage(GameUtils.explodeImg, planeobj.getX()-35, planeobj.getY()-50, this);
			GameUtils.drawWord(gImage,"游戏失败", Color.red, 40, 218, 300);
			GameUtils.drawWord(gImage, "得分: "+score+"分", Color.GREEN, 20, 30, 100);
			gImage.setColor(Color.green);
			gImage.fillRect(210,400,180,80);
			GameUtils.drawWord(gImage, "回到首页", Color.red, 40, 220, 450);
			bossobj=null;
		}
		if(state==4) {
			gImage.drawImage(GameUtils.bgImg, 0, 0, this);
			gImage.drawImage(GameUtils.explodeImg, bossobj.getX()+30, bossobj.getY(), this);
			GameUtils.drawWord(gImage,"通关成功", Color.red, 40, 218, 300);
			GameUtils.drawWord(gImage, "得分: "+score+"分", Color.GREEN, 20, 30, 100);
			gImage.setColor(Color.green);
			gImage.fillRect(210,400,180,80);
			GameUtils.drawWord(gImage, "回到首页", Color.red, 40, 220, 450);
			bossobj=null;
		}
		if(state==5) {
			gImage.drawImage(GameUtils.bgImg, 0, 0, this);
			gImage.setColor(Color.green);
			gImage.fillRect(210, 80, 180, 80);
			gImage.fillRect(210, 200, 180, 80);
			gImage.fillRect(210, 440, 180, 80);
			GameUtils.drawWord(gImage, "经典模式", Color.red, 40, 220, 130);
			GameUtils.drawWord(gImage, "无尽模式", Color.red, 40, 220, 250);
			GameUtils.drawWord(gImage, "生存模式", Color.red, 40, 220, 490);
			bossobj=null;
			PlaneObj.life=10;
			mode=0;
		}
		g.drawImage(offScreenImg,0,0,this);
		count++;
	}
	
	void createObj() {
		//每重画15次产生1个子弹和飞机
		if(count%15==0) {
			GameUtils.shellObjList.add(new ShellObj(GameUtils.shellImg,planeobj.getX()+3,planeobj.getY()-16,14,29,5,this));
			GameUtils.gameObjList.add(GameUtils.shellObjList.get(GameUtils.shellObjList.size()-1));
		}
		if(count%15==0) {
			GameUtils.enemyObjList.add(new EnemyObj(GameUtils.enemyImg,(int)(Math.random()*12)*50,0,49,36,5,this));
			GameUtils.gameObjList.add(GameUtils.enemyObjList.get(GameUtils.enemyObjList.size()-1));
			if(bossobj==null) enemyCount++;
		}
		if(count%15==0 && bossobj!=null) {
			GameUtils.bulletObjList.add(new BulletObj(GameUtils.bulletImg,bossobj.getX()+76,bossobj.getY()+150,15,25,5,this));
			GameUtils.gameObjList.add(GameUtils.bulletObjList.get(GameUtils.bulletObjList.size()-1));
		}
		if(enemyCount%20==0 && bossobj==null && enemyCount!=0) {
			bossobj = new BossObj(GameUtils.bossImg,250,35,155,100,5,this,bossCount*10);
			GameUtils.gameObjList.add(bossobj);
		}
		if(count%150==0 && mode == 4) {
			GameUtils.eliteObjList.add(new EliteObj(GameUtils.eliteImg,(int)(Math.random()*6)*90+30,0,68,51,3,this));
			GameUtils.gameObjList.add(GameUtils.eliteObjList.get(GameUtils.eliteObjList.size()-1));
		}
	}
	
	public static void main(String args[]) {
		GameWindow window = new GameWindow();
		window.launch();
	}
}
