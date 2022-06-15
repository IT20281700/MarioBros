package com.chamodex.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.chamodex.mariobros.Screens.PlayScreen;

public abstract class Enemy extends Sprite {

    protected final PlayScreen screen;
    protected final World world;
    public Body b2Body;

    public Enemy(PlayScreen screen, float x, float y) {
        this.screen = screen;
        world = screen.getWorld();

        setPosition(x, y);
        defineEnemy();
    }

    protected abstract void defineEnemy();

    public abstract void hitOnHead();
}
