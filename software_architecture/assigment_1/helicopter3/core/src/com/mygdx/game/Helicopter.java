package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class Helicopter {
    Sprite sprite;
    Texture[] textures;
    float x, y;
    float xSpeed, ySpeed;

    long lastTexturesChangeTimes;
    Rectangle bounds;

    Helicopter(){
        textures = new Texture[4];
        for (int i = 0; i < 4; i++) {
            textures[i] = new Texture("heli/heli" + (i + 1) + ".png");
        }

        sprite = new Sprite(textures[MathUtils.random(0, textures.length - 1)]);
        x = MathUtils.random(0, Gdx.graphics.getWidth() - sprite.getWidth());
        y = MathUtils.random(0, Gdx.graphics.getHeight() - sprite.getHeight());
        xSpeed = MathUtils.random(50, 100) * (MathUtils.randomBoolean() ? 1 : -1);
        ySpeed = MathUtils.random(50, 100) * (MathUtils.randomBoolean() ? 1 : -1);
        lastTexturesChangeTimes = TimeUtils.millis();

        sprite.flip(true, xSpeed > 0 ? false : true);

        float previousRotation = sprite.getRotation();

        bounds = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
    }

    public void updateBounds() {
        bounds.setPosition(x, y);
    }

    public void dispose(){
        for (int i = 0; i < textures.length; i++){
            textures[i].dispose();
        }
    }

}
