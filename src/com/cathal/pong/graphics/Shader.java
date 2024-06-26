package com.cathal.pong.graphics;

import com.cathal.pong.maths.Matrix4f;
import com.cathal.pong.utils.ShaderUtils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private boolean enabled;

    private final int shaderID;
    private final Map<String, Integer> uniformLocationCache = new HashMap<>();

    public static Shader BALL, PADDLE, BLOCK;

    public Shader(String vertShaderPath, String fragShaderPath) {
        shaderID = ShaderUtils.load(vertShaderPath, fragShaderPath);
    }

    public static void loadAll() {
        BALL = new Shader("shaders/ball.vert", "shaders/ball.frag");
        PADDLE = new Shader("shaders/paddle.vert", "shaders/paddle.frag");
        BLOCK = new Shader("shaders/block.vert", "shaders/block.frag");
    }

    public void setUniformMatrix4f(String name, Matrix4f matrix) {
        glUniformMatrix4fv(getUniformLocation(name), false, matrix.toFloatBuffer());
    }

    public void setUniform1i(String name, int value) {
        glUniform1i(getUniformLocation(name), value);
    }

    private int getUniformLocation(String name) {
        if (uniformLocationCache.containsKey(name)) {
            return uniformLocationCache.get(name);
        }
        int location = glGetUniformLocation(shaderID, name);
        if (location == -1) {
            System.out.println("Could not find uniform " + name);
        }
        uniformLocationCache.put(name, location);
        return location;
    }

    public void bind() {
        glUseProgram(shaderID);
        enabled = true;
    }

    public void unbind() {
        glUseProgram(0);
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
