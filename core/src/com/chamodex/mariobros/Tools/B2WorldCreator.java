package com.chamodex.mariobros.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.chamodex.mariobros.MarioBros;
import com.chamodex.mariobros.Screens.PlayScreen;
import com.chamodex.mariobros.Sprites.Enemies.Enemy;
import com.chamodex.mariobros.Sprites.Enemies.Turtle;
import com.chamodex.mariobros.Sprites.TileObjects.Brick;
import com.chamodex.mariobros.Sprites.TileObjects.Coin;
import com.chamodex.mariobros.Sprites.Enemies.Goomba;

public class B2WorldCreator {
    private Array<Goomba> goombas;
    private static Array<Turtle> turtles;

    private final World world;
    private final TiledMap map;
    private BodyDef bdef;
    private PolygonShape shape;
    private FixtureDef fdef;
    private Body body;

    private PlayScreen screen;

    public B2WorldCreator(PlayScreen screen) {
        this.screen = screen;

        world = screen.getWorld();
        map = screen.getMap();

        // Create body and fixture variables
        bdef = new BodyDef();
        shape = new PolygonShape();
        fdef = new FixtureDef();

        // Create ground bodies/fixtures
        createObjects(2, "ground");

        // Create pipe bodies/fixtures
        createObjects(3, "pipe");

        // Create brick bodies/fixtures
        createObjects(5, "brick");

        // Create coin bodies/fixtures
        createObjects(4, "coin");

        // Create goombas
        goombas = new Array<Goomba>();
        createObjects(6, "goomba");

        // Create turtles
        turtles = new Array<Turtle>();
        createObjects(7, "turtle");

    }

    // create objects
    public void createObjects(int indexOfObject, String type) {

        for (MapObject object : map.getLayers().get(indexOfObject).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            // Create pipe bodies/fixtures
            if(type.equals("pipe")) {
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2 / MarioBros.PPM, rect.getHeight() / 2 / MarioBros.PPM);
                fdef.shape = shape;
                fdef.filter.categoryBits = MarioBros.OBJECT_BIT;
                body.createFixture(fdef);
            }
            // Create ground bodies/fixtures
            if(type.equals("ground")) {
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2 / MarioBros.PPM, rect.getHeight() / 2 / MarioBros.PPM);
                fdef.shape = shape;
                body.createFixture(fdef);
            }
            // Create coin bodies/fixtures
            if(type.equals("coin")) {
                new Coin(screen, object);
            }
            // Create brick bodies/fixtures
            if(type.equals("brick")) {
                new Brick(screen, object);
            }
            // Create goombas
            if(type.equals("goomba")) {
                goombas.add(new Goomba(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
            }

            // Create turtles
            if(type.equals("turtle")) {
                turtles.add(new Turtle(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
            }

        }
    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }

    public void removeTurtle(Turtle turtle) {
        turtles.removeValue(turtle, true);
    }

    public Array<Enemy> getEnemies() {
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }
}
