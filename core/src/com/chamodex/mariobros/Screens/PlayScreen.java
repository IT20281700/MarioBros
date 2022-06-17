package com.chamodex.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chamodex.mariobros.MarioBros;
import com.chamodex.mariobros.Scenes.Hud;
import com.chamodex.mariobros.Sprites.Enemies.Enemy;
import com.chamodex.mariobros.Sprites.Items.Item;
import com.chamodex.mariobros.Sprites.Items.ItemDef;
import com.chamodex.mariobros.Sprites.Items.Mushroom;
import com.chamodex.mariobros.Sprites.Mario;
import com.chamodex.mariobros.Tools.B2WorldCreator;
import com.chamodex.mariobros.Tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen {
    // Reference to Game, used to set Screens
    private final MarioBros game;
    private TextureAtlas atlas;
    public static boolean alreadyDestroyed = false;

    // Basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    // Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    // Sprites
    private Mario player;

    public static Music music;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    public PlayScreen(MarioBros game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        // Create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        // Create a FitViewport to maintain virtual aspect ratio despite
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gamecam);

        // Create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        // Load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);

        // Initially set our gamecam to be centered correctly at the start of map
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        // Create Box2D world, setting no gravity in x, -10 gravity in y, and allow bodies to sleep
        world = new World(new Vector2(0, -10), true);
        // allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        // Create world objects (bricks, coins, etc...)
        creator = new B2WorldCreator(this);

        // create mario in to the game
        player = new Mario(this);


        // player contact world objects
        world.setContactListener(new WorldContactListener());

        // Main Music Play
        music = MarioBros.manager.get(MarioBros.mainMusicPath, Music.class);
        music.setLooping(true);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

    }

    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems() {
        if(!itemsToSpawn.isEmpty()) {
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Mushroom.class) {
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    private void handleInput(float dt) {

        // If user is holding down mouse move our camera through the game world
        if (player.currentState != Mario.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W))
                player.jump();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) && player.b2Body.getLinearVelocity().x <= 2)
                player.b2Body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2Body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) && player.b2Body.getLinearVelocity().x >= -2)
                player.b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2Body.getWorldCenter(), true);
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                player.fire();
        }
    }

    public void update(float dt) {
        handleInput(dt);
        handleSpawningItems();

        // takes 1 step in the physics simulation (60 times per sec)
        world.step(1/60f, 6,2);

        // Player update
        player.update(dt);
        for (Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / MarioBros.PPM) {
                enemy.b2Body.setActive(true);
            }
        }

        for (Item item : items)
            item.update(dt);

        hud.update(dt);

        // attach game cam to player.x coordinate
        if (player.currentState != Mario.State.DEAD)
            gamecam.position.x = player.b2Body.getPosition().x;

        // Update game cam to correct coordinate
        gamecam.update();

        // render the view
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        // Clear the game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render our game map
        renderer.render();

        // Render our Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        // player render
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        // Draw sprites
        player.draw(game.batch);
        for (Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);

        for (Item item : items)
            item.draw(game.batch);

        game.batch.end();

        // Set our batch to new draw what the Hud camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    public boolean gameOver() {
        if (player.currentState == Mario.State.DEAD && player.getStateTimer() > 3) {
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public Hud getHud() {
        return hud;
    }
}
