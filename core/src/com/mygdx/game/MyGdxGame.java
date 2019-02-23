package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.atoms.Circles;
import com.mygdx.game.screens.MenuScreen;

import java.util.ArrayList;

public class MyGdxGame extends Game {

    private FreeTypeFontGenerator generator;
	public ArrayList<Circles> allCircles;
	public ArrayList<Circles> addToCircles;
    public BitmapFont font;
	public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public Circles highlightedCircle = null;
    public Skin skin;
    public Sound soundEffect;

	@Override
	public void create () {
		batch = new SpriteBatch();

        shapeRenderer = new ShapeRenderer();

		skin = new Skin();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("SecretST.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.YELLOW;
        parameter.size = 40;
        font = generator.generateFont(parameter);
        skin.add("font", font, BitmapFont.class);

        if (Gdx.app.getType() == Application.ApplicationType.Android ||
				Gdx.app.getType() == Application.ApplicationType.Desktop) {
			soundEffect = Gdx.audio.newSound(Gdx.files.internal(Variables.SFX_CREATE_ATOM + ".ogg"));
		} else if (Gdx.app.getType() == Application.ApplicationType.iOS) {
			soundEffect = Gdx.audio.newSound(Gdx.files.internal(Variables.SFX_CREATE_ATOM + ".mp3"));
		}

        setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
        super.render();
	}

	@Override
	public void dispose () {

		if (skin != null) {
            if (skin.has("font", BitmapFont.class))
                skin.remove("font", BitmapFont.class);
			skin.dispose();
		}

		batch.dispose();
		font.dispose();
		generator.dispose();
		shapeRenderer.dispose();
	}

}
