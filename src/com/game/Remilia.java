package com.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Remilia extends GameObj{

    int life;
    int dead = 0;
    int originlife;
    @Override
    public void paintSelf(Graphics g) {
        // TODO Auto-generated method stub
        super.paintSelf(g);
        if(x > 800 || x < -50) {
            speed = - speed;
        }
        x+=speed;
        for(ShellObj shellobj:GameUtils.shellObjList) {
            if(this.getRec().intersects(shellobj.getRec())) {
                shellobj.setX(-100);
                shellobj.setY(100);
                GameUtils.removeObjList.add(shellobj);
                life-=PlaneObj.atk;
            }
            if(life<=0) {
                life=-1;
                if(dead==0) {
                    GameWindow.score+=100;
                    GameWindow.money+=100;
                }
                dead=1;
                ExplodeObj explodeObj = new ExplodeObj(x,y);
                GameUtils.explodeObjList.add(explodeObj);
                GameUtils.removeObjList.add(explodeObj);
                this.x=-200;
                this.y=200;
                GameUtils.removeObjList.add(this);
                if(GameWindow.mode==1) {
                    GameWindow.state=4;
                }
                else if(GameWindow.mode==3) {
                    GameWindow.state=6;
                }
            }
        }
        g.setColor(Color.white);
        g.fillRect(20,40,100,10);
        g.setColor(Color.red);
        g.fillRect(20,40,life*100/originlife,10);
    }

    @Override
    public Rectangle getRec() {
        // TODO Auto-generated method stub
        return super.getRec();
    }

    public Remilia(Image img, int x, int y, int width, int height, double speed, GameWindow frame) {
        super(img, x, y, width, height, speed, frame);
    }

    public Remilia(Image img, int x, int y, int width, int height, double speed, GameWindow frame, int life) {
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
