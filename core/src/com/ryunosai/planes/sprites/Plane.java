package com.ryunosai.planes.sprites;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ryunosai.planes.Planes;

/**
 * Created by getrasa on 30/10/16.
 */

public class Plane {
    public static final float WIDTH = 60;
    public static final float HEIGHT = 65;
    private static final float TURN_SPEED = 500;

    private Planes game;
    private World world;
    private float position;

    private com.badlogic.gdx.math.Circle rectangle;
    private Body b2body;

    private boolean left = true;

    public Plane(Planes game, World world, float position) {
        this.game = game;
        this.world = world;
        this.position = position;

        definePlane();

        float xRectangle = b2body.getPosition().x - (WIDTH / game.PTM_RATIO);
        float yRectangle = b2body.getPosition().y - (HEIGHT / game.PTM_RATIO);


        rectangle = new com.badlogic.gdx.math.Circle(xRectangle, yRectangle, (WIDTH / game.PTM_RATIO) / 2);


    }

    public void changeLine() {
        // Start moving to the right
        if(left) {
            b2body.setLinearVelocity(new Vector2(TURN_SPEED, 0));
            left = false;
        }

        // Start moving to the left
        else if(left == false){
            b2body.setLinearVelocity(new Vector2(-TURN_SPEED, 0));
            left = true;
        }
    }


    public void update() {
        // If plane reached right lane - stop
        if(b2body.getPosition().x >= (game.WIDTH / game.PTM_RATIO / 6) + position && left == false){
            b2body.setLinearVelocity(new Vector2(0, 0));
        }

        // If plane reached left lane - stop
        if(b2body.getPosition().x <= position && left) {
            b2body.setLinearVelocity(new Vector2(0, 0));
        }

        float xRectangle = b2body.getPosition().x;
        float yRectangle = b2body.getPosition().y;

        // Set b2bodies position to rectangle


        rectangle.setPosition(xRectangle, yRectangle);
    }

    public Circle returnRectangle() {
        return rectangle;
    }

    public void definePlane() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(position, 200 / game.PTM_RATIO);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius((WIDTH / game.PTM_RATIO) / 2);

//        WIDTH / game.PTM_RATIO / 2f, HEIGHT / game.PTM_RATIO / 2f

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

}
