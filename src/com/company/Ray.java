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
            //horizontal lines
            depthOfField = 0;
            float distanceHorizontal = 1000000;
            float horizontalX = p.playerX;
            float horizontalY = p.playerY;
            float rayAngleRadians = rayAngle % (2 * (float) Math.PI);
            if (rayAngleRadians < 0) {
                rayAngleRadians += 2 * (float) Math.PI;
            }
            float inverseTan = (float) (-1 / Math.tan(rayAngleRadians));
            // Determine the initial ray direction based on the ray angle
            float deltaX = (rayAngleRadians >= Math.PI && rayAngleRadians < 2 * Math.PI) ? -1 : 1;
            float deltaY = (rayAngleRadians >= Math.PI / 2 && rayAngleRadians < 3 * Math.PI / 2) ? -1 : 1;

            if (rayAngleRadians > Math.PI && rayAngleRadians < 2 * Math.PI) { // ray looking down
                rayY = (float) ((((int) p.playerY >> 6) << 6) - 0.0001); // bitshifting for accuracy
                rayX = p.playerX + (p.playerY - rayY) * inverseTan;
                yOffset = -64;
                xOffset = -yOffset * inverseTan;
                deltaX = -deltaX; // Invert the initial ray direction
            } else if (rayAngleRadians >= 0 && rayAngleRadians < Math.PI) { // ray looking up
                rayY = (float) ((((int) p.playerY >> 6) << 6) + 64); // bitshifting for accuracy
                rayX = p.playerX + (p.playerY - rayY) * inverseTan;
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
                    horizontalX  = rayX;
                    horizontalY = rayY;
                    distanceHorizontal = distance(p.playerX, p.playerY, horizontalX, horizontalY, rayAngle);
                    //System.out.println("HIT");
                } else {
                    rayX += xOffset;
                    rayY += yOffset;
                    depthOfField+=1; //next line
                }
            }
            //vertical line
            depthOfField = 0;
            float distanceVertical = 1000000;
            float verticalX = p.playerX;
            float verticalY = p.playerY;
            //float rayAngleRadians = rayAngle % (2 * (float) Math.PI);
            if (rayAngleRadians < 0) {
                rayAngleRadians += 2 * (float) Math.PI;
            }
            float negativeTan = (float) (-Math.tan(rayAngleRadians));
            float p2 = (float) (Math.PI/2);
            float p3 = (float) (3*Math.PI/2);

            if (rayAngleRadians > p2 && rayAngleRadians < p3) { // ray looking left
                rayX = (float) ((((int) p.playerY >> 6) << 6) - 0.0001); // bitshifting for accuracy
                rayY = p.playerY + (p.playerX - rayX) * negativeTan;
                xOffset = -64;
                yOffset = -xOffset * negativeTan;
                deltaY = -deltaY; // Invert the initial ray direction
            } else if (rayAngleRadians < p2 || rayAngleRadians > p3) { // ray looking right
                rayX = (float) ((((int) p.playerX >> 6) << 6) + 64); // bitshifting for accuracy
                rayY = p.playerY + (p.playerX - rayX) * negativeTan;
                xOffset = 64;
                yOffset = -xOffset * negativeTan;
            }

            while (depthOfField < 8) {
                mapX = (int) (rayX)>>6;
                mapY = (int) (rayY)>>6;
                map = mapY*m.mapX + mapX;
                if (map > 0 && map < m.mapX * m.mapY && m.mapDisplay[map] == 1)  { //wall hit
                    depthOfField = 8;

                    verticalX  = rayX;
                    verticalY = rayY;
                    distanceVertical = distance(p.playerX, p.playerY, verticalX, verticalY, rayAngle);
                } else {
                    rayX += xOffset;
                    rayY += yOffset;
                    depthOfField+=1; //next line
                }
            }
            if(distanceVertical < distanceHorizontal) {
                rayX = verticalX;
                rayY = verticalY;
            } else if(distanceHorizontal < distanceVertical) {
                rayX = horizontalX;
                rayY = horizontalY;
            }
            drawRay();
        }
    }
    public void drawRay() {
        //System.out.println("Ray Angle: " + rayAngle);
        GL11.glColor3f(1, 0,0);
        GL11.glLineWidth(1);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2i((int) p.playerX, (int) p.playerY);
        GL11.glVertex2i((int) rayX, (int) rayY);
        GL11.glEnd();
        System.out.println(rayAngle);
    }
    public float distance(float ax, float ay, float bx, float by, float ang) {
        return (float ) (Math.sqrt((bx - ax) * (bx -ax) + (by- ay) * (by - ay)));

    }
}
