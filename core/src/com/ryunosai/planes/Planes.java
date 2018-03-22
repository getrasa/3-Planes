package com.ryunosai.planes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ryunosai.planes.screens.StartScreen;

public class Planes extends Game {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final int PTM_RATIO = 4;
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;

	public BitmapFont font24;
	public BitmapFont fontScore;
	public BitmapFont fontRecord;
	public BitmapFont gameOverFont;

	public boolean musicState;
	public boolean soundState;

	public int firstTime;

	public Music music;

	AdHandler handler;
	boolean toggle;

	public Preferences prefs;

	public Planes(AdHandler handler) {
		this.handler = handler;
	}


	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		prefs = Gdx.app.getPreferences("mySound");



		getPreferences();
		music = Gdx.audio.newMusic(Gdx.files.internal("music/bensound-betterdays.mp3"));
		// Music by "Music: www.bensound.com”
		music.setLooping(true);
		music.setVolume(0.5f);


		if(musicState) {
			music.play();
		}


		setScreen(new StartScreen(this));

		initFont();

	}

	public void getPreferences() {
//		firstTime = prefs.getInteger("firstTime", 0);
		musicState = prefs.getBoolean("music", true);
		soundState = prefs.getBoolean("sound", true);

	}





	@Override
	public void render () {
		if(Gdx.input.justTouched()) {
			handler.showAds(toggle);
			toggle = !toggle;
		}
		super.render();

	}

	@Override
	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();
		font24.dispose();
		fontScore.dispose();
		fontRecord.dispose();
		gameOverFont.dispose();
		music.dispose();
	}

	public void initFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-CondLight.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();


		params.size = 62;
		params.color = Color.WHITE;
		font24 = generator.generateFont(params);

		params.size = 120;
		params.color = Color.WHITE;
		fontScore = generator.generateFont(params);

		params.size = 42;
		fontRecord = generator.generateFont(params);



		generator.dispose();

		FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Raleway-Thin.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params2 = new FreeTypeFontGenerator.FreeTypeFontParameter();


		params2.size = 68;
		params2.color = Color.WHITE;
		gameOverFont = generator2.generateFont(params2);

		generator2.dispose();
	}


}
