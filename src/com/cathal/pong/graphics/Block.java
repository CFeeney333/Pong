package com.cathal.pong.graphics;

import com.cathal.pong.interfaces.GameComponentI;
import com.cathal.pong.maths.Matrix4f;
import com.cathal.pong.maths.Vector3f;

public class Block implements GameComponentI {

    private Mesh mesh;
    public static final float WIDTH = 2.0f, HEIGHT = 0.5f;
    private Vector3f position;
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }


    @Override
    public void update() {
        // Shader.BLOCK.bind();
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    @Override
    public void render() {
        Shader.BLOCK.bind();
        Shader.BLOCK.setUniformMatrix4f("ml_matrix", Matrix4f.translate(position));
        mesh.render();
    }

    public Block(Vector3f position) {
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

    public Block() {
        this(new Vector3f(0.0f, 0.0f, 0.0f));
    }

    public Block(float x, float y, float z) {
        this(new Vector3f(x, y, z));
    }
}
