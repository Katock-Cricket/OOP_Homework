package com.game;

import java.awt.*;
import java.awt.event.*;

public class Reimu extends GameObj{

    public static int life = 10;

    public static int atk = 1;

    @Override
    public void paintSelf(Graphics g) {
        // TODO Auto-generated method stub
        super.paintSelf(g);
        if(this.frame.remilia!=null&&this.getRec().intersects(this.frame.remilia.getRec())) {
            GameWindow.state = 3;
        }
        if(life<=0) {
            GameWindow.state=3;
        }
    }

    @Override
    public Rectangle getRec() {
        // TODO Auto-generated method stub
        return new Rectangle(x,y,width,height);
    }

    public Image getImg() {
        return super.getImg();
    }

    public Reimu() {
        super();
    }

    public Reimu(Image img, int x, int y, int width, int height, double speed, GameWindow frame) {
        super(img, x, y, width, height, speed, frame);
        this.frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Reimu.super.x=e.getX()-width/2;
                Reimu.super.y=e.getY()-height/2;
            }
        });
    }
}
