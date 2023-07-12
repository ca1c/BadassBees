package com.badassbees.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bee {
    public Vector2 position;
    public Sprite sprite;
    public float topSpeed = 300f;
    public float xSpeed = 0f;
    public float ySpeed = 500f;
    public float rotationSpeed = 200f;
    public float rotationAngle = 360f;
    public float moveAngle;
    public Vector2 flowerPosition;
    public boolean collided = false;
    public Bee(Texture img, Vector2 flowerVector) {
        sprite = new Sprite(img);
        sprite.setScale(2);
        position = new Vector2(Gdx.graphics.getWidth()/2 - sprite.getWidth(),
                Gdx.graphics.getHeight()/2 - sprite.getHeight());
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

    // gets quadrant of coordinate
    public float getCoordinateQuadrantMinuend(float x, float y) {
        float yAxis = Gdx.graphics.getWidth() / 2;
        float xAxis = Gdx.graphics.getHeight() / 2;

        if(x > yAxis && y > xAxis) {
            return 90f;
        }
        else if(x > yAxis && y < xAxis) {
            return 180f;
        }
        else if(x < yAxis && y < xAxis) {
            return 270f;
        }
        else {
            return 360f;
        }
    }

    // gets the angle bee needs to be rotated to, to move to a certain coordinate
    public float getPathfindingRotationAngle(float x1, float y1, float x2, float y2) {
        float c = (y2 - y1) / (x2 - x1);
        float minuend = getCoordinateQuadrantMinuend(x1, y1);
        float theta = minuend - MathUtils.atanDeg(c);

        return theta;
    }

    public float targetAngle() {
        return 360 - getPathfindingRotationAngle(flowerPosition.x, flowerPosition.y, position.x, position.y);
    }

    public void moveForward(float deltaTime) {
        if(!collided) {
            moveAngle = targetAngle();
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

    public void updateRotationAngle(float deltaTime) {
        float myAngle = targetAngle();
        if(rotationAngle > myAngle) {
            rotateRight(deltaTime);
        }
        if(rotationAngle < myAngle) {
            rotateLeft(deltaTime);
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
    int runs = 0;
    public void Draw(SpriteBatch batch) {
        Update(Gdx.graphics.getDeltaTime());
        sprite.setPosition(position.x, position.y);
        sprite.setRotation(rotationAngle);
        sprite.draw(batch);
    }
}
