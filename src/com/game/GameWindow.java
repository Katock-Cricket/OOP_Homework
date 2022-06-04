package com.game;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GameWindow extends JFrame{
	//游戏状态：0：主页 1：游戏状态 2：暂停 3：游戏失败 4：游戏成功 5：选择模式 6:关中商店 7:选择难度
	public static int state = 0;
	//得分
	public static int score = 0;
	//游戏模式 1:经典 2:无尽 3:关卡 4:生存
	public static int mode = 0;
	//飞机身上是否有buff
	public static boolean isbuffed = false;
	//buff种类
	public static int buffkind = 0;
	//目前金钱
	public static int money = 0;
	//商店价格
	int price = 0;
	//buff计时
	int buffcount=0;
	//双缓存
	Image offScreenImg=null;
	
	int width=600;
	int height=600;
	//难度 -1:简单 0：普通 1：困难
	int difficulty=0;
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
	//敌人计数
	int enemyCount=0;
	//boss计数
	int bossCount=1;
	//原始攻击力记录
	int originatk=1;
	//商店flag
	int shopflag1=0;
	int shopflag2=0;
	int shopflag3=0;
	int bossdecflag=0;//boss减少20%生命的标志
	//bgm对象
	static Bgm bgm = new Bgm("resources/normal.mp3");
	//启动
	public void launch() {
		//窗口初始化
		this.setVisible(true);
		this.setSize(width,height);
		this.setLocationRelativeTo(null);
		this.setTitle("飞机大战");
		GameUtils.gameObjList.add(bgobj);
		GameUtils.gameObjList.add(planeobj);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
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
								state = 7; 
								mode = 1;
							}
							else if(e.getY()>=200&&e.getY()<=280) {
								state = 7; 
								mode = 2;
							}
							else if(e.getY()>=320&&e.getY()<=400) {
								state = 7; 
								mode = 3;
							}
							else if(e.getY()>=440&&e.getY()<=520) {
								state = 7; 
								mode = 4;
							}
							repaint();
						}
					}
					else if(state==3||state==4) {
						price=0;
						count=1;
						enemyCount=0;
						score=0;
						difficulty=0;
						bossCount=1;
						isbuffed=false;
						buffkind=0;
						buffcount=0;
						money=0;
						originatk=1;
						GameUtils.shellObjList.clear();
						GameUtils.bulletObjList.clear();
						GameUtils.enemyObjList.clear();
						GameUtils.explodeObjList.clear();
						GameUtils.removeObjList.clear();
						GameUtils.gameObjList.clear();
						GameUtils.eliteObjList.clear();
						GameUtils.bonusObjList.clear();
						if(e.getX()>=210&&e.getX()<=390) {
							if(e.getY()>=400&&e.getY()<=480) {
								state = 0; 
								mode = 0;
							}
						}
						repaint();
					}
					else if(state==6) {
						count=1;
						enemyCount=0;
						isbuffed=false;
						buffkind=0;
						buffcount=0;
						GameUtils.shellObjList.clear();
						GameUtils.bulletObjList.clear();
						GameUtils.enemyObjList.clear();
						GameUtils.explodeObjList.clear();
						GameUtils.removeObjList.clear();
						GameUtils.gameObjList.clear();
						GameUtils.eliteObjList.clear();
						GameUtils.bonusObjList.clear();
						repaint();
						if(e.getX()>=210&&e.getX()<=390) {
							//监听鼠标位置判断模式
							if(e.getY()>=200&&e.getY()<=280) {
								if(shopflag1==0&&(money>=price)) {
									PlaneObj.life+=2;
									shopflag1=1;
									money-=price;
								}
							}
							else if(e.getY()>=320&&e.getY()<=400) {
								if(shopflag2==0&&(money>=price)) {
									originatk++;
									shopflag2=1;
									money-=price;
								}
							}
							else if(e.getY()>=440&&e.getY()<=520) {
								if(shopflag3==0&&(money>=price)) {
									bossdecflag=1;
									shopflag3=1;
									money-=price;
								}
							}
							else{
									state = 1;
									PlaneObj.atk=originatk;
									shopflag1=0;
									shopflag2=0;
									shopflag3=0;
								
							}
						}
						repaint();
					}
					else if(state==7) {
						if(e.getX()>=210&&e.getX()<=390) {
							//监听鼠标位置判断模式
							if(e.getY()>=80&&e.getY()<=160) {
								state = 1; 
								difficulty = -1;
							}
							else if(e.getY()>=200&&e.getY()<=280) {
								state = 1; 
								difficulty = 0;
							}
							else if(e.getY()>=320&&e.getY()<=400) {
								state = 1; 
								difficulty = 1;
							}
							PlaneObj.life=10-2*difficulty;
							repaint();
						}
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
			System.out.println("state=1");
			//播放对应的音乐
			if (!bgm.playing) {
				bgm.start();
				System.out.println("bgm start");
				bgm.playing=true;
			}
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
				GameUtils.drawWord(gImage, "经典模式，按下空格键以暂停", Color.red, 20, 30, 550);
			else if(mode==2) 
				GameUtils.drawWord(gImage, "无尽模式，按下空格键以暂停", Color.red, 20, 30, 550);
			else if(mode==3) {
				GameUtils.drawWord(gImage, "金币: "+money, Color.GREEN, 20, 480, 100);
				GameUtils.drawWord(gImage, "第 "+bossCount+" 关", Color.GREEN, 20, 30, 130);
				GameUtils.drawWord(gImage, "关卡模式，按下空格键以暂停", Color.red, 20, 30, 550);
				gImage.setColor(Color.white);
				gImage.fillRect(20,570,100,10);
				gImage.setColor(Color.red);
				if(PlaneObj.life<=10) gImage.fillRect(20,570,PlaneObj.life*10,10);
				else gImage.fillRect(20,570,100,10);
				GameUtils.drawWord(gImage, "life:"+PlaneObj.life+"  atk:"+PlaneObj.atk, Color.red, 20, 360, 580);
				for(BonusObj bonusobj:GameUtils.bonusObjList) {
					if(planeobj.getRec().intersects(bonusobj.getRec())) {
						if(bonusobj.kind==1) {
							PlaneObj.life+=2;
						}
						else if(bonusobj.kind==4) {
							money+=100;
						}
						else if(bonusobj.kind!=1||bonusobj.kind!=4) {
							GameWindow.isbuffed=true;
							GameWindow.buffkind=bonusobj.kind;
						}
						bonusobj.setX(-600);
						bonusobj.setY(600);
						GameUtils.removeObjList.add(bonusobj);
					}
				}
			}
			else if(mode==4) {
				GameUtils.drawWord(gImage, "生存模式，按下空格键以暂停", Color.red, 20, 30, 550);
				gImage.setColor(Color.white);
				gImage.fillRect(20,570,100,10);
				gImage.setColor(Color.red);
				if(PlaneObj.life<=10) gImage.fillRect(20,570,PlaneObj.life*10,10);
				else gImage.fillRect(20,570,100,10);
				GameUtils.drawWord(gImage, "life:"+PlaneObj.life+"  atk:"+PlaneObj.atk, Color.red, 20, 360, 580);
				for(BonusObj bonusobj:GameUtils.bonusObjList) {
					if(planeobj.getRec().intersects(bonusobj.getRec())) {
						if(bonusobj.kind==1) {
							PlaneObj.life+=2;
						}
						else if(bonusobj.kind!=1) {
							GameWindow.isbuffed=true;
							GameWindow.buffkind=bonusobj.kind;
						}
						bonusobj.setX(-600);
						bonusobj.setY(600);
						GameUtils.removeObjList.add(bonusobj);
					}
				}
			}
		}
		if(state==3) {
			System.out.println("state=3");
			if (bgm.playing) {
				bgm.stop();
				System.out.println("bgm stopped");
				bgm.playing=false;
			}
			gImage.drawImage(GameUtils.bgImg, 0, 0, this);
			gImage.drawImage(GameUtils.explodeImg, planeobj.getX()-35, planeobj.getY()-50, this);
			GameUtils.drawWord(gImage,"游戏结束", Color.red, 40, 218, 300);
			GameUtils.drawWord(gImage, "得分: "+score+"分", Color.GREEN, 20, 30, 100);
			gImage.setColor(Color.green);
			gImage.fillRect(210,400,180,80);
			GameUtils.drawWord(gImage, "回到首页", Color.red, 40, 220, 450);
			bossobj=null;
		}
		if(state==4) {
			if (bgm.playing) {
				bgm.stop();
				bgm.playing=false;
			}
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
			if (bgm.playing) {
				bgm.stop();
				bgm.playing=false;
			}
			gImage.drawImage(GameUtils.bgImg, 0, 0, this);
			gImage.setColor(Color.green);
			gImage.fillRect(210, 80, 180, 80);
			gImage.fillRect(210, 200, 180, 80);
			gImage.fillRect(210, 320, 180, 80);
			gImage.fillRect(210, 440, 180, 80);
			GameUtils.drawWord(gImage, "经典模式", Color.red, 40, 220, 130);
			GameUtils.drawWord(gImage, "无尽模式", Color.red, 40, 220, 250);
			GameUtils.drawWord(gImage, "关卡模式", Color.red, 40, 220, 370);
			GameUtils.drawWord(gImage, "生存模式", Color.red, 40, 220, 490);
			bossobj=null;
			PlaneObj.life=10;
			originatk=1;
			PlaneObj.atk=1;
			mode=0;
		}
		if(state==6) {
			if (bgm.playing) {
				bgm.stop();
				bgm.playing=false;
			}
			gImage.drawImage(GameUtils.bgImg, 0, 0, this);
			price = (20 + 10 * bossCount) * (5 + 1 * difficulty) / 5;
			String str = String.valueOf(price);
			GameUtils.drawWord(gImage, "商店", Color.red, 60, 250, 120);
			GameUtils.drawWord(gImage, "金币："+money, Color.red, 30, 220, 180);
			gImage.setColor(Color.green);
			gImage.fillRect(200, 200, 200, 80);
			gImage.fillRect(200, 320, 200, 80);
			gImage.fillRect(200, 440, 200, 80);
			gImage.fillRect(240, 540, 120, 30);
			GameUtils.drawWord(gImage, "增加2生命值", Color.red, 30, 200, 250);
			GameUtils.drawWord(gImage, str , Color.red, 30, 420, 250);
			if(shopflag1==1) GameUtils.drawWord(gImage, "√" , Color.red, 30, 540, 250);
			GameUtils.drawWord(gImage, "攻击力+1", Color.red, 30, 200, 370);
			GameUtils.drawWord(gImage, str , Color.red, 30, 420, 370);
			if(shopflag2==1) GameUtils.drawWord(gImage, "√" , Color.red, 30, 540, 370);
			GameUtils.drawWord(gImage, "BOSS生命-20%", Color.red, 30, 200, 490);
			GameUtils.drawWord(gImage, str , Color.red, 30, 420, 490);
			if(shopflag3==1) GameUtils.drawWord(gImage, "√" , Color.red, 30, 540, 490);
			GameUtils.drawWord(gImage, "下一关", Color.red, 20, 270, 560);
		}
		if(state==7) {
			gImage.drawImage(GameUtils.bgImg, 0, 0, this);
			gImage.setColor(Color.green);
			gImage.fillRect(210, 80, 180, 80);
			gImage.fillRect(210, 200, 180, 80);
			gImage.fillRect(210, 320, 180, 80);
			GameUtils.drawWord(gImage, "简单", Color.red, 40, 260, 130);
			GameUtils.drawWord(gImage, "普通", Color.red, 40, 260, 250);
			GameUtils.drawWord(gImage, "困难", Color.red, 40, 260, 370);
		}
		if(bossobj!=null) {
			GameUtils.drawWord(gImage, "life"+bossobj.life, Color.red, 20, 500, 80);
		}
		g.drawImage(offScreenImg,0,0,this);
		count++;
		if(isbuffed==true) {
			buffcount++;
			if(buffkind==2) {
				PlaneObj.atk=originatk*2;
			}
			if(buffcount==400) {
				buffcount=0;
				buffkind=0;
				PlaneObj.atk=originatk;
				isbuffed=false;
			}
		}
	}
	
	void createObj() {
		//每重画15次产生1个子弹和飞机
		if(count%15==0) {
			if(buffkind!=3) {
				GameUtils.shellObjList.add(new ShellObj(GameUtils.shellImg,planeobj.getX()+3,planeobj.getY()-16,14,29,5,this));
				GameUtils.gameObjList.add(GameUtils.shellObjList.get(GameUtils.shellObjList.size()-1));
			}
			else if(buffkind==3) {
				GameUtils.shellObjList.add(new ShellObj(GameUtils.shellImg,planeobj.getX()-12,planeobj.getY()-16,14,29,5,this));
				GameUtils.gameObjList.add(GameUtils.shellObjList.get(GameUtils.shellObjList.size()-1));
				GameUtils.shellObjList.add(new ShellObj(GameUtils.shellImg,planeobj.getX()+18,planeobj.getY()-16,14,29,5,this));
				GameUtils.gameObjList.add(GameUtils.shellObjList.get(GameUtils.shellObjList.size()-1));
			}
		}
		if(count%(15-3*difficulty)==0) {
			GameUtils.enemyObjList.add(new EnemyObj(GameUtils.enemyImg,(int)(Math.random()*12)*50,0,49,36,5,this));
			GameUtils.gameObjList.add(GameUtils.enemyObjList.get(GameUtils.enemyObjList.size()-1));
			if(bossobj==null) enemyCount++;
		}
		if(count%(15-3*difficulty)==0 && bossobj!=null) {
			GameUtils.bulletObjList.add(new BulletObj(GameUtils.bulletImg,bossobj.getX()+76,bossobj.getY()+150,15,25,5,this));
			GameUtils.gameObjList.add(GameUtils.bulletObjList.get(GameUtils.bulletObjList.size()-1));
		}
		if(enemyCount%(20-4*difficulty)==0 && bossobj==null && enemyCount!=0) {
			bossobj = new BossObj(GameUtils.bossImg,250,35,155,100,5,this,bossCount*(10+2*difficulty));
			GameUtils.gameObjList.add(bossobj);
			if(bossdecflag==1) {
				bossdecflag=0;
				bossobj.life=bossobj.life*4/5;
			}
		}
		if(count%(200-40*difficulty)==0 && (mode == 3|| mode == 4)) {
			if(mode==4) {
				GameUtils.eliteObjList.add(new EliteObj(GameUtils.eliteImg,(int)(Math.random()*6)*90+30,0,68,51,3,this));
				GameUtils.gameObjList.add(GameUtils.eliteObjList.get(GameUtils.eliteObjList.size()-1));
			}
			else if(mode==3) {
				GameUtils.eliteObjList.add(new EliteObj(GameUtils.eliteImg,(int)(Math.random()*6)*90+30,0,68,51,3,this,(3+2*bossCount)*(5+difficulty)/5));
				GameUtils.gameObjList.add(GameUtils.eliteObjList.get(GameUtils.eliteObjList.size()-1));
			}
		}
		if(count%(800+200*difficulty)==0 && (mode == 3|| mode == 4)) {
			int kind = (int)(Math.random()*4)+1;
			int kind2 = (int)(Math.random()*4)+1;
			if(kind==1) {
				GameUtils.bonusObjList.add(new BonusObj(GameUtils.lifeImg,(int)(Math.random()*12)*50,0,50,50,5,this,1));
				GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size()-1));
			}
			else if(kind==2) {
				GameUtils.bonusObjList.add(new BonusObj(GameUtils.atkImg,(int)(Math.random()*12)*50,0,50,50,5,this,2));
				GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size()-1));
			}
			else if(kind==3) {
				GameUtils.bonusObjList.add(new BonusObj(GameUtils.doubleImg,(int)(Math.random()*12)*50,0,50,50,5,this,3));
				GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size()-1));
			}
			else if(kind==4) {
				if(kind2==1) {
					GameUtils.bonusObjList.add(new BonusObj(GameUtils.ramdomImg,(int)(Math.random()*12)*50,0,50,50,5,this,1));
					GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size()-1));
				}
				else if(kind2==2) {
					GameUtils.bonusObjList.add(new BonusObj(GameUtils.ramdomImg,(int)(Math.random()*12)*50,0,50,50,5,this,2));
					GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size()-1));
				}
				else if(kind2==3) {
					GameUtils.bonusObjList.add(new BonusObj(GameUtils.ramdomImg,(int)(Math.random()*12)*50,0,50,50,5,this,3));
					GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size()-1));
				}
				else if(kind2==3) {
					GameUtils.bonusObjList.add(new BonusObj(GameUtils.ramdomImg,(int)(Math.random()*12)*50,0,50,50,5,this,4));
					GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size()-1));
				}
			}
		}
	}
	
	public static void main(String args[]) {
		GameWindow window = new GameWindow();
		window.launch();
	}
}