package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Sprite sprite;
	BitmapFont font;
	float x, y;
	boolean isTouched;
	float touchX, touchY;


	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("heli/heli1.png");

		sprite = new Sprite(img);

		x = 200;
		y = 200;


		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				if (x <= screenX && screenX <= x + img.getWidth() && y <= Gdx.graphics.getHeight() - screenY && Gdx.graphics.getHeight() - screenY <= y + img.getHeight()) {
					isTouched = true;
					touchX = screenX - x;
					touchY = Gdx.graphics.getHeight() - screenY - y;
					return true;
				}
				return false;
			}


			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				if (isTouched) {
					float newX = screenX - touchX;
					float newY = Gdx.graphics.getHeight() - screenY - touchY;

					if (newX < 0) {
						newX = 0;
					} else if (newX + img.getWidth() > Gdx.graphics.getWidth()) {
						newX = Gdx.graphics.getWidth() - img.getWidth();
					}

					if (newY < 0) {
						newY = 0;
					} else if (newY + img.getHeight() > Gdx.graphics.getHeight()) {
						newY = Gdx.graphics.getHeight() - img.getHeight();
					}

					float angle = MathUtils.atan2(newY - y, newX - x) * MathUtils.radiansToDegrees + 180;

					sprite.setRotation(angle);

					x = newX;
					y = newY;

					return true;
				}
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				isTouched = false;
				return true;
			}
		});

		font = new BitmapFont();
		font.setColor(Color.WHITE);
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0, 1);

		batch.begin();
		sprite.setPosition(x, y);
		sprite.draw(batch);

		String positionText = "Sprite Position: (" + x + ", " + y + ")";
		font.draw(batch, positionText, 10, Gdx.graphics.getHeight() - 10);

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
