package com.game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.MemoryImageSource;

import javax.swing.*;

public class GameWindow extends JFrame {
    //游戏状态：0：主页 1：游戏状态 2：暂停 3：游戏失败 4：游戏成功 5：选择模式 6:关中商店 7:选择难度
    public static int state = 0;
    //得分
    public static int score = 0;
    //游戏模式 1:经典 2:关卡 3:生存 4:车万
    public static int mode = 0;
    //飞机身上是否有buff
    public static boolean isbuffed = false;
    //buff种类
    public static int buffkind = 0;
    //目前金钱
    public static int money = 0;
    public static double pi = Math.PI;
    //商店价格
    int price = 0;
    //buff计时
    int buffcount = 0;
    //双缓存
    Image offScreenImg = null;

    int width = 800;
    int height = 1000;
    //难度 -1:简单 0：普通 1：困难
    int difficulty = 0;
    //背景图片
    BgObj bgobj = new BgObj(GameUtils.bgImg, 0, -200, 2);
    //我方飞机
    public PlaneObj planeobj = new PlaneObj(GameUtils.planeImg, 290, 750, 20, 30, 0, this);
    //我方城管
    public Reimu reimu = new Reimu(GameUtils.reimuImg, 290, 750, 10, 15, 0, this);
    //我方子弹
    ShellObj shellobj = new ShellObj(GameUtils.shellImg, planeobj.getX() + 3, planeobj.getY() - 16, 14, 29, 5, this);
    //boss
    public BossObj bossobj = null;
    public Remilia remilia = null;
    //repaint 次数
    int count = 1;
    //敌人计数
    int enemyCount = 0;
    //boss计数
    int bossCount = 1;
    //原始攻击力记录
    int originatk = 1;
    //商店flag
    int shopflag1 = 0;
    int shopflag2 = 0;
    int shopflag3 = 0;
    int bossdecflag = 0;//boss减少20%生命的标志
    //bgm对象
    static Bgm bgm = new Bgm("resources/normal.mp3");

    //启动
    public void launch() {
        //窗口初始化
        this.setVisible(true);
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setTitle("飞机大战");
        //加载蕾米莉亚的6个姿态
        GameUtils.remiliaImgList.add(Toolkit.getDefaultToolkit().getImage("imgs/r1.png"));
        GameUtils.remiliaImgList.add(Toolkit.getDefaultToolkit().getImage("imgs/r2.png"));
        GameUtils.remiliaImgList.add(Toolkit.getDefaultToolkit().getImage("imgs/r3.png"));
        GameUtils.remiliaImgList.add(Toolkit.getDefaultToolkit().getImage("imgs/r4.png"));
        GameUtils.remiliaImgList.add(Toolkit.getDefaultToolkit().getImage("imgs/r5.png"));
        GameUtils.remiliaImgList.add(Toolkit.getDefaultToolkit().getImage("imgs/r6.png"));

        GameUtils.gameObjList.add(bgobj);
//        GameUtils.gameObjList.add(planeobj);
//        GameUtils.gameObjList.add(reimu);
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
                if (e.getButton() == 1) {
                    if (state == 0) {
                        state = 5;
                        repaint();
                    } else if (state == 5) {
                        if (e.getX() >= 310 && e.getX() <= 490) {
                            //监听鼠标位置判断模式
                            if (e.getY() >= 180 && e.getY() <= 260) {
                                state = 7;
                                mode = 2;
                                GameUtils.gameObjList.add(planeobj);
                            } else if (e.getY() >= 400 && e.getY() <= 480) {
                                state = 7;
                                mode = 3;
                                GameUtils.gameObjList.add(planeobj);
                            } else if (e.getY() >= 620 && e.getY() <= 700) {
                                state = 7;
                                mode = 4;
                                GameUtils.gameObjList.add(planeobj);
                            } else if (e.getY() >= 840 && e.getY() <= 920) {
                                state = 7;
                                mode = 5;
                                GameUtils.gameObjList.add(reimu);
                            }
                            repaint();
                        } else if (e.getX() >= 700 && e.getX() <= 800) {
                            if (e.getY() >= 840 && e.getY() <= 900) {
                                state = 0;
                                repaint();
                            }
                        }
                    } else if (state == 3 || state == 4) {
                        price = 0;
                        count = 1;
                        enemyCount = 0;
                        score = 0;
                        difficulty = 0;
                        bossCount = 1;
                        isbuffed = false;
                        buffkind = 0;
                        buffcount = 0;
                        money = 0;
                        originatk = 1;
                        clearObj();
                        if (e.getX() >= 310 && e.getX() <= 490) {
                            if (e.getY() >= 500 && e.getY() <= 580) {
                                state = 0;
                                mode = 0;
                            }
                        }
                        repaint();
                    } else if (state == 6) {
                        count = 1;
                        enemyCount = 0;
                        isbuffed = false;
                        buffkind = 0;
                        buffcount = 0;
                        clearObj();
                        repaint();
                        if (e.getX() >= 300 && e.getX() <= 500) {
                            //监听鼠标位置判断模式
                            if (e.getY() >= 200 && e.getY() <= 280) {
                                if (shopflag1 == 0 && (money >= price)) {
                                    PlaneObj.life += 2;
                                    shopflag1 = 1;
                                    money -= price;
                                }
                            } else if (e.getY() >= 400 && e.getY() <= 480) {
                                if (shopflag2 == 0 && (money >= price)) {
                                    originatk++;
                                    shopflag2 = 1;
                                    money -= price;
                                }
                            } else if (e.getY() >= 600 && e.getY() <= 680) {
                                if (shopflag3 == 0 && (money >= price)) {
                                    bossdecflag = 1;
                                    shopflag3 = 1;
                                    money -= price;
                                }
                            } else if (e.getY() >= 720 && e.getY() <= 750){
                                state = 1;
                                PlaneObj.atk = originatk;
                                shopflag1 = 0;
                                shopflag2 = 0;
                                shopflag3 = 0;
                                GameUtils.gameObjList.add(bgobj);
                                GameUtils.gameObjList.add(planeobj);
                            }
                        }
                        repaint();
                    } else if (state == 7) {
                        if (e.getX() >= 310 && e.getX() <= 490) {
                            //监听鼠标位置判断模式
                            if (e.getY() >= 180 && e.getY() <= 260) {
                                state = 1;
                                difficulty = -1;
                            } else if (e.getY() >= 400 && e.getY() <= 480) {
                                state = 1;
                                difficulty = 0;
                            } else if (e.getY() >= 620 && e.getY() <= 700) {
                                state = 1;
                                difficulty = 1;
                            }
                            PlaneObj.life = 10 - 2 * difficulty;
                            GameUtils.gameObjList.add(bgobj);
                            if(mode==5)
                                GameUtils.gameObjList.add(reimu);
                            else
                                GameUtils.gameObjList.add(planeobj);
                            repaint();
                        } else if (e.getX() >= 700 && e.getX() <= 800) {
                            if (e.getY() >= 840 && e.getY() <= 900) {
                                state = 5;
                                repaint();
                            }
                        }
                    }
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 32) {
                    switch (state) {
                        case 1 -> state = 2;
                        case 2 -> state = 1;
                        default -> {
                        }
                    }
                }
            }
        });

        while (true) {
            if (state == 1) {
                createObj();
                if (PlaneObj.life <= 0 || Reimu.life <= 0) {
                    state = 3;
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

    private void clearObj() {
        GameUtils.shellObjList.clear();
        GameUtils.bulletObjList.clear();
        GameUtils.redBulletList.clear();
        GameUtils.enemyObjList.clear();
        GameUtils.explodeObjList.clear();
        GameUtils.removeObjList.clear();
        GameUtils.gameObjList.clear();
        GameUtils.eliteObjList.clear();
        GameUtils.bonusObjList.clear();
    }

    @Override
    public void paint(Graphics g) {
        if (state!=1)
            showCursor();
        //游戏未开始
        if (offScreenImg == null) {
            offScreenImg = createImage(width, height);
        }
        Graphics gImage = offScreenImg.getGraphics();
        gImage.fillRect(0, 0, width, height);
        if (state == 0) {
            gImage.drawImage(GameUtils.bgImg, 0, 0, this);
            gImage.drawImage(GameUtils.bossImg, 320, 120, this);
            gImage.drawImage(GameUtils.explodeImg, 370, 350, this);
            GameUtils.drawWord(gImage, "开始游戏", Color.yellow, 40, 318, 700);
        }
        //
        if (state == 1) {
            hideCursor();
            System.out.println("state=1");
            //播放对应的音乐
            if (mode == 5 && !bgm.playing) bgm = new Bgm("resources/touhou.mp3");
            if (!bgm.playing) {
                bgm.start();
                System.out.println("bgm start");
                bgm.playing = true;
            }
            if (bossobj != null && bossobj.dead == 1) {
                bossobj = null;
                bossCount++;
                enemyCount = 0;
            }
            if (remilia != null && remilia.dead == 1) {
                remilia = null;
                bossCount++;
                enemyCount = 0;
            }

            gImage.drawImage(GameUtils.bgImg, 0, 0, this);
            if (mode == 5) {
                reimu.altImg(GameUtils.reimuImg);
                gImage.drawImage(GameUtils.reimuImg, reimu.getX(), reimu.getY(), this);
                System.out.println("draw reimu");
            } else {
                gImage.drawImage(GameUtils.planeImg, planeobj.getX(), planeobj.getY(), this);
                System.out.println("draw plane");
            }
            //变换蕾米莉亚的姿态
            if (remilia!=null) {
                remilia.altImg();
                gImage.drawImage(remilia.img, remilia.x, remilia.y, this);
            }

            GameUtils.gameObjList.addAll(GameUtils.explodeObjList);
            for (int i = 0; i < GameUtils.gameObjList.size(); i++) {
                GameUtils.gameObjList.get(i).paintSelf(gImage);
            }
            GameUtils.gameObjList.removeAll(GameUtils.removeObjList);
            GameUtils.drawWord(gImage, "得分: " + score + "分", Color.GREEN, 20, 30, 100);

            if (mode == 1)
                GameUtils.drawWord(gImage, "按下空格键以暂停", Color.red, 20, 30, 900);
            else if (mode == 2)
                GameUtils.drawWord(gImage, "按下空格键以暂停", Color.red, 20, 30, 900);
            else if (mode == 3) {
                GameUtils.drawWord(gImage, "金币: " + money, Color.GREEN, 20, 680, 100);
                GameUtils.drawWord(gImage, "第 " + bossCount + " 关", Color.GREEN, 20, 30, 130);
                drawPause(gImage);
                for (BonusObj bonusobj : GameUtils.bonusObjList) {
                    if (planeobj.getRec().intersects(bonusobj.getRec())) {
                        if (bonusobj.kind == 1) {
                            PlaneObj.life += 2;
                        } else if (bonusobj.kind == 4) {
                            money += 100;
                        } else if (bonusobj.kind != 1 || bonusobj.kind != 4) {
                            GameWindow.isbuffed = true;
                            GameWindow.buffkind = bonusobj.kind;
                        }
                        bonusobj.setX(-600);
                        bonusobj.setY(600);
                        GameUtils.removeObjList.add(bonusobj);
                    }
                }
            } else if (mode == 4) {
                drawPause(gImage);
                for (BonusObj bonusobj : GameUtils.bonusObjList) {
                    if (planeobj.getRec().intersects(bonusobj.getRec())) {
                        if (bonusobj.kind == 1) {
                            PlaneObj.life += 2;
                        } else if (bonusobj.kind != 1) {
                            GameWindow.isbuffed = true;
                            GameWindow.buffkind = bonusobj.kind;
                        }
                        bonusobj.setX(-600);
                        bonusobj.setY(600);
                        GameUtils.removeObjList.add(bonusobj);
                    }
                }
            } else {
                GameUtils.drawWord(gImage, "按下空格键以暂停", Color.red, 20, 30, 900);
            }
        }
        if (state == 3) {
            System.out.println("state=3");
            if (bgm.playing) {
                bgm.stop();
                System.out.println("bgm stopped");
                bgm.playing = false;
            }
            remilia = null;
            gImage.drawImage(GameUtils.bgImg, 0, 0, this);
            bgm = new Bgm("resources/normal.mp3");
            if (planeobj != null)
                gImage.drawImage(GameUtils.explodeImg, planeobj.getX() - 35, planeobj.getY() - 50, this);
            if (reimu != null)
                gImage.drawImage(GameUtils.explodeImg, reimu.getX() - 35, reimu.getY() - 50, this);
            GameUtils.drawWord(gImage, "游戏结束", Color.red, 40, 318, 700);
            drawScore(gImage);
        }
        if (state == 4) {
            if (bgm.playing) {
                bgm.stop();
                bgm.playing = false;
            }
            gImage.drawImage(GameUtils.bgImg, 0, 0, this);
            if (bossobj!=null)
                gImage.drawImage(GameUtils.explodeImg, bossobj.getX() + 30, bossobj.getY(), this);
            else if (remilia!=null)
                gImage.drawImage(GameUtils.explodeImg, remilia.getX() + 30, remilia.getY(), this);
            GameUtils.drawWord(gImage, "通关成功", Color.red, 40, 318, 700);
            drawScore(gImage);
        }
        if (state == 5) {
            if (bgm.playing) {
                bgm.stop();
                bgm.playing = false;
            }
            gImage.drawImage(GameUtils.bgImg, 0, 0, this);
            gImage.setColor(Color.green);
            gImage.fillRect(310, 180, 180, 80);
            gImage.fillRect(310, 400, 180, 80);
            gImage.fillRect(310, 620, 180, 80);
            gImage.fillRect(310, 840, 180, 80);
            gImage.fillRect(700, 840, 100, 60);
            GameUtils.drawWord(gImage, "经典模式", Color.red, 40, 320, 230);
            GameUtils.drawWord(gImage, "关卡模式", Color.red, 40, 320, 450);
            GameUtils.drawWord(gImage, "生存模式", Color.red, 40, 320, 670);
            GameUtils.drawWord(gImage, "车万模式", Color.red, 40, 320, 890);
            GameUtils.drawWord(gImage, "返回", Color.red, 40, 700, 880);
            bossobj = null;
            PlaneObj.life = 10;
            originatk = 1;
            PlaneObj.atk = 1;
            mode = 0;
        }
        if (state == 6) {
            if (bgm.playing) {
                bgm.stop();
                bgm.playing = false;
            }
            gImage.drawImage(GameUtils.bgImg, 0, 0, this);
            price = (20 + 10 * bossCount) * (5 + difficulty) / 5;
            String str = String.valueOf(price);
            GameUtils.drawWord(gImage, "商店", Color.red, 60, 350, 120);
            GameUtils.drawWord(gImage, "金币：" + money, Color.red, 30, 320, 180);
            gImage.setColor(Color.green);
            gImage.fillRect(300, 200, 200, 80);
            gImage.fillRect(300, 400, 200, 80);
            gImage.fillRect(300, 600, 200, 80);
            gImage.fillRect(340, 720, 120, 30);
            GameUtils.drawWord(gImage, "增加2生命值", Color.red, 30, 300, 250);
            GameUtils.drawWord(gImage, str, Color.red, 30, 520, 250);
            if (shopflag1 == 1) GameUtils.drawWord(gImage, "√", Color.red, 30, 640, 250);
            GameUtils.drawWord(gImage, "攻击力+1", Color.red, 30, 300, 450);
            GameUtils.drawWord(gImage, str, Color.red, 30, 520, 450);
            if (shopflag2 == 1) GameUtils.drawWord(gImage, "√", Color.red, 30, 640, 450);
            GameUtils.drawWord(gImage, "BOSS生命-20%", Color.red, 30, 300, 650);
            GameUtils.drawWord(gImage, str, Color.red, 30, 520, 650);
            if (shopflag3 == 1) GameUtils.drawWord(gImage, "√", Color.red, 30, 640, 650);
            GameUtils.drawWord(gImage, "下一关", Color.red, 20, 370, 740);
        }
        if (state == 7) {
            gImage.drawImage(GameUtils.bgImg, 0, 0, this);
            gImage.setColor(Color.green);
            gImage.fillRect(310, 180, 180, 80);
            gImage.fillRect(310, 400, 180, 80);
            gImage.fillRect(310, 620, 180, 80);
            gImage.fillRect(700, 840, 100, 60);
            GameUtils.drawWord(gImage, "简单", Color.red, 40, 360, 230);
            GameUtils.drawWord(gImage, "普通", Color.red, 40, 360, 450);
            GameUtils.drawWord(gImage, "困难", Color.red, 40, 360, 670);
            GameUtils.drawWord(gImage, "返回", Color.red, 40, 700, 880);
        }
        g.drawImage(offScreenImg, 0, 0, this);
        count++;
        if (isbuffed) {
            buffcount++;
            if (buffkind == 2) {
                PlaneObj.atk = originatk * 2;
            }
            if (buffcount == 400) {
                buffcount = 0;
                buffkind = 0;
                PlaneObj.atk = originatk;
                isbuffed = false;
            }
        }
    }

    private void drawScore(Graphics gImage) {
        GameUtils.drawWord(gImage, "得分: " + score + "分", Color.GREEN, 20, 30, 100);
        gImage.setColor(Color.green);
        gImage.fillRect(310, 500, 180, 80);
        GameUtils.drawWord(gImage, "回到首页", Color.red, 40, 320, 550);
        bossobj = null;
    }

    private void drawPause(Graphics gImage) {
        GameUtils.drawWord(gImage, "按下空格键以暂停", Color.red, 20, 30, 850);
        gImage.setColor(Color.white);
        gImage.fillRect(20, 870, 100, 10);
        gImage.setColor(Color.red);
        if (PlaneObj.life <= 10) gImage.fillRect(20, 870, PlaneObj.life * 10, 10);
        else gImage.fillRect(20, 870, 100, 10);
        GameUtils.drawWord(gImage, "life:" + PlaneObj.life + "  atk:" + PlaneObj.atk, Color.red, 20, 560, 880);
    }

    void createObj() {
        //车万模式干脆重画
        if (mode == 5) {
            System.out.println("in mode 5");
            //生成一个无敌的大小姐
            if (count >= 30 && remilia == null) {
                remilia = new Remilia(GameUtils.remiliaImg, 250, 35, 155, 100, 3, this, 300);
                GameUtils.gameObjList.add(remilia);
            }

            //生成大小姐的子弹
            if (count % 15 == 0 && remilia != null) {
                GameUtils.redBulletList.add(new RedBullet(GameUtils.redBulletImg, remilia.getX() + 76, remilia.getY() + 150, 7, 7, 5, this, pi / 9));
                GameUtils.gameObjList.add(GameUtils.redBulletList.get(GameUtils.redBulletList.size() - 1));
                GameUtils.redBulletList.add(new RedBullet(GameUtils.redBulletImg, remilia.getX() + 76, remilia.getY() + 150, 7, 7, 5, this, 2 * pi / 9));
                GameUtils.gameObjList.add(GameUtils.redBulletList.get(GameUtils.redBulletList.size() - 1));
                GameUtils.redBulletList.add(new RedBullet(GameUtils.redBulletImg, remilia.getX() + 76, remilia.getY() + 150, 7, 7, 5, this, 3 * pi / 9));
                GameUtils.gameObjList.add(GameUtils.redBulletList.get(GameUtils.redBulletList.size() - 1));
                GameUtils.redBulletList.add(new RedBullet(GameUtils.redBulletImg, remilia.getX() + 76, remilia.getY() + 150, 7, 7, 5, this, 4 * pi / 9));
                GameUtils.gameObjList.add(GameUtils.redBulletList.get(GameUtils.redBulletList.size() - 1));
                GameUtils.redBulletList.add(new RedBullet(GameUtils.redBulletImg, remilia.getX() + 76, remilia.getY() + 150, 7, 7, 5, this, 5 * pi / 9));
                GameUtils.gameObjList.add(GameUtils.redBulletList.get(GameUtils.redBulletList.size() - 1));
                GameUtils.redBulletList.add(new RedBullet(GameUtils.redBulletImg, remilia.getX() + 76, remilia.getY() + 150, 7, 7, 5, this, 6 * pi / 9));
                GameUtils.gameObjList.add(GameUtils.redBulletList.get(GameUtils.redBulletList.size() - 1));
                GameUtils.redBulletList.add(new RedBullet(GameUtils.redBulletImg, remilia.getX() + 76, remilia.getY() + 150, 7, 7, 5, this, 7 * pi / 9));
                GameUtils.gameObjList.add(GameUtils.redBulletList.get(GameUtils.redBulletList.size() - 1));
                GameUtils.redBulletList.add(new RedBullet(GameUtils.redBulletImg, remilia.getX() + 76, remilia.getY() + 150, 7, 7, 5, this, 8 * pi / 9));
                GameUtils.gameObjList.add(GameUtils.redBulletList.get(GameUtils.redBulletList.size() - 1));
            }
            //生成冈格尼尔之枪
            if (count % 300 == 0 && remilia != null){
                Gungnir gungnir = new Gungnir(GameUtils.gunImg, remilia.getX() + 76, remilia.getY() + 150, 80, 100, 20, this);
                GameUtils.gameObjList.add(gungnir);
            }
            //每重画8次产生2个子弹
            if (count % 8 == 0) {
                GameUtils.shellObjList.add(new ShellObj(GameUtils.shellImg, reimu.getX() - 12, reimu.getY() - 16, 14, 29, 5, this));
                GameUtils.gameObjList.add(GameUtils.shellObjList.get(GameUtils.shellObjList.size() - 1));
                GameUtils.shellObjList.add(new ShellObj(GameUtils.shellImg, reimu.getX() + 18, reimu.getY() - 16, 14, 29, 5, this));
                GameUtils.gameObjList.add(GameUtils.shellObjList.get(GameUtils.shellObjList.size() - 1));
            }

        } else {
            //每重画15次产生1个子弹和飞机
            if (count % 15 == 0) {
                if (buffkind != 3) {
                    GameUtils.shellObjList.add(new ShellObj(GameUtils.shellImg, planeobj.getX() + 3, planeobj.getY() - 16, 14, 29, 5, this));
                    GameUtils.gameObjList.add(GameUtils.shellObjList.get(GameUtils.shellObjList.size() - 1));
                } else if (buffkind == 3) {
                    GameUtils.shellObjList.add(new ShellObj(GameUtils.shellImg, planeobj.getX() - 12, planeobj.getY() - 16, 14, 29, 5, this));
                    GameUtils.gameObjList.add(GameUtils.shellObjList.get(GameUtils.shellObjList.size() - 1));
                    GameUtils.shellObjList.add(new ShellObj(GameUtils.shellImg, planeobj.getX() + 18, planeobj.getY() - 16, 14, 29, 5, this));
                    GameUtils.gameObjList.add(GameUtils.shellObjList.get(GameUtils.shellObjList.size() - 1));
                }
            }
            if (count % (12 - 2 * difficulty) == 0) {
                GameUtils.enemyObjList.add(new EnemyObj(GameUtils.enemyImg, (int) (Math.random() * 16) * 50, 0, 49, 36, 6, this));
                GameUtils.gameObjList.add(GameUtils.enemyObjList.get(GameUtils.enemyObjList.size() - 1));
                if (bossobj == null) enemyCount++;
            }
            if (count % (15 - 3 * difficulty) == 0 && bossobj != null) {
                GameUtils.bulletObjList.add(new BulletObj(GameUtils.bulletImg, bossobj.getX() + 76, bossobj.getY() + 150, 15, 25, 6, this));
                GameUtils.gameObjList.add(GameUtils.bulletObjList.get(GameUtils.bulletObjList.size() - 1));
            }
            if (enemyCount % (30 - 5 * difficulty) == 0 && bossobj == null && enemyCount != 0) {
                bossobj = new BossObj(GameUtils.bossImg, 250, 35, 155, 100, 5, this, bossCount * (10 + 2 * difficulty));
                GameUtils.gameObjList.add(bossobj);
                if (bossdecflag == 1) {
                    bossdecflag = 0;
                    bossobj.life = bossobj.life * 4 / 5;
                }
            }
            if (count % (200 - 40 * difficulty) == 0 && (mode == 3 || mode == 4)) {
                if (mode == 4) {
                    GameUtils.eliteObjList.add(new EliteObj(GameUtils.eliteImg, (int) (Math.random() * 8) * 92 + 30, 0, 68, 51, 4, this));
                    GameUtils.gameObjList.add(GameUtils.eliteObjList.get(GameUtils.eliteObjList.size() - 1));
                } else if (mode == 3) {
                    GameUtils.eliteObjList.add(new EliteObj(GameUtils.eliteImg, (int) (Math.random() * 8) * 92 + 30, 0, 68, 51, 4, this, (3 + 2 * bossCount) * (5 + difficulty) / 5));
                    GameUtils.gameObjList.add(GameUtils.eliteObjList.get(GameUtils.eliteObjList.size() - 1));
                }
            }
            if (count % (800 + 200 * difficulty) == 0 && (mode == 3 || mode == 4)) {
                int kind = (int) (Math.random() * 4) + 1;
                int kind2 = (int) (Math.random() * 4) + 1;
                if (kind == 1) {
                    GameUtils.bonusObjList.add(new BonusObj(GameUtils.lifeImg, (int) (Math.random() * 12) * 50, 0, 50, 50, 5, this, 1));
                    GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size() - 1));
                } else if (kind == 2) {
                    GameUtils.bonusObjList.add(new BonusObj(GameUtils.atkImg, (int) (Math.random() * 12) * 50, 0, 50, 50, 5, this, 2));
                    GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size() - 1));
                } else if (kind == 3) {
                    GameUtils.bonusObjList.add(new BonusObj(GameUtils.doubleImg, (int) (Math.random() * 12) * 50, 0, 50, 50, 5, this, 3));
                    GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size() - 1));
                } else if (kind == 4) {
                    if (kind2 == 1) {
                        GameUtils.bonusObjList.add(new BonusObj(GameUtils.ramdomImg, (int) (Math.random() * 12) * 50, 0, 50, 50, 5, this, 1));
                        GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size() - 1));
                    } else if (kind2 == 2) {
                        GameUtils.bonusObjList.add(new BonusObj(GameUtils.ramdomImg, (int) (Math.random() * 12) * 50, 0, 50, 50, 5, this, 2));
                        GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size() - 1));
                    } else if (kind2 == 3) {
                        GameUtils.bonusObjList.add(new BonusObj(GameUtils.ramdomImg, (int) (Math.random() * 12) * 50, 0, 50, 50, 5, this, 3));
                        GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size() - 1));
                    } else if (kind2 == 4) {
                        GameUtils.bonusObjList.add(new BonusObj(GameUtils.ramdomImg, (int) (Math.random() * 12) * 50, 0, 50, 50, 5, this, 4));
                        GameUtils.gameObjList.add(GameUtils.bonusObjList.get(GameUtils.bonusObjList.size() - 1));
                    }
                }
            }
        }
    }
    public void hideCursor() {
        Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(0, 0, new int[0], 0, 0));
        this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), null));
    }
    public void showCursor() {
        this.setCursor(Cursor.getDefaultCursor());
    }
    public static void main(String[] args) {
        GameWindow window = new GameWindow();
        window.launch();
    }
}
