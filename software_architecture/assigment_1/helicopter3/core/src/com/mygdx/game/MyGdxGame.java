package com.mygdx.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Arrays;



public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Helicopter[] helicopters;


	private Helicopter createNonOverlappingHelicopter() {
		Helicopter newHelicopter;

		do {
			newHelicopter = new Helicopter();
		} while (checkOverlap(newHelicopter));

		return newHelicopter;
	}

	private boolean checkOverlap(Helicopter newHelicopter) {
		for (Helicopter existingHelicopter : helicopters) {
			if (existingHelicopter != null && existingHelicopter.bounds.overlaps(newHelicopter.bounds)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();

		int numSprites = 3;

		helicopters = new Helicopter[numSprites];


		for (int i = 0; i < numSprites; i++) {
			helicopters[i] = createNonOverlappingHelicopter();
		}

	}


	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0, 1);

		batch.begin();

		for (int i = 0; i < helicopters.length; i++) {
			Helicopter helicopter = helicopters[i];
			boolean sameHorizontalDifferentVerticalMovement = false;
			boolean bouncedFromAnother = false;

			helicopter.x += helicopter.xSpeed * Gdx.graphics.getDeltaTime();
			helicopter.y += helicopter.ySpeed * Gdx.graphics.getDeltaTime();

			helicopter.updateBounds();
			for (int j = 0; j < helicopters.length; j++) {
				if (i == j){
					continue;
				}
				Helicopter otherHelicopter = helicopters[j];

				if (helicopter.bounds.overlaps(otherHelicopter.bounds)) {
					bouncedFromAnother = true;
					sameHorizontalDifferentVerticalMovement = ((helicopter.ySpeed * otherHelicopter.ySpeed) > 0 && (helicopter.xSpeed * otherHelicopter.xSpeed) > 0) || ((helicopter.ySpeed * otherHelicopter.ySpeed) < 0 && (helicopter.xSpeed * otherHelicopter.xSpeed) > 0);

					float tempXSpeed = helicopter.xSpeed;
					float tempYSpeed = helicopter.ySpeed;

					helicopter.xSpeed = otherHelicopter.xSpeed;
					helicopter.ySpeed = otherHelicopter.ySpeed;

					otherHelicopter.xSpeed = tempXSpeed;
					otherHelicopter.ySpeed = tempYSpeed;


					while (helicopter.bounds.overlaps(otherHelicopter.bounds)) {
						helicopter.x += helicopter.xSpeed * Gdx.graphics.getDeltaTime();
						helicopter.y += helicopter.ySpeed * Gdx.graphics.getDeltaTime();
						helicopter.updateBounds();

						otherHelicopter.x += otherHelicopter.xSpeed * Gdx.graphics.getDeltaTime();
						otherHelicopter.y += otherHelicopter.ySpeed * Gdx.graphics.getDeltaTime();
						otherHelicopter.updateBounds();
					}

//					helicopter.sprite.flip(false, true);
//					otherHelicopter.sprite.flip(false, true);


					if(sameHorizontalDifferentVerticalMovement == false){
						helicopter.sprite.flip(false, true);
						otherHelicopter.sprite.flip(false, true);
					}

				}
			}

			float rotation = 0;
			boolean hitSideWall = false;
			boolean hitVerticalWall = false;

			if (helicopter.x < 0 || helicopter.x + helicopter.sprite.getWidth() > Gdx.graphics.getWidth()) {
				helicopter.xSpeed = -helicopter.xSpeed;
				hitSideWall = true;
			}

			if (helicopter.y < 0 || helicopter.y + helicopter.sprite.getHeight() > Gdx.graphics.getHeight()) {
				helicopter.ySpeed = -helicopter.ySpeed;
				hitVerticalWall = true;
			}

			rotation += MathUtils.radiansToDegrees * MathUtils.atan2(helicopter.ySpeed, helicopter.xSpeed);

			helicopter.sprite.setRotation(rotation);


			helicopter.sprite.setPosition(helicopter.x, helicopter.y);
			if (hitSideWall) {
				helicopter.sprite.flip(false, true);
			} else if (hitVerticalWall) {
				boolean movingClockwise = helicopter.sprite.getRotation() >= 0;
				if (movingClockwise) {
					helicopter.sprite.setRotation(rotation + 90);
				} else {
					helicopter.sprite.setRotation(rotation - 90);
				}
			}

			long currentTime = TimeUtils.millis();
			if (currentTime - helicopter.lastTexturesChangeTimes > 100) {
				int currentTextureIndex = Arrays.asList(helicopter.textures).indexOf(helicopter.sprite.getTexture());
				int nextTextureIndex = (currentTextureIndex + 1) % helicopter.textures.length;
				helicopter.sprite.setTexture(helicopter.textures[nextTextureIndex]);
				helicopter.lastTexturesChangeTimes = currentTime;
			}


			helicopter.sprite.draw(batch);
		}

		batch.end();

	}

	@Override
	public void dispose() {
		batch.dispose();
		for (Helicopter helicopter:helicopters) {
			helicopter.dispose();
		}
	}
}