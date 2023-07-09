package com.badassbees.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public void updateRotationAngle(float deltaTime) {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            rotationAngle += deltaTime*rotationSpeed;
            if(rotationAngle > 360) {
                rotationAngle -= 360;
            }
            System.out.println(rotationAngle);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rotationAngle += 360 - deltaTime*rotationSpeed;
            if(rotationAngle > 360) {
                rotationAngle -= 360;
            }
            System.out.println(rotationAngle);
        }
    }

    public void updateSpeedForRotationAngle() {
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

        // between 360 and 270 degrees
        if(rotationAngle < 360 && rotationAngle > 270) {
            float xNumerator = Math.abs(rotationAngle - 360);
            float yNumerator = rotationAngle - 270;

            float xSpeedMultiplier = xNumerator / 90;
            float ySpeedMultiplier = yNumerator / 90;

            xSpeed = topSpeed * xSpeedMultiplier;
            ySpeed = topSpeed * ySpeedMultiplier;
        }
        // between 270 and 180 degrees
        else if(rotationAngle < 270 && rotationAngle > 180) {
            float xNumerator = rotationAngle - 180;
            float yNumerator = Math.abs(rotationAngle - 270);

            float xSpeedMultiplier = xNumerator / 90;
            float ySpeedMultiplier = -1 * (yNumerator / 90);

            xSpeed = topSpeed * xSpeedMultiplier;
            ySpeed = topSpeed * ySpeedMultiplier;
        }
        // between 180 and 90 degrees
        else if(rotationAngle < 180 && rotationAngle > 90 ) {
            float xNumerator = rotationAngle - 180;
            float yNumerator = rotationAngle - 90;

            float xSpeedMultiplier = xNumerator / 90;
            float ySpeedMultiplier = -1 * (yNumerator / 90);

            xSpeed = topSpeed * xSpeedMultiplier;
            ySpeed = topSpeed * ySpeedMultiplier;
        }
        // between 90 and 0 degrees (360)
        // >= because it starts at 0
        else if(rotationAngle < 90 && rotationAngle >= 0) {
            float xNumerator = rotationAngle;
            float yNumerator = Math.abs(rotationAngle - 90);

            float xSpeedMultiplier = -1 * (xNumerator / 90);
            float ySpeedMultiplier = yNumerator / 90;

            xSpeed = topSpeed * xSpeedMultiplier;
            ySpeed = topSpeed * ySpeedMultiplier;
        }
    }

    public void Update(float deltaTime) {
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.x += deltaTime*xSpeed;
            position.y += deltaTime*ySpeed;
        }

        // rotate the bee
        updateRotationAngle(deltaTime);
        updateSpeedForRotationAngle();

    }

    public void Draw(SpriteBatch batch) {
        Update(Gdx.graphics.getDeltaTime());
        sprite.setPosition(position.x, position.y);
        sprite.setRotation(rotationAngle);
        sprite.draw(batch);
    }
}
