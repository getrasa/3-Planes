package com.ryunosai.planes.sprites;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ryunosai.planes.Planes;

/**
 * Created by getrasa on 15/11/16.
 */

public class Engine {
    public static final float WIDTH = 10;
    public static final float HEIGHT = 10;
    private double SPEED;

    private Planes game;
    private float positionX;

    private Rectangle rectangle;

    private Vector2 rectanglePosition;

    private float initialSpeed = 0.8f;
    private float shrink = 0.1f;

    private float offSet;

    public Engine(Planes game, float positionX, float positionY) {
        this.game = game;
        this.positionX = positionX;
        positionX = positionX - (WIDTH / 2 / game.PTM_RATIO);

        offSet = 2;


        SPEED = initialSpeed;

        rectanglePosition = new Vector2(positionX, positionY);

        rectangle = new Rectangle(rectanglePosition.x, rectanglePosition.y, WIDTH / game.PTM_RATIO, HEIGHT / game.PTM_RATIO);
    }

    public void update() {
        rectanglePosition.x = positionX - rectangle.getWidth() /  2;

        rectanglePosition.y -= SPEED;
        rectangle.setSize(rectangle.getWidth() - shrink, rectangle.getHeight() - shrink);
        rectangle.setPosition(rectanglePosition);
    }


    public Rectangle getRectangle() {
        return rectangle;
    }

}
