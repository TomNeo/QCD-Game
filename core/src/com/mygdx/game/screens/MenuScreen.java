package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGdxGame;

public class MenuScreen implements Screen {

    private MyGdxGame game;
    private Stage stageUI;
    private ParticleEffect effect;

    //create paths
    private Bezier<Vector2> path1;
    private CatmullRomSpline<Vector2> path2;

    public MenuScreen(MyGdxGame gam) {
        game = gam;

        OrthographicCamera camera = new OrthographicCamera();

        stageUI = new Stage(new StretchViewport(960, 540, camera));
        Gdx.input.setInputProcessor(stageUI);

        Image imgExit = new Image(new Texture("exit.png"));
        Image imgPlay = new Image(new Texture("play.png"));

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

        game.batch.setProjectionMatrix(camera.combined);

        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("particles/Hallucinogen/hallucinogen_full.p"), game.particleAtlas);
        effect.setPosition(960/2f, 400f);
        effect.start();

        // set up random control points
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        int points = 4;
        Vector2[] controlPoints = new Vector2[points];
        for (int i = 0; i < points; i++) {
            int x = (int) (Math.random() * width) ;
            int y = (int) (Math.random() * height);
            Vector2 point = new Vector2(x, y);
            controlPoints[i] = point;
        }

        // set up the curves
        path1 = new Bezier<Vector2>(controlPoints);
        path2 = new CatmullRomSpline<Vector2>(controlPoints, true);

        // setup ShapeRenderer
        game.shapeRenderer.setAutoShapeType(true);
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

        game.shapeRenderer.setColor(1, 1, 1, 1);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        //draw path1
        for(int i = 0; i < 100; ++i){
            float t = i /100f;
            // create vectors to store start and end points of this section of the curve
            Vector2 st = new Vector2();
            Vector2 end = new Vector2();
            // get the start point of this curve section
            path1.valueAt(st,t);
            // get the next start point(this point's end)
            path1.valueAt(end, t-0.01f);
            // draw the curve
            game.shapeRenderer.line(st.x, st.y, end.x, end.y);

        }

        //same as above but for catmullrom
        game.shapeRenderer.setColor(1, 0.13f, 0.5f, 1);
        for(int i = 0; i < 100; ++i){
            float t = i /100f;
            Vector2 st = new Vector2();
            Vector2 end = new Vector2();
            path2.valueAt(st,t);
            path2.valueAt(end, t-0.01f);
            game.shapeRenderer.line(st.x, st.y, end.x, end.y);

        }

        game.shapeRenderer.end();
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
        effect.dispose();
    }
}
