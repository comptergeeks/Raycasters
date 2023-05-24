package com.company;

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
    public Ray(Player p) {
        this.p = p;
    }
    public void drawRay() {
        rayAngle = p.playerAngle;
        for(int ray = 0; ray < 1; ray++) {
            depthOfField = 0;
            float inverseTan = (float) (-1/Math.tan(rayAngle));
            if (rayAngle > Math.PI) { //ray looking down
                rayY = (float) ((((int)p.playerY>>6)<<6)-0.0001); //bitshifting for accuracy
                rayX = (p.playerX - rayY)*inverseTan + p.playerX;
                yOffset=-64;
                xOffset=-yOffset*inverseTan;
            } //ray looking up
            if (rayAngle < Math.PI) {
                rayY = (float) ((((int)p.playerY>>6)<<6)+64); //bitshifting for accuracy
                rayX = (p.playerX - rayY)*inverseTan + p.playerX;
                yOffset=64;
                xOffset=-yOffset*inverseTan;
            }

            //ray horizontal
            if (rayAngle == (float) 0 || rayAngle == (float) Math.PI) {
                rayX = p.playerX;
                rayY = p.playerY;
                depthOfField = 8;
            }
            while (depthOfField < 8) {
                mapX = (int)
            }
        }
    }
}
