package com.ryunosai.planes.sprites;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ryunosai.planes.Planes;



/**
 * Created by getrasa on 3/11/16.
 */

public class Circle {
    private static final float WIDTH = 52;
    private static float HEIGHT = 52;
    private double SPEED;

    private Planes game;
    private float position;
    private int distance;
    private float initialSpeed = 0.8f;

    private Rectangle rectangle;

    private Vector2 rectanglePosition;

    private int number = 1;

    private float animationTime;

    public Circle(Planes game, float position, int distance) {
        this.game = game;
        this.position = position;
        this.distance = distance;
        position = position - (WIDTH / 2 / game.PTM_RATIO);

        SPEED = initialSpeed;



        rectanglePosition = new Vector2(position, (game.HEIGHT / game.PTM_RATIO) + distance);

        rectangle = new Rectangle(rectanglePosition.x, rectanglePosition.y, WIDTH / game.PTM_RATIO, HEIGHT / game.PTM_RATIO);
    }

    public void update(float speed) {
        SPEED = initialSpeed + speed / 2f;

        rectanglePosition.y -= SPEED;
        rectangle.setPosition(rectanglePosition);

    }

    public boolean newShape() {

        if(rectanglePosition.y <= game.HEIGHT / game.PTM_RATIO / 1.8) {
            number++;
        }

        return number == 2;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean overLapDetector(com.badlogic.gdx.math.Circle plane) {
        return Intersector.overlaps(plane, rectangle);
    }

    public boolean animation(float dt) {
        animationTime += dt;
        if(animationTime < 0.5) {
            rectangle.setSize(0,0);
        }
        else if(animationTime >= 0.5 && animationTime < 1) {
            rectangle.setSize(WIDTH / game.PTM_RATIO, HEIGHT / game.PTM_RATIO);
        }

        else if(animationTime >= 1 && animationTime < 1.5) {
            rectangle.setSize(0,0);
        }
        else if(animationTime >= 1.5 && animationTime < 2) {
            rectangle.setSize(WIDTH / game.PTM_RATIO, HEIGHT / game.PTM_RATIO);
        }

        else if(animationTime >= 2 && animationTime < 2.5) {
            rectangle.setSize(0,0);
        }
        else if(animationTime >= 2.5 && animationTime < 3) {
            rectangle.setSize(WIDTH / game.PTM_RATIO, HEIGHT / game.PTM_RATIO);
        }

        return animationTime > 3;
    }
}
