package com.company;

import org.lwjgl.opengl.GL11;

public class Map {
    int mapX;
    int mapY;
    int mapSize;
    int[] mapDisplay;
    public Map() {
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
    public void print2DMap() { //print 2D version of the map (top-down)
        int xOffset;
        int yOffset;
        for(int y = 0; y < mapY; y++) {
            for (int x = 0; x < mapX; x++) {
                if (mapDisplay[y*mapX+x] == 1) {
                    GL11.glColor3f(1, 1, 1);
                } else {
                    GL11.glColor3f(0,0,0);
                }
                xOffset = x*mapSize;
                yOffset = y*mapSize;
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex2i(xOffset + 1, yOffset + 1);
                GL11.glVertex2i(xOffset + 1 , yOffset + mapSize - 1);;
                GL11.glVertex2i(xOffset + mapSize - 1, yOffset + mapSize - 1);
                GL11.glVertex2i(xOffset + mapSize - 1, yOffset + 1);
                GL11.glEnd();
            }
        }
    }
}
