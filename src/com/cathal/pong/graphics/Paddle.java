package com.cathal.pong.graphics;

import com.cathal.pong.Level;
import com.cathal.pong.input.Input;
import com.cathal.pong.interfaces.GameComponentI;
import com.cathal.pong.maths.Matrix4f;
import com.cathal.pong.maths.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Paddle implements GameComponentI {

    private Mesh mesh;
    private Vector3f position;

    public static final float WIDTH = 2.5f;
    public static final float HEIGHT = 0.25f;

    private float right_limit = 8.0f;
    private float left_limit = -8.0f;

    public Paddle(Vector3f position) {
        this.position = position;

        float[] positions = new float[]{
                -WIDTH / 2.0f, -HEIGHT / 2.0f, 0.0f,
                -WIDTH / 2.0f, HEIGHT / 2.0f, 0.0f,
                WIDTH / 2.0f, HEIGHT / 2.0f, 0.0f,
                WIDTH / 2.0f, -HEIGHT / 2.0f, 0.0f
        };

        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        this.mesh = new Mesh(positions, indices, null);
    }

    public Paddle() {
        this(new Vector3f(0.0f, 0.0f, 0.0f));
    }

    public Paddle(float x, float y, float z) {
        this(new Vector3f(x, y, z));
    }

    @Override
    public void update() {
        Shader.PADDLE.bind();
        if (Input.isKeyPressed(Level.window, GLFW_KEY_RIGHT)) {
            if (position.x < right_limit) {
                position.x += 0.5f;
            }
        }
        if (Input.isKeyPressed(Level.window, GLFW_KEY_LEFT)) {
            if (position.x > left_limit) {
                position.x -= 0.5f;
            }
        }
        Shader.PADDLE.setUniformMatrix4f("ml_matrix", Matrix4f.translate(position));
    }

    @Override
    public void render() {
        Shader.PADDLE.bind();
        mesh.render();
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }
}
