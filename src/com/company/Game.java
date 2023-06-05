package com.company;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Game {
        static Player p;
        private static long window;
        private static Set<Integer> keysDown; // Use a Set instead of a boolean array
        static GameMap map;
        static int height = 512;
        static int width = 1024;
        static boolean dim3 = false;

        public static void main(String[] args) {
            init();
            render();
        }

        private static void init() {
            GLFW.glfwInit();
            GLFW.glfwDefaultWindowHints();
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
            window = GLFW.glfwCreateWindow(width, height, "Raycaster", 0, 0);
            GLFW.glfwMakeContextCurrent(window);
            GL.createCapabilities();
            glClearColor(0.3f, 0.3f, 0.3f, 0f);
            glOrtho(0, width, height, 0, -1, 1);

            keysDown = new HashSet<>(); // Use a HashSet to store pressed keys

            GLFW.glfwSetFramebufferSizeCallback(window, (windowHandle, width, height) -> {
                glViewport(0, 0, width, height);
            });

            GLFW.glfwSetKeyCallback(window, new GLFWKeyCallback() {
                @Override
                public void invoke(long window, int key, int scancode, int action, int mods) {
                    if (action == GLFW_PRESS) {
                        keysDown.add(key); // Add pressed key to the set
                    } else if (action == GLFW_RELEASE) {
                        keysDown.remove(key); // Remove released key from the set
                    }
                }
            });

            p = new Player(300, 300);
            p.setDeltas((float) Math.cos(p.playerAngle * 5), (float) Math.sin(p.playerAngle * 5));
            map = new GameMap();
        }

        private static void render() {
            while (!GLFW.glfwWindowShouldClose(window)) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                map.print2DMap(0, 0, map.mapX, map.mapY, map.mapSize, map.mapDisplay);
                movePlayer();
                drawPlayer();

                glfwSwapBuffers(window);
                glfwPollEvents();
            }

            GLFW.glfwDestroyWindow(window);
            GLFW.glfwTerminate();
        }

        private static void movePlayer() {
            if (keysDown.contains(GLFW_KEY_W)) {
                if (p.checkCollision(map)) {
                    p.playerX += p.playerDeltaX;
                }
                if (p.checkCollision(map)) {
                    p.playerY += p.playerDeltaY;
                }
            }
            if (keysDown.contains(GLFW_KEY_S)) {
                p.playerX -= p.playerDeltaX;
                p.playerY -= p.playerDeltaY;
            }
            if (keysDown.contains(GLFW_KEY_A)) {
                p.playerAngle -= 0.05f;
                if (p.playerAngle < 0) {
                    p.playerAngle += 2 * (float) Math.PI;
                }
                p.playerDeltaX = (float) Math.cos(p.playerAngle);
                p.playerDeltaY = (float) Math.sin(p.playerAngle);
            }
            if (keysDown.contains(GLFW_KEY_D)) {
                p.playerAngle += 0.05f;
                if (p.playerAngle > 2 * Math.PI) {
                    p.playerAngle -= 2 * (float) Math.PI;
                }
                p.playerDeltaX = (float) Math.cos(p.playerAngle);
                p.playerDeltaY = (float) Math.sin(p.playerAngle);
            }
            createRays();
        }

        private static void drawPlayer() {
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