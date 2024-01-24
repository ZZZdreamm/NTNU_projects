package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Sprite sprite;
	float x, y;
	float xSpeed, ySpeed;


	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("heli/heli1.png");

		sprite = new Sprite(img);

		x = 200;
		y = 200;
		xSpeed = MathUtils.random(50, 100);
		ySpeed = MathUtils.random(50, 100);

		sprite.flip(true, false);
	}

	@Override
	public void render() {
		x += xSpeed * Gdx.graphics.getDeltaTime();
		y += ySpeed * Gdx.graphics.getDeltaTime();

		float rotation = 0;
		boolean hitSideWall = false;
		boolean hitVerticalWall = false;

		if (x < 0 || x + img.getWidth() > Gdx.graphics.getWidth()) {
			xSpeed = -xSpeed;
			hitSideWall = true;

		}

		if (y < 0 || y + img.getHeight() > Gdx.graphics.getHeight()) {
			ySpeed = -ySpeed;
			hitVerticalWall = true;
		}

		rotation += MathUtils.radiansToDegrees * MathUtils.atan2(ySpeed, xSpeed);

		sprite.setRotation(rotation);



		ScreenUtils.clear(1, 0, 0, 1);

		batch.begin();
		sprite.setPosition(x, y);
		if (hitSideWall){
			sprite.flip(false, true);
		}
		else if(hitVerticalWall){
			boolean movingClockwise = sprite.getRotation() >= 0;
			if(movingClockwise){
				sprite.setRotation(rotation + 90);
			}else{
				sprite.setRotation(rotation - 90);
			}
		}
		sprite.draw(batch);
		batch.end();

	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
