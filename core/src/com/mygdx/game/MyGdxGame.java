package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

	@Override
	public void create () {
		batch = new SpriteBatch();

        shapeRenderer = new ShapeRenderer();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("SecretST.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.YELLOW;
        parameter.size = 40;
        font = generator.generateFont(parameter);

        setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
        super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		generator.dispose();
		shapeRenderer.dispose();
	}

}
