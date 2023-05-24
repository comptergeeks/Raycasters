package com.company;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Game {
    static Player p;
    private static long window;
    private static boolean[] keysDown;
    static Map map;

    public static void main(String[] args) {
        init();
        render();

    }

    private static void init() {
        GLFW.glfwInit();
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        window = GLFW.glfwCreateWindow(1024, 512, "Raycaster", 0, 0);
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glClearColor(0.3f, 0.3f, 0.3f, 0f);
        glOrtho(0, 1024, 512, 0, -1, 1);

        keysDown = new boolean[GLFW.GLFW_KEY_LAST]; //set up keys down

        GLFW.glfwSetFramebufferSizeCallback(window, (windowHandle, width, height) -> {
            glViewport(0, 0, width, height);
        });

        GLFW.glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key >= 0 && key < keysDown.length) {
                    keysDown[key] = action != GLFW.GLFW_RELEASE;
                }
            }
        });


        p = new Player(300, 300);
        p.setDeltas((float) Math.cos(p.playerAngle*5), (float) Math.sin(p.playerAngle*5));
        map = new Map();

    }
    private static void render() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            map.print2DMap();
            createRays();
            drawPlayer();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
    private static void movePlayer() {
       if (keysDown[GLFW.GLFW_KEY_W]) {
           //System.out.println("up");
           p.playerX += p.playerDeltaX;
           p.playerY += p.playerDeltaY;
       }
        if (keysDown[GLFW.GLFW_KEY_A]) {
            //System.out.println("left");
            p.playerAngle -=0.025; //maybe add sensitivity option that can be changed
            if (p.playerAngle < 0) {
             p.playerAngle += 2*Math.PI;
            }
            p.playerDeltaX = (float) Math.cos(p.playerAngle*5);
            p.playerDeltaY = (float) Math.sin(p.playerAngle*5);
        }
        if (keysDown[GLFW.GLFW_KEY_S]) {
            //System.out.println("down");
            p.playerX -= p.playerDeltaX;
            p.playerY -= p.playerDeltaY;
        }
        if (keysDown[GLFW.GLFW_KEY_D]) {
            //System.out.println("right");
            p.playerAngle +=0.025;
            if (p.playerAngle > 2*Math.PI) {
                p.playerAngle -= 2*Math.PI ;
            }
            p.playerDeltaX = (float) Math.cos(p.playerAngle*5);
            p.playerDeltaY = (float) Math.sin(p.playerAngle*5);
        }
    }

    private static void drawPlayer() {
        movePlayer();
        GL11.glColor3f(1, 1, 0);
        GL11.glPointSize(8);
        GL11.glBegin(GL_POINTS);
        GL11.glVertex2i((int) p.playerX, (int) p.playerY);
        GL11.glEnd();

        GL11.glLineWidth(3);
        GL11.glBegin(GL_LINES);
        glVertex2i((int) p.playerX, (int) p.playerY);
        glVertex2i((int) (p.playerX + p.playerDeltaX * 5), (int) (p.playerY + p.playerDeltaY * 5));
        glEnd();

    }
    private static void createRays() {
        Ray r = new Ray(p, map);
        r.createRay();
    }
}
