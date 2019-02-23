package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGdxGame;

public class GameOverScreen implements Screen {

    private final MyGdxGame game;
    private float width = 1980f;
    private float height = 1080f;
    private Image imgHome, imgReplay;
    private OrthographicCamera camera;
    private Stage stageUI;

    GameOverScreen(final MyGdxGame game, long peakLife, final long highestScore, long totalScored,
                   long Protons, float runTime) {
        this.game = game;

        camera = new OrthographicCamera(width, height);
        camera.position.set(width/2, height/2, 0);
        camera.update();

        stageUI = new Stage(new StretchViewport(960, 540, camera));
        Gdx.input.setInputProcessor(stageUI);

        imgHome = new Image(new Texture("home.png"));
        imgReplay = new Image(new Texture("replay.png"));

        imgHome.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getType() == InputEvent.Type.touchUp) {
                    game.setScreen(new MenuScreen(game));
                    dispose();
                }
            }
        });

        imgReplay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getType() == InputEvent.Type.touchUp) {
                    game.setScreen(new GameScreen(game, highestScore));
                    dispose();
                }
            }
        });

        Table table = new Table();
        table.setSize(960, 540);

        Label label1 = new Label("Your highest health this round: " + peakLife, game.skin, "font", Color.YELLOW);
        label1.setAlignment(Align.center);
        table.add(label1).padBottom(20).colspan(2).row();

        Label label2 = new Label("Current Highest Score: " + highestScore, game.skin, "font", Color.YELLOW);
        label2.setAlignment(Align.center);
        table.add(label2).padBottom(20).colspan(2).row();

        Label label3 = new Label("Time: " + runTime, game.skin, "font", Color.YELLOW);
        label3.setAlignment(Align.center);
        table.add(label3).padBottom(20).colspan(2).row();

        Label label4 = new Label("Score this Round: " + totalScored, game.skin, "font", Color.YELLOW);
        label4.setAlignment(Align.center);
        table.add(label4).padBottom(20).colspan(2).row();

        Label label5 = new Label("Protons paid for: " + Protons, game.skin, "font", Color.YELLOW);
        label5.setAlignment(Align.center);
        table.add(label5).padBottom(20).colspan(2).row();

        table.add(imgHome).prefSize(128, 128).padRight(48);
        table.add(imgReplay).prefSize(128, 128).padLeft(48).row();

        stageUI.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stageUI.act();
        stageUI.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        stageUI.dispose();
    }
}
