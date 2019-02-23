package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGdxGame;

public class MenuScreen implements Screen {

    private MyGdxGame game;
    private OrthographicCamera camera;
    private Image imgExit, imgPlay;
    private Stage stageUI;
    private ParticleEffect effect;

    public MenuScreen(MyGdxGame gam) {
        game = gam;

        camera = new OrthographicCamera();

        stageUI = new Stage(new StretchViewport(960, 540, camera));
        Gdx.input.setInputProcessor(stageUI);

        imgExit = new Image(new Texture("exit.png"));
        imgPlay = new Image(new Texture("play.png"));

        imgExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getType() == InputEvent.Type.touchUp) {
                    Gdx.app.exit();
                }
            }
        });

        imgPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getType() == InputEvent.Type.touchUp) {
                    game.setScreen(new GameScreen(game, 0));
                    dispose();
                }
            }
        });

        Table btnTable = new Table();
        btnTable.setSize(960, 540);
        btnTable.add(imgExit).prefSize(128, 128).padRight(48);
        btnTable.add(imgPlay).prefSize(128, 128).padLeft(48).row();

        stageUI.addActor(btnTable);

        TextureAtlas particleAtlas = new TextureAtlas(); //<-load some atlas with your particle assets in
        TextureRegion ppNeutron = new TextureRegion(new Texture("particles/Hallucinogen/pp_neutron.png"));
        TextureRegion ppNeutronFull = new TextureRegion(new Texture("particles/Hallucinogen/pp_neutron-full.png"));
        particleAtlas.addRegion("pp_neutron", ppNeutron);
        particleAtlas.addRegion("pp_neutron-full", ppNeutronFull);
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("particles/Hallucinogen/hallucinogen_full.p"), particleAtlas);
        effect.setPosition(1366/2f, 600f);
        effect.start();
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

        game.batch.begin();
        //Updating and Drawing the particle effect
        //Delta being the time to progress the particle effect by, usually you pass in Gdx.graphics.getDeltaTime();
        effect.draw(game.batch, delta);

        game.batch.end();
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
