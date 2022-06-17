package com.chamodex.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.chamodex.mariobros.MarioBros;
import com.chamodex.mariobros.Scenes.Hud;
import com.chamodex.mariobros.Screens.PlayScreen;
import com.chamodex.mariobros.Sprites.Mario;

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if(mario.isBig()) {
            setCategoryFilter(MarioBros.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            MarioBros.manager.get(MarioBros.breakBlockSoundPath, Sound.class).play();
        }
        else
            MarioBros.manager.get(MarioBros.bumpSoundPath, Sound.class).play();
    }
}
