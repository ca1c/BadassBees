package com.badassbees.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Flower {
    public Vector2 position;
    public Sprite sprite;

    public Flower(Texture img) {
        sprite = new Sprite(img);
        sprite.setScale(2);
        position = new Vector2(100f,
                200f);
    }

    public Rectangle getBoundingBox() {
        return sprite.getBoundingRectangle();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void Update(float deltaTime) {

    }
    int runs = 0;
    public void Draw(SpriteBatch batch) {
        Update(Gdx.graphics.getDeltaTime());
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }
}
