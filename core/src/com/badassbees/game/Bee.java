package com.badassbees.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Array;

public class Bee {
    public Vector2 position;
    public Sprite sprite;
    public float topSpeed = 200f;
    public float xSpeed = 0f;
    public float ySpeed = 500f;
    public float rotationSpeed = 300f;
    public float rotationAngle = 360f;
    public Vector2 flowerPosition;
    public boolean collided = false;
    float myAngle = 0;

    public Bee(Texture img, Vector2 flowerVector) {
        sprite = new Sprite(img);
        sprite.setScale(2);
        position = new Vector2(Gdx.graphics.getWidth()/2f - sprite.getWidth(),
                Gdx.graphics.getHeight()/2f - sprite.getHeight());
        flowerPosition = flowerVector;
    }

    public Rectangle getBoundingBox() {
        return sprite.getBoundingRectangle();
    }

    public void updateCollided(boolean collidedBool) {
        collided = collidedBool;
    }

    public boolean beeHittingRightWall() {
        return position.x + sprite.getWidth() > Gdx.graphics.getWidth();
    }

    public boolean beeHittingLeftWall() {
        return position.x < 0;
    }

    public boolean beeHittingTopWall() {
        return position.y + sprite.getHeight() > Gdx.graphics.getHeight();
    }

    public boolean beeHittingBottomWall() {
        return position.y < 0;
    }

    public void updateBeeCoordinates(float deltaTime) {
        position.x += deltaTime * xSpeed;
        position.y += deltaTime * ySpeed;
    }

    public void checkBeeHittingBorders() {
        if(beeHittingRightWall()) {
            position.x -= sprite.getWidth();
        }
        if(beeHittingLeftWall()) {
            position.x += sprite.getWidth();
        }
        // Check if bee is hitting top and bottom walls
        if(beeHittingTopWall()) {
            position.y -= sprite.getHeight();
        }
        if(beeHittingBottomWall()) {
            position.y += sprite.getHeight();
        }
    }

    public void moveForward(float deltaTime) {
        if(!collided) {
            updateBeeCoordinates(deltaTime);
            checkBeeHittingBorders();
        }
    }

    public void rotateLeft(float deltaTime) {
        rotationAngle += deltaTime*rotationSpeed;
        if(rotationAngle > 360) {
            rotationAngle -= 360;
        }
    }

    public void rotateRight(float deltaTime) {
        rotationAngle += 360 - deltaTime*rotationSpeed;
        if(rotationAngle > 360) {
            rotationAngle -= 360;
        }
    }

    public Vector2 findClosestFlower(Vector2[] flowerPositions) {
        int minIndex = -1;
        double minDistance = Double.POSITIVE_INFINITY;
        for(int i = 0; i < flowerPositions.length; i++) {
            Vector2 iPosition = flowerPositions[i];

            double distance = Math.sqrt(Math.pow(iPosition.x - position.x, 2) + Math.pow(iPosition.y - position.y, 2));

            if (distance < minDistance){
                minIndex = i;
                minDistance = distance;
            }
        }

        return flowerPositions[minIndex];
    }

    // gets the angle bee needs to be rotated to, to move to a certain coordinate
    public float getPathfindingRotationAngle(float x1, float y1, float x2, float y2) {
        return MathUtils.atan2Deg((y2 - y1), (x2 - x1));
    }

    public float targetAngle() {
        return getPathfindingRotationAngle(flowerPosition.x, flowerPosition.y, position.x, position.y);
    }

    public void updateRotationAngle(float deltaTime) {
        myAngle = targetAngle() + 90;

        if(Math.abs(myAngle - rotationAngle) > 3 && !collided) {
            if (((myAngle - rotationAngle) % 360 + 360 + 179) % 360 - 179 < 0) {
                rotateRight(deltaTime);
            } else {
                rotateLeft(deltaTime);
            }
        }
    }

    // This is called update velocity
    // because velocity implies a speed and direction
    // since it is a vector (this function does both of these)
    public void updateVelocity() {
        // Adjust X and Y speeds based on angle

        /*
            looks like this:
                   0
                  360
                   |
             90____|____270
                   |
                   |
                  180
         */

        float xSpeedMultiplier;
        float ySpeedMultiplier;
        if(rotationAngle < 360 && rotationAngle > 180) {
            xSpeedMultiplier = Math.abs(MathUtils.sinDeg(rotationAngle));
        }
        else{
            xSpeedMultiplier = -1 * (MathUtils.sinDeg(rotationAngle));
        }
        ySpeedMultiplier = MathUtils.cosDeg(rotationAngle);


        xSpeed = topSpeed * xSpeedMultiplier;
        ySpeed = topSpeed * ySpeedMultiplier;
    }

    public void Update(float deltaTime) {
        moveForward(deltaTime);
        updateRotationAngle(deltaTime);
        updateVelocity();
    }

    public void Draw(SpriteBatch batch) {
        Update(Gdx.graphics.getDeltaTime());
        sprite.setPosition(position.x, position.y);
        sprite.setRotation(rotationAngle);
        sprite.draw(batch);
    }
}