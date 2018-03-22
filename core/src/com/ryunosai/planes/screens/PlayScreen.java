package com.ryunosai.planes.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ryunosai.planes.Planes;
import com.ryunosai.planes.sprites.Circle;
import com.ryunosai.planes.sprites.Engine;
import com.ryunosai.planes.sprites.Plane;
import com.ryunosai.planes.sprites.Square;

import java.util.Random;

/**
 * Created by getrasa on 30/10/16.
 */

public class PlayScreen implements Screen {

    private Planes game;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private World world;

    private Plane plane1, plane2, plane3;

    private Array<Circle> circles1, circles2, circles3;
    private Array<Square> squares1, squares2, squares3;
    private Array<Engine> engines1, engines2, engines3;

    private Texture background;
    private Texture planeTexture, pauseButton;
    private Texture blueCircle, redCircle, yellowCircle;
    private Texture blueSquare, redSquare, yellowSquare;

    private Texture  playAgainButton;

    private Texture smallPlayButton, smallHomeButton, verySmallHomeButton;

    //run sprites
    private Sprite pauseSprite;

    // pause sprites
    private Sprite smallPlay, smallHome, playAgain, verySmallHome;

    private float circleTimeOut;

    private float rowPosition1, rowPosition2, rowPosition3, rowPosition4, rowPosition5, rowPosition6;

    private int dis1, dis2, dis3;
    private int maxDis = 170;
    private int minDis = 1;
    private int score;
    private int record;

    private float speed = 1;

    private Random random;

    private OrthographicCamera hudCamera;
    private Viewport hudPortCam;

    private State state = State.RUN;

    private float restartTime;

    private GlyphLayout layout;
    private float textWidth;

    private Sound pickUp, notPicked;

    private static float soundVolume = 0.05f;
    private static float soundVolume2 = 0.2f;
    private Preferences prefs;

    private float stopOppasity;
    private float enginesTime;

    private boolean deadSound = false;


    public PlayScreen(Planes game) {
        this.game = game;

        // Create Camera na set initial position
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(game.WIDTH / game.PTM_RATIO, game.HEIGHT / game.PTM_RATIO, gamecam);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        hudCamera = new OrthographicCamera();
        hudPortCam = new FitViewport(game.WIDTH, game.HEIGHT, hudCamera);
        hudCamera.position.set(game.WIDTH / 2, game.HEIGHT / 2, 0);


        // Box2d methods
        world = new World(new Vector2(0, 0), true);

        random = new Random();

        // Textures
        background = new Texture("background1.png");

        planeTexture = new Texture("plane.png");

        blueCircle = new Texture("blue-circle.png");
        redCircle = new Texture("red-circle.png");
        yellowCircle = new Texture("yellow-circle.png");

        blueSquare = new Texture("blue-square.png");
        redSquare = new Texture("red-square.png");
        yellowSquare = new Texture("yellow-square.png");

        pauseButton = new Texture("ic_pause_white_24dp.png");

        smallPlayButton = new Texture("icons/small-play.png");
        smallHomeButton = new Texture("icons/small-home.png");
        verySmallHomeButton = new Texture("icons/very-small-home.png");

        playAgainButton = new Texture("icons/play-again.png");

        planeTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        blueCircle.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        redCircle.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        yellowCircle.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        blueSquare.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        redSquare.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        yellowSquare.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //run sprites
        pauseSprite = new Sprite(pauseButton);

        // pause sprites
        smallPlay = new Sprite(smallPlayButton);
        smallHome = new Sprite(smallHomeButton);

        playAgain = new Sprite(playAgainButton);

        verySmallHome = new Sprite(verySmallHomeButton);

        setButton(smallPlay, (game.WIDTH / 2) - smallPlayButton.getWidth() - 20,
                (game.HEIGHT / 2) - smallPlayButton.getHeight() / 2, smallPlay.getWidth(), smallPlay.getHeight());

        setButton(smallHome, (game.WIDTH / 2) + 20, (game.HEIGHT / 2) -
                smallHomeButton.getHeight() / 2, smallHomeButton.getWidth(), smallHomeButton.getHeight());

        setButton(pauseSprite, 5, game.HEIGHT - pauseButton.getWidth() - 5, pauseButton.getWidth(),
                pauseButton.getHeight());

        setButton(playAgain, game.WIDTH / 2 - playAgainButton.getWidth() / 2, game.HEIGHT / 2 - playAgainButton.getHeight() + 50,
                playAgainButton.getWidth(), playAgainButton.getHeight());

        setButton(verySmallHome, game.WIDTH / 2 - verySmallHomeButton.getWidth() / 2,
                game.HEIGHT / 3 - verySmallHomeButton.getHeight() / 2,
                verySmallHomeButton.getWidth(), verySmallHomeButton.getHeight());


        // Circle and Square arrays
        circles1 = new Array<Circle>();
        circles2 = new Array<Circle>();
        circles3 = new Array<Circle>();

        squares1= new Array<Square>();
        squares2= new Array<Square>();
        squares3= new Array<Square>();

        engines1 = new Array<Engine>();
        engines2 = new Array<Engine>();
        engines3 = new Array<Engine>();

        // Six row positions
        rowPosition1 = game.WIDTH / game.PTM_RATIO / 12;
        rowPosition2 = rowPosition1 * 3;
        rowPosition3 = rowPosition1 * 5;
        rowPosition4 = rowPosition1 * 7;
        rowPosition5 = rowPosition1 * 9;
        rowPosition6 = rowPosition1 * 11;

        // Create three planes
        plane1 = new Plane(game, world, rowPosition1);
        plane2 = new Plane(game, world, rowPosition3);
        plane3 = new Plane(game, world, rowPosition5);

        // Random distance between rows
        dis1 = random.nextInt(maxDis - minDis + 1) + minDis;
        dis2 = random.nextInt(maxDis - minDis + 1) + minDis;
        dis3 = random.nextInt(maxDis - minDis + 1) + minDis;


        columnDivider1(rowPosition1, rowPosition2, dis1);
        columnDivider2(rowPosition3, rowPosition4, dis2);
        columnDivider3(rowPosition5, rowPosition6, dis3);

        prefs = Gdx.app.getPreferences("MyScore");

        record = prefs.getInteger("score", 0);

        pickUp = Gdx.audio.newSound(Gdx.files.internal("music/pick-up.wav"));
        notPicked = Gdx.audio.newSound(Gdx.files.internal("music/not-picked.wav"));

        layout = new GlyphLayout();








    }


    @Override
    public void show() {}

    public enum State
    {
        PAUSE,
        RUN,
        CIRCLE,
        SQUARE,
        STOPPED
    }



    public void inputHandler(float dt) {

        // Keyboard controller
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            plane1.changeLine();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            plane2.changeLine();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            plane3.changeLine();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            state = State.PAUSE;
        }


        // Touch controller
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                Vector3 touchPos = new Vector3(x, y, 0);
                gamecam.unproject(touchPos);

                if(state == State.RUN) {
                    if(checkIfClicked(pauseSprite, touchPos.x * game.PTM_RATIO, touchPos.y * game.PTM_RATIO)) {
                        state = State.PAUSE;

                    }

                    else if(touchPos.x * 4 <= game.WIDTH / 3) {
                        plane1.changeLine();
                    }

                    else if(touchPos.x * 4 > game.WIDTH / 3 && touchPos.x * 4 <= (game.WIDTH / 3) * 2) {
                        plane2.changeLine();
                    }

                    else if(touchPos.x * 4 > (game.WIDTH / 3) * 2) {
                        plane3.changeLine();
                    }

                }

                if(state == State.PAUSE) {
                    if(checkIfClicked(smallPlay, touchPos.x * game.PTM_RATIO, touchPos.y * game.PTM_RATIO)) {
                        state = State.RUN;
                    }

                    if(checkIfClicked(smallHome, touchPos.x * game.PTM_RATIO, touchPos.y * game.PTM_RATIO)) {
                        game.setScreen(new StartScreen(game));
                    }
                }

                if(state == State.STOPPED) {
                    if(checkIfClicked(playAgain, touchPos.x * game.PTM_RATIO, touchPos.y * game.PTM_RATIO)) {
                        game.setScreen(new PlayScreen(game));
                    }

                    if(checkIfClicked(verySmallHome, touchPos.x * game.PTM_RATIO, touchPos.y * game.PTM_RATIO)) {
                        game.setScreen(new StartScreen(game));
                    }
                }


                return true; // return true to indicate the event was handled
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                // your touch up code here
                return true; // return true to indicate the event was handled
            }
        });

    }







    public void update(float dt) {
        inputHandler(dt);

        if(state == State.CIRCLE) {

        }
        if(state == State.RUN) {



            if (score <= 70) {
                speed = (score / 40f) + 1;
            }


            world.step(1 / 60f, 6, 2);

            // Update Planes
            plane1.update();
            plane2.update();
            plane3.update();


            // Update Circles and Squares
            for (Circle circle : circles1) {
                circle.update(speed);
                if (circle.newShape()) {
                    columnDivider1(rowPosition1, rowPosition2, 0);
                }
            }

            for (Square square : squares1) {
                square.update(speed);
                if (square.newShape()) {
                    columnDivider1(rowPosition1, rowPosition2, 0);
                }
            }

            for (Circle circle : circles2) {
                circle.update(speed);
                if (circle.newShape()) {
                    columnDivider2(rowPosition3, rowPosition4, 0);
                }
            }

            for (Square square : squares2) {
                square.update(speed);
                if (square.newShape()) {
                    columnDivider2(rowPosition3, rowPosition4, 0);
                }
            }

            for (Circle circle : circles3) {
                circle.update(speed);
                if (circle.newShape()) {
                    columnDivider3(rowPosition5, rowPosition6, 0);
                }
            }

            for (Square square : squares3) {
                square.update(speed);
                if (square.newShape()) {
                    columnDivider3(rowPosition5, rowPosition6, 0);
                }
            }

        }

        if(state == State.RUN || state == State.CIRCLE || state == State.SQUARE) {
            overlapDetector1(dt);
            overlapDetector2(dt);
            overlapDetector3(dt);
        }

        enginesUpdate(plane1.returnRectangle().x, plane2.returnRectangle().x, plane3.returnRectangle().x,
                plane1.returnRectangle().y - plane1.returnRectangle().radius, dt);


        pauseGame(dt);
        restartGame(dt);

    }



    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        // Draw Background Texture
        game.batch.draw(background, 0, 0, game.WIDTH / game.PTM_RATIO, game.HEIGHT / game.PTM_RATIO);

        game.batch.end();




        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        // Draw circles
        game.shapeRenderer.setColor(Color.BLUE);
        for(Circle circle : circles1) {
            Rectangle rectangle = circle.getRectangle();
//            game.shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());



            // Draw blue Circle Texture
            game.batch.draw(blueCircle, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());


        }


        // Draw squares
        for(Square square : squares1) {
            Rectangle rectangle = square.getRectangle();
            game.batch.draw(blueSquare, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        }

        for(Circle circle : circles2) {
            Rectangle rectangle = circle.getRectangle();
            game.batch.draw(yellowCircle, rectangle.getX(), rectangle.getY(),
                    rectangle.getWidth() - (2f/game.PTM_RATIO), rectangle.getHeight() - (2f/game.PTM_RATIO));
        }


        // Draw squares
        for(Square square : squares2) {
            Rectangle rectangle = square.getRectangle();
            game.batch.draw(yellowSquare, rectangle.getX(), rectangle.getY(),
                    rectangle.getWidth() - (2f/game.PTM_RATIO), rectangle.getHeight() - (2f/game.PTM_RATIO));
        }

        for(Circle circle : circles3) {
            Rectangle rectangle = circle.getRectangle();
            game.batch.draw(redCircle, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        }


        // Draw squares
        for(Square square : squares3) {
            Rectangle rectangle = square.getRectangle();
            game.batch.draw(redSquare, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        }



        game.batch.end();


        game.batch.setProjectionMatrix(gamecam.combined);

        game.shapeRenderer.setProjectionMatrix(gamecam.combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw Engines
        for(Engine engine : engines1) {
            Rectangle rect = engine.getRectangle();

            game.shapeRenderer.setColor(new Color(12/255f, 156/255f, 181/255f, 1));
            game.shapeRenderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

        }

        for(Engine engine : engines2) {
            Rectangle rect = engine.getRectangle();

            game.shapeRenderer.setColor(new Color(255/255f, 231/255f, 136/255f, 1));
            game.shapeRenderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

        }

        for(Engine engine : engines3) {
            Rectangle rect = engine.getRectangle();

            game.shapeRenderer.setColor(new Color(234/255f, 60/255f, 95/255f, 1));
            game.shapeRenderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

        }



        game.shapeRenderer.end();


        game.batch.begin();


        // Draw Background Texture
        com.badlogic.gdx.math.Circle rectangle = plane1.returnRectangle();
        game.batch.draw(planeTexture, rectangle.x  - (plane1.WIDTH / game.PTM_RATIO / 2),
                rectangle.y - (plane1.HEIGHT / game.PTM_RATIO / 2), 60 / game.PTM_RATIO, 65 / game.PTM_RATIO);


        rectangle = plane2.returnRectangle();
        game.batch.draw(planeTexture, rectangle.x  - (plane1.WIDTH / game.PTM_RATIO / 2),
                rectangle.y - (plane1.HEIGHT / game.PTM_RATIO / 2), 60 / game.PTM_RATIO, 65 / game.PTM_RATIO);

        rectangle = plane3.returnRectangle();
        game.batch.draw(planeTexture, rectangle.x  - (plane1.WIDTH / game.PTM_RATIO / 2),
                rectangle.y - (plane1.HEIGHT / game.PTM_RATIO / 2), 60 / game.PTM_RATIO, 65 / game.PTM_RATIO);




        game.batch.end();

        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();

        pauseSprite.draw(game.batch);

        layout.setText(game.font24, String.format("%01d", score));
        textWidth = layout.width;

        game.font24.draw(game.batch,  String.format("%01d", score), game.WIDTH - textWidth - 5, 790);



        game.batch.end();

        if(state == State.PAUSE) {

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            game.shapeRenderer.setProjectionMatrix(gamecam.combined);
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(new Color(0, 0, 0, 0.8f));
            game.shapeRenderer.rect(0, 0, game.WIDTH / game.PTM_RATIO, game.HEIGHT / game.PTM_RATIO);
            game.shapeRenderer.setColor(Color.WHITE);
            game.shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            game.batch.setProjectionMatrix(hudCamera.combined);
            game.batch.begin();

            smallPlay.draw(game.batch);
            smallHome.draw(game.batch);

            game.batch.end();
        }




        if(state == State.STOPPED) {
            if(stopOppasity < 0.8) {
                stopOppasity += 0.05;
            }
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            game.shapeRenderer.setProjectionMatrix(gamecam.combined);
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(new Color(0, 0, 0, stopOppasity));
            game.shapeRenderer.rect(0, 0, game.WIDTH / game.PTM_RATIO, game.HEIGHT / game.PTM_RATIO);
            game.shapeRenderer.setColor(Color.WHITE);
            game.shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            game.batch.setProjectionMatrix(hudCamera.combined);
            game.batch.begin();


            if(stopOppasity >= 0.7) {
                layout.setText(game.gameOverFont, "GAME OVER");
                textWidth = layout.width;


                game.gameOverFont.draw(game.batch, "GAME OVER", game.WIDTH / 2 - textWidth / 2, (game.HEIGHT / 6) * 5);

                layout.setText(game.fontRecord, "SCORE");
                textWidth = layout.width;

                game.fontRecord.draw(game.batch, "SCORE", game.WIDTH / 2 - textWidth - 35, ((game.HEIGHT / 6) * 5) - 85);

                game.fontRecord.draw(game.batch, "BEST", game.WIDTH / 2 - textWidth - 35, ((game.HEIGHT / 6) * 5) - 135);


                layout.setText(game.fontRecord, String.format("%01d", score));
                textWidth = layout.width;

                game.fontRecord.draw(game.batch, String.format("%01d", score), (game.WIDTH / 12) * 9 - textWidth,
                        ((game.HEIGHT / 6) * 5) - 80);

                layout.setText(game.fontRecord, String.format("%01d", record));
                textWidth = layout.width;

                game.fontRecord.draw(game.batch, String.format("%01d", record), (game.WIDTH / 12) * 9 - textWidth,
                        ((game.HEIGHT / 6) * 5) - 130);


                playAgain.draw(game.batch);
                verySmallHome.draw(game.batch);
            }

            game.batch.end();
        }

    }



    public void columnDivider1(float pos1, float pos2, int distance) {
        int temp = (Math.random() <= 0.5) ? 1 : 2;

        // Choose row 1 or 2
        if(temp == 1) {

            temp = (Math.random() <= 0.5) ? 1 : 2;
            // Choose square or circle
            if(temp == 1) {
                circles1.add(new Circle(game, pos1, distance));
            }
            else {
                squares1.add(new Square(game, pos1, distance));

            }
        }
        else {
            temp = (Math.random() <= 0.5) ? 1 : 2;
            if(temp == 1) {
                circles1.add(new Circle(game, pos2, distance));
            }
            else {
                squares1.add(new Square(game, pos2, distance));
            }

        }
    }

    public void columnDivider2(float pos1, float pos2, int distance) {
        int temp = (Math.random() <= 0.5) ? 1 : 2;

        // Choose row 1 or 2
        if(temp == 1) {

            temp = (Math.random() <= 0.5) ? 1 : 2;
            // Choose square or circle
            if(temp == 1) {
                circles2.add(new Circle(game, pos1, distance));
            }
            else {
                squares2.add(new Square(game, pos1, distance));

            }
        }
        else {
            temp = (Math.random() <= 0.5) ? 1 : 2;
            if(temp == 1) {
                circles2.add(new Circle(game, pos2, distance));
            }
            else {
                squares2.add(new Square(game, pos2, distance));
            }

        }
    }

    public void columnDivider3(float pos1, float pos2, int distance) {
        int temp = (Math.random() <= 0.5) ? 1 : 2;

        // Choose row 1 or 2
        if(temp == 1) {

            temp = (Math.random() <= 0.5) ? 1 : 2;
            // Choose square or circle
            if(temp == 1) {
                circles3.add(new Circle(game, pos1, distance));
            }
            else {
                squares3.add(new Square(game, pos1, distance));

            }
        }
        else {
            temp = (Math.random() <= 0.5) ? 1 : 2;
            if(temp == 1) {
                circles3.add(new Circle(game, pos2, distance));
            }
            else {
                squares3.add(new Square(game, pos2, distance));
            }

        }
    }





    public void overlapDetector1(float dt) {
        for (int i = 0; i < circles1.size; i++) {
            Circle circle = circles1.get(i);

            // Circle and Plane HitBox
            if(circle.overLapDetector(plane1.returnRectangle())) {
                circles1.removeIndex(i);
                score += 1;
                if(game.soundState) {
                    pickUp.play(soundVolume);
                }
            }




            if(circle.getRectangle().y <= 50 / game.PTM_RATIO) {
                if(game.soundState && deadSound == false) {
                    deadSound = true;
                    notPicked.play(soundVolume2);
                }
                state = State.CIRCLE;
                if(circle.animation(dt)) {
                    state = State.STOPPED;
                }


            }

        }

        for (int i = 0; i < squares1.size; i++) {
            Square square = squares1.get(i);

            if(square.overLapDetector(plane1.returnRectangle())) {
                if(game.soundState && deadSound == false) {
                    deadSound = true;
                    notPicked.play(soundVolume2);
                }
                state = State.SQUARE;
                if(square.animation()) {
                    state = State.STOPPED;
                }

            }

            // Square and floor HitBox
            if(square.getRectangle().getY() <= -square.HEIGHT / game.PTM_RATIO) {
                squares1.removeIndex(i);
            }
        }
    }

    public void overlapDetector2(float dt) {
        for (int i = 0; i < circles2.size; i++) {
            Circle circle = circles2.get(i);

            // Circle and Plane HitBox

            if(circle.overLapDetector(plane2.returnRectangle())) {
                circles2.removeIndex(i);
                score += 1;
                if(game.soundState) {
                    pickUp.play(soundVolume);
                }
            }


//             Circle and floor HitBox

            if(circle.getRectangle().y <= 50 / game.PTM_RATIO) {
                if(game.soundState && deadSound == false) {
                    deadSound = true;
                    notPicked.play(soundVolume2);
                }
                state = State.CIRCLE;
                if(circle.animation(dt)) {
                    state = State.STOPPED;
                }
            }

        }

        for (int i = 0; i < squares2.size; i++) {
            Square square = squares2.get(i);

            if(square.overLapDetector(plane2.returnRectangle())) {
                if(game.soundState && deadSound == false) {
                    deadSound = true;
                    notPicked.play(soundVolume2);
                }
                state = State.SQUARE;
                if(square.animation()) {
                    state = State.STOPPED;
                }
            }

            // Square and floor HitBox
            if(square.getRectangle().getY() <= -square.HEIGHT / game.PTM_RATIO) {
                squares2.removeIndex(i);
            }
        }
    }

    public void overlapDetector3(float dt) {
        for (int i = 0; i < circles3.size; i++) {
            Circle circle = circles3.get(i);

            // Circle and Plane HitBox

            if(circle.overLapDetector(plane3.returnRectangle())) {
                circles3.removeIndex(i);
                score += 1;
                if(game.soundState) {
                    pickUp.play(soundVolume);
                }
            }

            if(circle.getRectangle().y <= 50 / game.PTM_RATIO) {
                if(game.soundState && deadSound == false) {
                    deadSound = true;
                    notPicked.play(soundVolume2);
                }
                state = State.CIRCLE;
                if(circle.animation(dt)) {
                    state = State.STOPPED;
                }
            }

        }

        for (int i = 0; i < squares3.size; i++) {
            Square square = squares3.get(i);

            if(square.overLapDetector(plane3.returnRectangle())) {
                if(game.soundState && deadSound == false) {
                    deadSound = true;
                    notPicked.play(soundVolume2);
                }
                state = State.SQUARE;
                if(square.animation()) {
                    System.out.println("STOP");
                    state = State.STOPPED;
                }
            }

            // Square and floor HitBox
            if(square.getRectangle().getY() <= -square.HEIGHT / game.PTM_RATIO) {
                squares3.removeIndex(i);
            }
        }
    }

    public void enginesUpdate(float positionX1, float positionX2, float positionX3, float positionY, float dt) {

        enginesTime += dt;

        if(enginesTime >= 0.1) {
            engines1.add(new Engine(game, positionX1, positionY));
            engines2.add(new Engine(game, positionX2, positionY));
            engines3.add(new Engine(game, positionX3, positionY));
            enginesTime = 0;
        }



        for(int i=0; i< engines1.size; i++) {
            Engine engine = engines1.get(i);
            engine.update();

            if(engine.getRectangle().getWidth() <= 0) {
                engines1.removeIndex(i);
            }
        }

        for(int i=0; i< engines2.size; i++) {
            Engine engine = engines2.get(i);
            engine.update();

            if(engine.getRectangle().getWidth() <= 0) {
                engines2.removeIndex(i);
            }
        }

        for(int i=0; i< engines3.size; i++) {
            Engine engine = engines3.get(i);
            engine.update();

            if(engine.getRectangle().getWidth() <= 0) {
                engines3.removeIndex(i);
            }
        }

    }

    public void pauseGame(float dt) {
        if(state == State.PAUSE) {
            restartTime += dt;
            if(restartTime >= 0.5f) {

            }
        }
    }


    public void restartGame(float dt) {
        if(state == State.STOPPED) {

            if(score > record) {
                //put some Integer
                prefs.putInteger("score", score);

                //persist preferences
                prefs.flush();
                record = score;
            }

        }
    }

    public void setButton(Sprite sprite, float x, float y, float width, float height) {
        sprite.setPosition(x, y);
        sprite.setSize(width, height);
    }

    private boolean checkIfClicked (Sprite sprite, float ix, float iy) {
        return (ix > sprite.getX() && ix < sprite.getX() + sprite.getWidth()) &&
                (iy > sprite.getY() && iy < sprite.getY() + sprite.getHeight());
    }



    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        hudPortCam.update(width, height);
    }



    @Override
    public void pause() {
        if(state == State.RUN) {
            state = State.PAUSE;
        }

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
        planeTexture.dispose();

        blueCircle.dispose();
        redCircle.dispose();
        yellowCircle.dispose();

        blueSquare.dispose();
        redSquare.dispose();
        yellowSquare.dispose();

        pauseButton.dispose();
        smallPlayButton.dispose();
        smallHomeButton.dispose();
        verySmallHomeButton.dispose();
        playAgainButton.dispose();

        pickUp.dispose();
        notPicked.dispose();
    }
}
