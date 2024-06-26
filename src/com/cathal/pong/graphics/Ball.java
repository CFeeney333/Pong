package com.cathal.pong.graphics;

import com.cathal.pong.interfaces.GameComponentI;
import com.cathal.pong.maths.Matrix4f;
import com.cathal.pong.maths.Vector2f;
import com.cathal.pong.maths.Vector3f;

public class Ball implements GameComponentI {

    private final Mesh mesh;
    private final Vector3f position;
    private final Vector2f velocity;

    private final Texture texture;

    private float speed = 0.0f;

    public static final float SIZE = 1.0f;

    public Ball(Vector3f position) {
        this.position = position;
        this.velocity = new Vector2f(0, 0);
        this.texture = new Texture("res/bird.png");

        float[] vertexPositions = new float[]{
                -SIZE / 2.0f, -SIZE / 2.0f, 0.0f,
                -SIZE / 2.0f,  SIZE / 2.0f, 0.0f,
                 SIZE / 2.0f,  SIZE / 2.0f, 0.0f,
                 SIZE / 2.0f, -SIZE / 2.0f, 0.0f
        };

        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        float[] texCoords = new float[]{
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        mesh = new Mesh(vertexPositions, indices, texCoords);
        velocity.x = speed;
        velocity.y = speed;
    }

    public Ball() {
        this(new Vector3f(0, 0, 0));
    }

    public Ball(float x, float y, float z) {
        this(new Vector3f(x, y, z));
    }


    public void render() {
        Shader.BALL.bind();
        //
        texture.bind();
        mesh.render();
        Shader.BALL.unbind();
    }

    public void update() {
        Shader.BALL.bind();
        position.x += velocity.x;
        position.y += velocity.y;
        Shader.BALL.setUniformMatrix4f("ml_matrix", Matrix4f.translate(position));
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void bounceX() {
        velocity.x *= -1;
    }

    public void bounceY() {
        velocity.y *= -1;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        if (this.velocity.x >= 0) {
            this.velocity.x = speed;
        } else {
            this.velocity.x = -speed;
        }

        if (this.velocity.y >= 0) {
            this.velocity.y = speed;
        } else {
            this.velocity.y = -speed;
        }
    }
}
