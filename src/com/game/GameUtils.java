package com.game;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class GameUtils {
	//背景图片
	public static Image bgImg = Toolkit.getDefaultToolkit().getImage("imgs/bg.jpg");
	
	public static Image bossImg = Toolkit.getDefaultToolkit().getImage("imgs/boss.png");
	
	public static Image explodeImg = Toolkit.getDefaultToolkit().getImage("imgs/explode/e6.gif");
	
	public static Image planeImg = Toolkit.getDefaultToolkit().getImage("imgs/plane.png");
	
	public static Image shellImg = Toolkit.getDefaultToolkit().getImage("imgs/shell.png");
	
	public static Image enemyImg = Toolkit.getDefaultToolkit().getImage("imgs/enemy.png");
	
	public static Image bulletImg = Toolkit.getDefaultToolkit().getImage("imgs/bullet.png");
	
	public static Image eliteImg = Toolkit.getDefaultToolkit().getImage("imgs/Elite.png");
	
	public static Image lifeImg = Toolkit.getDefaultToolkit().getImage("imgs/healthobj.png");
	
	public static Image atkImg = Toolkit.getDefaultToolkit().getImage("imgs/ATKobj.png");
	
	public static Image doubleImg = Toolkit.getDefaultToolkit().getImage("imgs/doubleobj.png");

	public static Image ramdomImg = Toolkit.getDefaultToolkit().getImage("imgs/randomobj.png");
	public static Image redBulletImg = Toolkit.getDefaultToolkit().getImage("imgs/redBullet.png");
	public static Image reimuImg = Toolkit.getDefaultToolkit().getImage("imgs/reimu.png");
	public static Image remiliaImg = Toolkit.getDefaultToolkit().getImage("imgs/remilia.png");
	
	public static List<ShellObj> shellObjList = new ArrayList<>();
	
	public static List<GameObj> gameObjList = new ArrayList<>();
	
	public static List<EnemyObj> enemyObjList = new ArrayList<>();
	
	public static List<ExplodeObj> explodeObjList = new ArrayList<>();
	
	public static List<GameObj> removeObjList = new ArrayList<>();
	
	public static List<BulletObj> bulletObjList = new ArrayList<>();
	
	public static List<EliteObj> eliteObjList = new ArrayList<>();
	
	public static List<BonusObj> bonusObjList = new ArrayList<>();
	
	public static void drawWord(Graphics g,String str,Color color,int size,int x,int y) {
		g.setColor(color);
		g.setFont(new Font("宋体",Font.BOLD,size));
		g.drawString(str, x, y);
	}
}
