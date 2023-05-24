package com.company;

import org.lwjgl.opengl.GL11;

public class Ray {
    int ray;
    int mapX;
    int mapY;
    int map;
    int depthOfField;

    float rayAngle;
    float rayX;
    float rayY;
    float xOffset;
    float yOffset;

    Player p;
    Map m;
    public Ray(Player p, Map m) {
        this.p = p;
        this.m = m;
    }

    public void createRay() {
        rayAngle = p.playerAngle;
        for (int ray = 0; ray < 1; ray++) {
            depthOfField = 0;
            float inverseTan = (float) (-1 / Math.tan(rayAngle));
            if (rayAngle > Math.PI) { //ray looking down
                rayY = (float) ((((int) p.playerY >> 6) << 6) - 0.0001); //bitshifting for accuracy
                rayX = (p.playerX - rayY) * inverseTan + p.playerX;
                yOffset = -64;
                xOffset = -yOffset * inverseTan;
            } else if (rayAngle < Math.PI) { //ray looking up
                rayY = (float) ((((int) p.playerY >> 6) << 6) + 64); //bitshifting for accuracy
                rayX = (p.playerX - rayY) * inverseTan + p.playerX;
                yOffset = 64;
                xOffset = -yOffset * inverseTan;
            }

            //ray horizontal
            if (rayAngle == (float) 0 || rayAngle == (float) Math.PI) {
                rayX = p.playerX;
                rayY = p.playerY;
                depthOfField = 8;
            }
            while (depthOfField < 8) {
                mapX = (int) (rayX)>>6;
                mapY = (int) (rayY)>>6;
                map = mapY*m.mapX + mapX;
                if (map > 0 && map < m.mapX * m.mapY && m.mapDisplay[map] == 1)  { //wall hit
                    depthOfField = 8;
                    //System.out.println("HIT");
                } else {
                    rayX += xOffset;
                    rayY += yOffset;
                    depthOfField+=1; //next line
                }
                drawRay();
            }
        }
    }
    public void drawRay() {
        //System.out.println("Ray Angle: " + rayAngle);
        //System.out.println("Player Angle: " + p.playerAngle);
        GL11.glColor3f(0, 1,0);
        GL11.glLineWidth(1);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2i((int) p.playerX, (int) p.playerY);
        GL11.glVertex2i((int) rayX, (int) rayY);
        GL11.glEnd();

    }
}
