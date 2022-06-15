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
import com.chamodex.mariobros.MarioBros;
import com.chamodex.mariobros.Screens.PlayScreen;
import com.chamodex.mariobros.Sprites.Brick;
import com.chamodex.mariobros.Sprites.Coin;

public class B2WorldCreator {

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

    }

    // create objects
    public void createObjects(int indexOfObject, String type) {
        for (MapObject object : map.getLayers().get(indexOfObject).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            // Create coin bodies/fixtures
            if(type.equals("coin")) {
                new Coin(screen, rect);
            }
            // Create brick bodies/fixtures
            if(type.equals("brick")) {
                new Brick(screen, rect);
            }
            // Create pipe bodies/fixtures
            if(type.equals("pipe")) {
                BodyDef bdef = new BodyDef();
                FixtureDef fdef = new FixtureDef();
                PolygonShape shape = new PolygonShape();

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
                BodyDef bdef = new BodyDef();
                FixtureDef fdef = new FixtureDef();
                PolygonShape shape = new PolygonShape();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2 / MarioBros.PPM, rect.getHeight() / 2 / MarioBros.PPM);
                fdef.shape = shape;
                body.createFixture(fdef);
            }

        }
    }

}
