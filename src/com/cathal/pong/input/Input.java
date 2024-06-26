package com.cathal.pong.input;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    public static boolean isKeyPressed(long window, int keycode) {
        return glfwGetKey(window, keycode) == GLFW_PRESS;
    }

    public static boolean isKeyReleased(long window, int keycode) {
        return glfwGetKey(window, keycode) == GLFW_RELEASE;
    }
}
