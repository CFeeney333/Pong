package com.cathal.pong;

import com.cathal.pong.graphics.Shader;
import com.cathal.pong.maths.Matrix4f;
import com.cathal.pong.utils.Timer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main implements Runnable {

    private int WIDTH = 1280;  // width of the window in pixels
    private int HEIGHT = 720;  // height of the window in pixels
    private long windowID;  // glfw window id

    public static final int TARGET_UPS = 30;
    public static final int TARGET_FPS = 75;

    public static final float LEFT = -8.0f;
    public static final float RIGHT = 8.0f;
    public static final float TOP = 4.5f;
    public static final float BOTTOM = -4.5f;
    public static final float NEAR = -1.0f;
    public static final float FAR = 1.0f;

    private Timer timer;

    private Thread thread;  // main game thread
    private boolean running = false;  // is the game running?

    private Level level;

    public static void main(String[] args) {
        new Main().start();  // Create a Main object and run it
    }

    private void start() {
        running = true;  // set the game running
        thread = new Thread(this, "game");  // Create a new thread from Main
        thread.start();  // Start the thread
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        init();  // Initialize the game
        gameLoop();
    }

    private void gameLoop() {
        float elapsedTime;
        float accumulator = 0.0f;
        float interval = 1.0f / TARGET_UPS;

        while (running) {
            elapsedTime = timer.getElapsedtime();
            accumulator += elapsedTime;

            while (accumulator >= interval) {
                if (update() == -1) {
                    running = false;
                }
                accumulator -= interval;
            }

            render();

            float loopSlot = 1.0f / TARGET_FPS;
            double endTime = timer.getLastLoopTime() + loopSlot;
            while (timer.getTime() < endTime) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.err.println("Thread " + thread.getName() + " interrupted");
                }

            }
            if (glfwWindowShouldClose(windowID) || !running) {  // If the game is quit, stop running
                running = false;
            }
        }
    }

    private int update() {
        glfwPollEvents();
        return level.update();
    }

    public static void checkError() {
        int error = glGetError();
        if (error != GL_NO_ERROR) {
            System.out.println(error);
        }
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);  // Clear the color buffer
        level.render();

        glfwSwapBuffers(windowID);  // Swap the buffers
        checkError();
    }

    private void init() {

        timer = new Timer();
        timer.init();

        // Try to initialize GLFW
        if (!glfwInit()) {
            // TODO: 20/10/2021 handle this
            return;
        }

        // Set some window hints
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Set up the window
        windowID = glfwCreateWindow(WIDTH, HEIGHT, "Pong", NULL, NULL);  // Create window and retrieve id

        // Make sure the window is not null
        if (windowID == NULL) {
            // TODO: 20/10/2021 handle this
            return;
        }

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());  // Get the vidmode from the primary monitor
        glfwSetWindowPos(windowID, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);  // Set position of the window

        // Set the key callback for escape key presses
        glfwSetKeyCallback(windowID, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });

        glfwMakeContextCurrent(windowID);  // make an opengl context current
        GL.createCapabilities();  // initialize openGL

        glActiveTexture(GL_TEXTURE1);

        glfwShowWindow(windowID);  // Make the window visible

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set the opengl clear color

        Shader.loadAll();

        Matrix4f pr_matrix = Matrix4f.orthographic(RIGHT, LEFT, TOP, BOTTOM, NEAR, FAR);
        Shader.BALL.bind();
        Shader.BALL.setUniformMatrix4f("pr_matrix", pr_matrix);

        Shader.BALL.setUniform1i("tex", 1);

        Shader.PADDLE.bind();
        Shader.PADDLE.setUniformMatrix4f("pr_matrix", pr_matrix);

        Shader.BLOCK.bind();
        Shader.BLOCK.setUniformMatrix4f("pr_matrix", pr_matrix);

        Level.initLevel(windowID);
        level = new Level();

        glfwSwapInterval(1);  // Set the swap interval to 1

    }

}
