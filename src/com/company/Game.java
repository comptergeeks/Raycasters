package com.company;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.Scanner;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Game {
    static Player p;
    private static long window;
    private static boolean[] keysDown;
    static GameMap map;
    static int height = 512;
    static int width = 1024;
    static boolean dim3 = false;

    public static void main(String[] args) {
        init();
        render();

    }

    // three options, 2d, 3d, and d
    // set window height based on if boolean is true or not, if the window height is
    private static void init() {
        /*
        Scanner sc = new Scanner(System.in);
        System.out.println("Would you like to start in 2d, 3d or Dev mode (d)");
        String input = sc.nextLine();
        if (input.contains("3d")) {
            dim3 = true;
        }

         */


        GLFW.glfwInit();
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        window = GLFW.glfwCreateWindow(width, height, "Raycaster", 0, 0);
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glClearColor(0.3f, 0.3f, 0.3f, 0f);
        glOrtho(0, width, height, 0, -1, 1);

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
        map = new GameMap();

    }
    private static void render() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            if (!dim3) {
                map.print2DMap();
            }
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
           if
           p.playerX += p.playerDeltaX;
           p.playerY += p.playerDeltaY;
       }
        if (keysDown[GLFW.GLFW_KEY_S]) {
            //System.out.println("down");
            p.playerX -= p.playerDeltaX;
            p.playerY -= p.playerDeltaY;
        }
        if (keysDown[GLFW.GLFW_KEY_A]) {
            //System.out.println("left");
            p.playerAngle -= 0.05f; // Adjust the angle decrement value to change rotation speed
            if (p.playerAngle < 0) {
                p.playerAngle += 2 * (float) Math.PI;
            }
            p.playerDeltaX = (float) Math.cos(p.playerAngle);
            p.playerDeltaY = (float) Math.sin(p.playerAngle);
        }
        if (keysDown[GLFW.GLFW_KEY_D]) {
            //System.out.println("right");
            p.playerAngle += 0.05f; // Adjust the angle increment value to change rotation speed
            if (p.playerAngle > 2 * Math.PI) {
                p.playerAngle -= 2 * (float) Math.PI;
            }
            p.playerDeltaX = (float) Math.cos(p.playerAngle);
            p.playerDeltaY = (float) Math.sin(p.playerAngle);
        }
        createRays();
    }

    private static void drawPlayer() {
        movePlayer();
        //draw 2d player
        if (!dim3) {
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

    }
    private static void createRays() {
        Ray r = new Ray(p, map);
        r.createRay();
    }
}
