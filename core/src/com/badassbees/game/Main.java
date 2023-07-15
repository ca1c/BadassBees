package com.badassbees.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	Texture beeImg;
	Texture flowerImg;
	Bee bee;
	Flower flower;
	ShapeRenderer shapeRenderer;
	@Override
	public void create () {
		batch = new SpriteBatch();
		beeImg = new Texture("bee.png");
		flowerImg = new Texture("flower.png");
		flower = new Flower(flowerImg);
		shapeRenderer = new ShapeRenderer();
		bee = new Bee(beeImg, flower.getPosition());

	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		bee.Draw(batch);
		flower.Draw(batch);

		if(flower.getBoundingBox().overlaps(bee.getBoundingBox())) {
			bee.updateCollided(true);
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		beeImg.dispose();
		flowerImg.dispose();
	}
}
