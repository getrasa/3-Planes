package com.ryunosai.planes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ryunosai.planes.Planes;

/**
 * Created by getrasa on 30/10/16.
 */

public class StartScreen implements Screen {
    private Planes game;
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    private Texture startBackground;

    private Texture bigPlay;

    private Texture musicOnTexture, musicOffTexture, soundOnTexture, soundOffTexture;

    private Sprite playButton, musicState, soundState;

    private GlyphLayout layout;

    private float startOppasity = 0.8f;

    private boolean playPressed = false;


    public StartScreen(Planes game) {
        this.game = game;



        // Create Camera na set initial position

        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(game.WIDTH, game.HEIGHT, gamecam);
        gamecam.position.set(game.WIDTH / 2, game.HEIGHT / 2, 0);



        startBackground = new Texture("start-background.png");

        bigPlay = new Texture("icons/big-play.png");
        musicOnTexture = new Texture("icons/music-on.png");
        musicOffTexture = new Texture("icons/music-off.png");
        soundOnTexture = new Texture("icons/sound-on.png");
        soundOffTexture = new Texture("icons/sound-off.png");

        playButton = new Sprite(bigPlay);

        if(game.musicState) {
            musicState = new Sprite(musicOnTexture);
        }
        else {
            musicState = new Sprite(musicOffTexture);
        }

        if(game.soundState) {
            soundState = new Sprite(soundOnTexture);
        }
        else {
            soundState = new Sprite(soundOffTexture);
        }



        setButton(playButton, game.WIDTH / 2 - bigPlay.getWidth() / 2, game.HEIGHT / 2 - 30,
                bigPlay.getWidth(), bigPlay.getHeight());

        setButton(musicState, game.WIDTH / 2 - musicOnTexture.getWidth() - 20, game.HEIGHT / 3,
                musicOnTexture.getWidth(), musicOnTexture.getHeight());

        setButton(soundState, game.WIDTH / 2 + 20, game.HEIGHT / 3,
                musicOnTexture.getWidth(), musicOnTexture.getHeight());


    }



    @Override
    public void show() {

    }

    public void inputHandler(float dt) {

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                Vector3 touchPos = new Vector3(x, y, 0);
                gamecam.unproject(touchPos);

                if(checkIfClicked(playButton, touchPos.x, touchPos.y)) {
                    playPressed = true;

                }

                if(checkIfClicked(musicState, touchPos.x, touchPos.y)) {
                    if(game.musicState) {
                        musicState.setTexture(musicOffTexture);
                        game.musicState = false;
                        game.music.stop();

                        game.prefs.putBoolean("music", game.musicState);
                        game.prefs.flush();

                    }
                    else {
                        musicState.setTexture(musicOnTexture);
                        game.musicState = true;
                        game.music.play();
                        game.prefs.putBoolean("music", game.musicState);
                        game.prefs.flush();
                    }
                }

                if(checkIfClicked(soundState, touchPos.x, touchPos.y)) {
                    if(game.soundState) {
                        soundState.setTexture(soundOffTexture);
                        game.soundState = false;
                        game.prefs.putBoolean("sound", game.musicState);
                        game.prefs.flush();
                    }
                    else {
                        soundState.setTexture(soundOnTexture);
                        game.soundState = true;
                        game.prefs.putBoolean("sound", game.musicState);
                        game.prefs.flush();
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

        if(playPressed) {
            startOppasity -= 0.05;
            if(startOppasity <= 0) {
                game.setScreen(new PlayScreen(game));
            }
        }


    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        game.batch.draw(startBackground, 0, 0, game.WIDTH, game.HEIGHT);

        game.batch.end();


        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shapeRenderer.setProjectionMatrix(gamecam.combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(new Color(0, 0, 0, startOppasity));
        game.shapeRenderer.rect(0, 0, game.WIDTH, game.HEIGHT);
        game.shapeRenderer.setColor(Color.WHITE);
        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        layout = new GlyphLayout(game.fontScore, "3 PLANES");
        float textWidth = layout.width;

        if(startOppasity >= 0.7) {
            game.fontScore.draw(game.batch, "3 PLANES", game.WIDTH / 2 - textWidth / 2, (game.HEIGHT / 6) * 5);

            playButton.draw(game.batch);
            musicState.draw(game.batch);
            soundState.draw(game.batch);
        }

        game.batch.end();
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
        startBackground.dispose();
        bigPlay.dispose();
        musicOffTexture.dispose();
        musicOffTexture.dispose();
        soundOnTexture.dispose();
        soundOffTexture.dispose();
    }
}
