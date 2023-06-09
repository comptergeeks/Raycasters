package com.company;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;


public class Ray {
    int mapX;
    int mapY;
    int map;
    int depthOfField;
    //int centerRayIndex;
    int numRays;

    //float rayAngle;
    float rayX;
    float rayY;
    float xOffset;
    float yOffset;
    float finalDistance;


    //add keys up from main class, and then after create ray constructor


    float oneDegToRad = (float) 0.0174533;

    Player p;
    GameMap m;
    public Ray(Player p, GameMap m) {
        this.p = p;
        this.m = m;
    }

    public void createRay() {
            int numRays = 120; // Adjust the number of rays as desired
            float angleStep = (float) Math.PI / (3 * numRays); // Calculate the angle step size

            ArrayList<Float> rayAngles = new ArrayList<>();

            for (int rayIndex = 0; rayIndex < numRays; rayIndex++) {
                float rayAngle = p.playerAngle - (float) Math.PI / 6 + rayIndex * angleStep;
                rayAngles.add(rayAngle);
            }

            for (float rayAngle : rayAngles) {
                //float rayAngle = p.playerAngle - (float) Math.PI / 6 + rayAngles.indexOf(rayAngle) * angleStep;

                // horizontal lines
                depthOfField = 0;
                float distanceHorizontal = Float.POSITIVE_INFINITY; // Initialize with a large value
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
                    } else {
                        rayX += xOffset;
                        rayY += yOffset;
                        depthOfField+=1; //next line
                    }
                }
                //vertical line
                depthOfField = 0;
                float distanceVertical = Float.POSITIVE_INFINITY; // Initialize with a large value
                float verticalX = p.playerX;
                float verticalY = p.playerY;
                rayAngleRadians = rayAngle % (2 * (float) Math.PI);
                if (rayAngleRadians < 0) {
                    rayAngleRadians += 2 * (float) Math.PI;
                }
                float negativeTan = (float) (-Math.tan(rayAngleRadians));

                if (rayAngleRadians > Math.PI / 2 && rayAngleRadians < 3 * Math.PI / 2) { // ray looking left
                    rayX = (float) ((((int) p.playerX >> 6) << 6) - 0.0001); // bitshifting for accuracy
                    rayY = p.playerY + (p.playerX - rayX) * negativeTan;
                    xOffset = -64;
                    yOffset = -xOffset * negativeTan;
                    deltaY = -deltaY; // Invert the initial ray direction
                } else if (rayAngleRadians < Math.PI / 2 || rayAngleRadians > 3 * Math.PI / 2) { // ray looking right
                    rayX = (float) ((((int) p.playerX >> 6) << 6) + 64); // bitshifting for accuracy
                    rayY = p.playerY + (p.playerX - rayX) * negativeTan;
                    xOffset = 64;
                    yOffset = -xOffset * negativeTan;
                }

                while (depthOfField < 8) {
                    mapX = (int) (rayX) >> 6;
                    mapY = (int) (rayY) >> 6;
                    map = mapY * m.mapX + mapX;
                    if (map > 0 && map < m.mapX * m.mapY && m.mapDisplay[map] == 1) { // wall hit
                        depthOfField = 8;
                        verticalX = rayX;
                        verticalY = rayY;
                        distanceVertical = distance(p.playerX, p.playerY, verticalX, verticalY, rayAngle);
                    } else {
                        rayX += xOffset;
                        rayY += yOffset;
                        depthOfField += 1; // next line
                    }
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
                //distance vertical and horizontal (setting to walls)
                float colorChange = 0;
                if(distanceVertical < distanceHorizontal) {
                    colorChange = 0.9f; //darker
                    rayX = verticalX;
                    rayY = verticalY;
                    finalDistance = distanceVertical;
                } else if(distanceHorizontal < distanceVertical) {
                    colorChange = 0.7f; //lighter
                    rayX = horizontalX;
                    rayY = horizontalY;
                    finalDistance = distanceHorizontal;
                }
                drawRay(rayAngles.indexOf(rayAngle), colorChange);

                //drawing 3d Scene
                //320 w x 160 h
                int height = 0;
                int width = 160;
                if (!Game.dim3) {
                    height = 320;
                } else {
                    height = 512;
                }
                float lineHeight = (m.mapSize * height)/finalDistance;
                if (lineHeight > height) {
                    lineHeight = height;
                }
                float cosAngle = p.playerAngle - rayAngle;
                if (cosAngle < 0) {
                    cosAngle += 2 * Math.PI;
                }
                if (cosAngle > 2*Math.PI) {
                    rayAngle -= 2* Math.PI;
                }
                finalDistance = (float) (finalDistance*Math.cos(cosAngle)); // fish eye effect fixing
                float lineOffset = width - lineHeight/2;
                GL11.glLineWidth(24);
                GL11.glBegin(GL11.GL_LINES);
                int offset = 530;
            /*
            if (Game.dim3) { //remove offset if tru
                lineOffset = 0;
                offset = 0;
            }
             */
                GL11.glVertex2i(rayAngles.indexOf(rayAngle)*8 + offset, (int) lineOffset); //shifting to right
                GL11.glVertex2i(rayAngles.indexOf(rayAngle)*8 +offset, (int) ((int) lineHeight + lineOffset));
                GL11.glEnd();
            }
        }

    public void drawRay(int rayIndex, float colorChange) {
        //System.out.println("Ray Angle: " + rayAngle);
        /*
        if (rayIndex == centerRayIndex) {
            GL11.glColor3f(colorChange, 0 , 0);
        } else {
            GL11.glColor3f(0, 1,0 );
        }
         */



        GL11.glColor3f(colorChange, 0 , 0);
        if (!Game.dim3) { //draw 2d rays
        GL11.glLineWidth(1);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2i((int) p.playerX, (int) p.playerY);
        GL11.glVertex2i((int) rayX, (int) rayY);
        GL11.glEnd();
        }


    }
    public float distance(float ax, float ay, float bx, float by, float ang) {
        return (float) (Math.sqrt((bx - ax) * (bx -ax) + (by- ay) * (by - ay)));
    }
    public void fireRay() {

    }
}
//if the ray is past a certain threshold, reflect it, most likely turn it to degrees
/*
            if (centerRay == numRays) {
                System.out.println();
            }
*/

/*
SHOOTING MECHANIC
public void createRay() {
    int numRays = 120; // Adjust the number of rays as desired
    float angleStep = (float) Math.PI / (3 * numRays); // Calculate the angle step size
    int centerRayIndex = numRays / 2; // Calculate the index of the center ray

    for (int rayIndex = 0; rayIndex < numRays; rayIndex++) {
        float rayAngle = p.playerAngle - (float) Math.PI / 6 + rayIndex * angleStep;

        // Rest of your code...
        // Make sure to replace all references to the outer 'ray' variable with the 'rayAngle' variable.
        // ...
        // drawRay() method can remain the same.
        // ...

        if (rayIndex == centerRayIndex) {
            // This is the center ray
            // You can perform additional operations or calculations specifically for the center ray if needed
        }
    }
}
 */