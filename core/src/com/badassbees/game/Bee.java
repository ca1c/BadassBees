package com.badassbees.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Bee {
    public Vector2 position;
    public Sprite sprite;
    public float topSpeed = 500f;
    public float xSpeed = 0f;
    public float ySpeed = 500f;
    public float rotationSpeed = 500f;
    public float rotationAngle = 360f;
    public Bee(Texture img) {
        sprite = new Sprite(img);
        position = new Vector2(Gdx.graphics.getWidth()/2f,sprite.getScaleY()*sprite.getHeight()/2);
        sprite.setScale(2);
    }

    public void moveForward(float deltaTime) {
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.x += deltaTime * xSpeed;
            position.y += deltaTime * ySpeed;
            // Check if bee is hitting side walls
            if(position.x + sprite.getWidth() > Gdx.graphics.getWidth()) {
                position.x -= sprite.getWidth();
            }
            if(position.x < 0) {
                position.x += sprite.getWidth();
            }
            // Check if bee is hitting top and bottom walls
            if(position.y + sprite.getHeight() > Gdx.graphics.getHeight()) {
                position.y -= sprite.getHeight();
            }
            if(position.y < 0) {
                position.y += sprite.getHeight();
            }
        }
    }


    public void updateRotationAngle(float deltaTime) {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            rotationAngle += deltaTime*rotationSpeed;
            if(rotationAngle > 360) {
                rotationAngle -= 360;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rotationAngle += 360 - deltaTime*rotationSpeed;
            if(rotationAngle > 360) {
                rotationAngle -= 360;
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
