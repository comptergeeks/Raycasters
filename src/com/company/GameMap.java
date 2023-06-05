package com.company;

import org.lwjgl.opengl.GL11;

public class GameMap {
    int mapX;
    int mapY;
    int mapSize;
    int[] mapDisplay;
    public GameMap() {
        mapX = 8;
        mapY = 8;
        mapSize = 64;
        mapDisplay = new int[]{
                1,1,1,1,1,1,1,1,
                1,0,0,0,0,0,0,1,
                1,0,0,1,0,0,0,1,
                1,0,0,1,0,0,0,1,
                1,0,0,0,0,0,0,1,
                1,0,0,0,0,1,0,1,
                1,0,0,0,0,1,0,1,
                1,1,1,1,1,1,1,1,
        };
    }
    public void print2DMap(int x, int y, int mapX, int mapY, int mapSize, int[] mapDisplay) {
        if (y >= mapY) {
            return;
        }

        if (x >= mapX) {
            print2DMap(0, y + 1, mapX, mapY, mapSize, mapDisplay);
            return;
        }

        if (mapDisplay[y * mapX + x] == 1) {
            GL11.glColor3f(1, 1, 1);
        } else {
            GL11.glColor3f(0, 0, 0);
        }

        int xOffset = x * mapSize;
        int yOffset = y * mapSize;

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(xOffset + 1, yOffset + 1);
        GL11.glVertex2i(xOffset + 1, yOffset + mapSize - 1);
        GL11.glVertex2i(xOffset + mapSize - 1, yOffset + mapSize - 1);
        GL11.glVertex2i(xOffset + mapSize - 1, yOffset + 1);
        GL11.glEnd();

        print2DMap(x + 1, y, mapX, mapY, mapSize, mapDisplay);
    }

    // Call the function like this:

}
