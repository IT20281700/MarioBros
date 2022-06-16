package com.chamodex.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chamodex.mariobros.Screens.PlayScreen;

public class MarioBros extends Game {

	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;

	public static final short GROUND_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;


	public SpriteBatch batch;

	public static final String mainMusicPath = "audio/music/mario_music.ogg";
	public static final String coinSoundPath = "audio/sounds/coin.wav";
	public static final String bumpSoundPath = "audio/sounds/bump.wav";
	public static final String breakBlockSoundPath = "audio/sounds/breakblock.wav";

	public static AssetManager manager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load(mainMusicPath, Music.class);
		manager.load(coinSoundPath, Sound.class);
		manager.load(bumpSoundPath, Sound.class);
		manager.load(breakBlockSoundPath, Sound.class);
		manager.finishLoading();

		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
}
