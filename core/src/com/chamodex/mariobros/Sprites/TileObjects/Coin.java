package com.chamodex.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.chamodex.mariobros.MarioBros;
import com.chamodex.mariobros.Scenes.Hud;
import com.chamodex.mariobros.Screens.PlayScreen;
import com.chamodex.mariobros.Sprites.Items.ItemDef;
import com.chamodex.mariobros.Sprites.Items.Mushroom;
import com.chamodex.mariobros.Sprites.Mario;
import com.chamodex.mariobros.Sprites.TileObjects.InteractiveTileObject;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if (getCell().getTile().getId() == BLANK_COIN)
            MarioBros.manager.get(MarioBros.bumpSoundPath, Sound.class).play();
        else {
            if (object.getProperties().containsKey("mushroom")) {
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioBros.PPM),
                        Mushroom.class));
                MarioBros.manager.get(MarioBros.powerupSpawnPath, Sound.class).play();
            }
            else
                MarioBros.manager.get(MarioBros.coinSoundPath, Sound.class).play();
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(100);
    }
}
