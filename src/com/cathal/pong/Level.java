package com.cathal.pong;

import com.cathal.pong.graphics.Ball;
import com.cathal.pong.graphics.Block;
import com.cathal.pong.graphics.Paddle;
import com.cathal.pong.input.Input;
import com.cathal.pong.interfaces.GameComponentI;
import com.cathal.pong.graphics.Mesh;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class Level {

    private Map<String, GameComponentI> gameComponents = new HashMap<>();
    public Mesh triangle;
    public static long window;
    private Ball ball;
    private int blocksWidth = 6;
    private int blocksHeight = 3;
    private float blockGap = 0.05f;

    public Level() {
        gameComponents.put("ball", new Ball());
        gameComponents.put("paddle", new Paddle(0.0f, -4.0f, 0.0f));
        float startx = (-((blockGap + Block.WIDTH) * blocksWidth) / 2.0f) + Block.WIDTH / 2.0f;
        float starty = 2.0f;
        for (int x = 0; x < blocksWidth; x++) {
            for (int y = 0; y < blocksHeight; y++) {
                float offsetX = x * (Block.WIDTH + blockGap);
                float offsetY = y * (Block.HEIGHT + blockGap);
                gameComponents.put("block" + x + y, new Block(startx + offsetX, starty + offsetY, 0.0f));
            }
        }
    }

    public static void initLevel(long window) {
        Level.window = window;
    }

    public void render() {
        for (GameComponentI gc : gameComponents.values()) {
            gc.render();
        }
    }

    public int update() {


        for (GameComponentI gc : gameComponents.values()) {
            gc.update();
        }

        if (Input.isKeyPressed(window, GLFW_KEY_SPACE)) {
            Ball b = (Ball) gameComponents.get("ball");
            b.setSpeed(0.2f);
        }
        // we have to create a holder for the names because we can't remove the things from the map
        // in the for loop without causing a "ConcurrentModificationException" :(
        // [i had to wait a whole 16 secs for dat to compile]
        ArrayList<String> toRemove = new ArrayList<>();
        for (String name : gameComponents.keySet()) {
            if (name.startsWith("block")) {
                Block block = (Block) gameComponents.get(name);
                if (blockCollision(block)) {
                    toRemove.add(name);
                }
            }
        }
        // now we can remove them and jawver is 'appy
        for (String name : toRemove) {
            gameComponents.remove(name);
        }
        paddleCollision();
        wallCollision();
        if (isFloorCollision()) {
            return -1;
        }
        return 1;
    }

    private boolean blockCollision(Block block) {
        Ball b = (Ball) gameComponents.get("ball");

        float bx = b.getX();
        float by = b.getY();
        float blockx = block.getX();
        float blocky = block.getY();

        float bx0 = bx - Ball.SIZE / 2.0f;
        float bx1 = bx + Ball.SIZE / 2.0f;
        float by0 = by - Ball.SIZE / 2.0f;
        float by1 = by + Ball.SIZE / 2.0f;

        float blockx0 = blockx - Block.WIDTH / 2.0f;
        float blockx1 = blockx + Block.WIDTH / 2.0f;
        float blocky0 = blocky - Block.HEIGHT / 2.0f;
        float blocky1 = blocky + Block.HEIGHT / 2.0f;

        if ((bx1 > blockx0 && bx0 < blockx1) && by < blocky1 && by > blocky0) {
            b.bounceX();
            return true;
        }
        if ((by1 > blocky0 && by0 < blocky1) && (bx < blockx1 && bx > blockx0)) {
            b.bounceY();
            return true;
        }
        return false;
    }

    private void paddleCollision() {
        Ball b = (Ball) gameComponents.get("ball");
        Paddle p = (Paddle) gameComponents.get("paddle");

        float bx = b.getX();
        float by = b.getY();
        float px = p.getX();
        float py = p.getY();

        float bx0 = bx - Ball.SIZE / 2.0f;
        float bx1 = bx + Ball.SIZE / 2.0f;
        float by0 = by - Ball.SIZE / 2.0f;
        float by1 = by + Ball.SIZE / 2.0f;

        float px0 = px - Paddle.WIDTH / 2.0f;
        float px1 = px + Paddle.WIDTH / 2.0f;
        float py0 = py - Paddle.HEIGHT / 2.0f;
        float py1 = py + Paddle.HEIGHT / 2.0f;

        if ((bx1 > px0 && bx0 < px1) && by < py1 && by > py0) {
            b.bounceX();
        }
        if ((by1 > py0 && by0 < py1) && (bx < px1 && bx > px0)) {
            b.bounceY();
        }
    }

    private void wallCollision() {
        Ball b = (Ball) gameComponents.get("ball");

        float bx0 = b.getX() - Ball.SIZE / 2.0f;
        float bx1 = b.getX() + Ball.SIZE / 2.0f;
        float by1 = b.getY() + Ball.SIZE / 2.0f;

        if (bx0 <= Main.LEFT || bx1 >= Main.RIGHT) {
            b.bounceX();
        }
        if (by1 >= Main.TOP) {
            b.bounceY();
        }

    }

    private boolean isFloorCollision() {
        Ball b = (Ball) gameComponents.get("ball");
        float by0 = b.getY() - Ball.SIZE / 2.0f;
        if (by0 <= Main.BOTTOM) {
            return true;
        }
        return false;
    }
}
