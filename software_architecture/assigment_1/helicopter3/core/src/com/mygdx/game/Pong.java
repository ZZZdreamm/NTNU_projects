package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Arrays;



public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture[] textures;
	Sprite sprite;
	float x, y;
	float xSpeed, ySpeed;
	long lastTextureChangeTime;


	@Override
	public void create() {
		batch = new SpriteBatch();
		textures = new Texture[4];
		for (int i = 0; i < 4; i++) {
			textures[i] = new Texture("heli/heli" + (i + 1) + ".png");
		}

		sprite = new Sprite(textures[0]);

		x = 200;
		y = 200;
		xSpeed = MathUtils.random(50, 100);
		ySpeed = MathUtils.random(50, 100);

		sprite.flip(true, false);

		lastTextureChangeTime = TimeUtils.millis();
	}

	@Override
	public void render() {
		x += xSpeed * Gdx.graphics.getDeltaTime();
		y += ySpeed * Gdx.graphics.getDeltaTime();

		float rotation = 0;
		boolean hitSideWall = false;
		boolean hitVerticalWall = false;

		if (x < 0 || x + sprite.getWidth() > Gdx.graphics.getWidth()) {
			xSpeed = -xSpeed;
			hitSideWall = true;
		}

		if (y < 0 || y + sprite.getHeight() > Gdx.graphics.getHeight()) {
			ySpeed = -ySpeed;
			hitVerticalWall = true;
		}

		rotation += MathUtils.radiansToDegrees * MathUtils.atan2(ySpeed, xSpeed);

		sprite.setRotation(rotation);

		long currentTime = TimeUtils.millis();
		if (currentTime - lastTextureChangeTime > 100) {
			int currentTextureIndex =  Arrays.asList(textures).indexOf(sprite.getTexture());
			int nextTextureIndex = (currentTextureIndex + 1) % textures.length;
			sprite.setTexture(textures[nextTextureIndex]);
			lastTextureChangeTime = currentTime;
		}

		ScreenUtils.clear(1, 0, 0, 1);

		batch.begin();
		sprite.setPosition(x, y);
		if (hitSideWall) {
			sprite.flip(false, true);
		} else if (hitVerticalWall) {
			boolean movingClockwise = sprite.getRotation() >= 0;
			if (movingClockwise) {
				sprite.setRotation(rotation + 90);
			} else {
				sprite.setRotation(rotation - 90);
			}
		}
		sprite.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		for (Texture texture : textures) {
			texture.dispose();
		}
	}
}