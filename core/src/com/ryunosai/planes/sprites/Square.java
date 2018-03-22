package com.ryunosai.planes.sprites;

import com.badlogic.gdx.math.*;
import com.ryunosai.planes.Planes;

/**
 * Created by getrasa on 3/11/16.
 */

public class Square {
    public static final float WIDTH = 50;
    public static final float HEIGHT = 50;
    private double SPEED;

    private Planes game;
    private float position;
    private int distance;

    private Rectangle rectangle;

    private Vector2 rectanglePosition;

    private float initialSpeed = 0.8f;

    private int number = 1;

    private float addSize = 0.3f;
    private float addNow;

    private int times;

    public Square(Planes game, float position, int distance) {
        this.game = game;
        this.position = position;
        this.distance = distance;
        position = position - (WIDTH / 2 / game.PTM_RATIO);

        SPEED = initialSpeed;

        addNow = addSize;
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

    public boolean animation() {

        if(rectangle.getWidth() >= 70 / game.PTM_RATIO) {
            addNow = -addSize;
            times = times+1;
        }

        if(rectangle.getWidth() <= 55 / game.PTM_RATIO) {
            addNow = addSize;
            times = times+1;
        }

        rectangle.setSize(rectangle.getWidth()+addNow, rectangle.getHeight()+addNow);

        rectangle.x =  rectangle.x - addNow / 2;
        rectangle.y =  rectangle.y - addNow / 2;

        return times >= 6;
    }


}
