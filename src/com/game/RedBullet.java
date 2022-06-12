package com.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class RedBullet extends GameObj{
    //对于蕾米莉亚的子弹，要实现一个放射状
    double originX, originY;
    double alpha;
    int cnt=0;

    @Override
    public void paintSelf(Graphics g) {
        // TODO Auto-generated method stub
        super.paintSelf(g);
        this.x = (int)(this.originX + cnt*Math.cos(alpha));
        this.y = (int)(this.originY + cnt*Math.sin(alpha));
        cnt += 4;
        if(this.getRec().intersects(this.frame.planeobj.getRec())) {
            if(GameWindow.mode==4||GameWindow.mode==3) {
                PlaneObj.life-=5;
                this.x=-300;
                this.y=300;
                GameUtils.removeObjList.add(this);
            }
            else GameWindow.state = 3;
        }
        if(y>1000) {
            this.x=-300;
            this.y=300;
            GameUtils.removeObjList.add(this);
        }
    }

    @Override
    public Rectangle getRec() {
        // TODO Auto-generated method stub
        return super.getRec();
    }

    public RedBullet(Image img, int x, int y, int width, int height, double speed, GameWindow frame, double alpha) {
        super(img, x, y, width, height, speed, frame);
        this.originX=x;
        this.originY=y;
        this.alpha=alpha;
    }
}
