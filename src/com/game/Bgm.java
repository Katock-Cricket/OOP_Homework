package com.game;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Bgm implements Runnable {

    private final String file;
    private AdvancedPlayer player = null;
    private Thread thread = null;
    boolean playing=false;
    public Bgm(String file) {
        this.file = file;
    }

    public void run() {
        // 每次开始需要重新创建AdvancedPlayer，否则会报错
        createPlayer();
        play();
    }

    public void start() {
        thread = new Thread(this, "Player thread");
        thread.start();
        System.out.println("real started");
    }

    public void stop() {
        System.out.println("in stop void");
        player.close();
        System.out.println("stopped player");
        thread = null;
    }

    protected void play() {
        try {
            player.play();
        } catch (JavaLayerException ex) {
            System.err.println("Problem playing audio: " + ex);
        }
    }

    protected void createPlayer() {
        try {
            try {
                player = new AdvancedPlayer(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            // 这里设置一个监听器，来监听停止事件
            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent event) {
                    // 当播放完毕后,会触发该事件,再次调用start()即可!
                    start();
                }
            });
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }
}