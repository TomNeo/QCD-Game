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
import com.mygdx.game.Variables;

public class MenuScreen implements Screen {

    private MyGdxGame game;
    private Stage stageUI;
    private ParticleEffect effect;
    float currentDegrees = 0;
    int radiusFlux = -5;
    int radiusChange = 0;

    float P0x = 40;
    float P0y = 140;
    float P1x = 80;
    float P1y = 140 + 70.71f;
    float P2x = 120;
    float P2y = 140;

    //create paths
    private Bezier<Vector2> path1;
    private Bezier<Vector2> path3;
    private CatmullRomSpline<Vector2> path2;
    private Bezier<Vector2>[] circlePath;

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

        Vector2[] RossPoints = new Vector2[]{new Vector2(P0x, P0y), new Vector2(P1x, P1y), new Vector2(P2x, P2y)};





        // set up the curves
        path1 = new Bezier<Vector2>(controlPoints);
        path2 = new CatmullRomSpline<Vector2>(controlPoints, true);
        path3 = new Bezier<Vector2>(RossPoints);

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
/*
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

        //Ross's curves
        game.shapeRenderer.setColor(.5f, 1f, 1f, 1);
        game.shapeRenderer.line(P0x, P0y, P1x, P1y);
        game.shapeRenderer.line(P1x, P1y, P2x, P2y);
        game.shapeRenderer.line(P0x, P0y, P2x, P2y);
        game.shapeRenderer.setColor(1f, 1f, 1f, 1);
        float segments = 100;
        for(int i = 0; i < segments; ++i){
            float t = i /segments;
            Vector2 st = new Vector2();
            Vector2 end = new Vector2();
            path3.valueAt(st,t);
            path3.valueAt(end, t-0.01f);

            if(i == 50){
                int breakpoint = 0;
            }
            game.shapeRenderer.line(st.x, st.y, end.x, end.y);

        }
*/
        if(radiusFlux == -5){
            radiusChange = 1;
        }
        if(radiusFlux > 5){
            radiusChange = -1;
        }
        radiusFlux = radiusFlux + radiusChange;

        int waveFrequency = 9;
        float OriginX = 800;
        float OriginY = 200;
        float radius1 = Variables.HELIUM_RADIUS + radiusFlux;
        float tempCureentDegrees = currentDegrees;
        currentDegrees = currentDegrees + 1;
        float nextDegrees;
        float amplitude1 = radius1/20f;
        float segments = 10;

        circlePath = new Bezier[waveFrequency * 2];

        for(int i = 0; i < (waveFrequency * 2);i++){
            nextDegrees = tempCureentDegrees + (360/(waveFrequency * 2));
            Vector2[] currentPositions = new Vector2[3];
            currentPositions[0] = new Vector2((float)Math.cos(Math.toRadians(tempCureentDegrees))*radius1+OriginX,(float)Math.sin(Math.toRadians(tempCureentDegrees))*radius1+OriginY);
            currentPositions[2] = new Vector2((float)Math.cos(Math.toRadians(nextDegrees))*radius1+OriginX,(float)Math.sin(Math.toRadians(nextDegrees))*radius1+OriginY);

            float midPointX = (currentPositions[0].x + currentPositions[2].x)/2;
            float midPointY = (currentPositions[0].y + currentPositions[2].y)/2;
            float midPointDistance = (float)Math.sqrt((midPointX - OriginX)*(midPointX - OriginX) + (midPointY - OriginY)*(midPointY - OriginY));
            float bezierDistance = (radius1 - midPointDistance) + radius1;
            if(i%2 == 0){
                bezierDistance = 2 * (radius1 - midPointDistance) + radius1 + amplitude1;
            }else{
                bezierDistance = radius1 - (radius1 - midPointDistance)*2 - amplitude1;
            }

            currentPositions[1] = new Vector2((float)Math.cos(Math.toRadians((tempCureentDegrees + nextDegrees)/2))*bezierDistance+OriginX,(float)Math.sin(Math.toRadians((tempCureentDegrees + nextDegrees)/2))*bezierDistance+OriginY);
            circlePath[i] =  new Bezier<Vector2>(currentPositions);
            tempCureentDegrees = nextDegrees;
        }

        game.shapeRenderer.setColor(0f, 1f, 0f, 1);
        float radius2 = Variables.HELIUM_RADIUS - radiusFlux;
        float amplitude2 = radius1/10f;

        for(int i = 0; i < circlePath.length; i++){
            for(int j = 0; j < segments; ++j){
                float t = j /segments;
                Vector2 st = new Vector2();
                Vector2 end = new Vector2();
                circlePath[i].valueAt(st,t);
                circlePath[i].valueAt(end, t+1/(segments));
                game.shapeRenderer.line(st.x, st.y, end.x, end.y);
            }
        }

        tempCureentDegrees = -currentDegrees + (360/(waveFrequency * 2));

        circlePath = new Bezier[waveFrequency * 2];

        for(int i = 0; i < (waveFrequency * 2);i++){
            nextDegrees = tempCureentDegrees + (360/(waveFrequency * 2));
            Vector2[] currentPositions = new Vector2[3];
            currentPositions[0] = new Vector2((float)Math.cos(Math.toRadians(tempCureentDegrees))*radius2+OriginX,(float)Math.sin(Math.toRadians(tempCureentDegrees))*radius2+OriginY);
            currentPositions[2] = new Vector2((float)Math.cos(Math.toRadians(nextDegrees))*radius2+OriginX,(float)Math.sin(Math.toRadians(nextDegrees))*radius2+OriginY);

            float midPointX = (currentPositions[0].x + currentPositions[2].x)/2;
            float midPointY = (currentPositions[0].y + currentPositions[2].y)/2;
            float midPointDistance = (float)Math.sqrt((midPointX - OriginX)*(midPointX - OriginX) + (midPointY - OriginY)*(midPointY - OriginY));
            float bezierDistance = (radius2 - midPointDistance) + radius2;
            if(i%2 == 0){
                bezierDistance = 2 * (radius2 - midPointDistance) + radius2 + amplitude2;
            }else{
                bezierDistance = radius2 - (radius2 - midPointDistance)*2 - amplitude2;
            }

            currentPositions[1] = new Vector2((float)Math.cos(Math.toRadians((tempCureentDegrees + nextDegrees)/2))*bezierDistance+OriginX,(float)Math.sin(Math.toRadians((tempCureentDegrees + nextDegrees)/2))*bezierDistance+OriginY);
            circlePath[i] =  new Bezier<Vector2>(currentPositions);
            tempCureentDegrees = nextDegrees;
        }

        game.shapeRenderer.setColor(0f, 1f, 0f, 1);

        for(int i = 0; i < circlePath.length; i++){
            for(int j = 0; j < segments; ++j){
                float t = j /segments;
                Vector2 st = new Vector2();
                Vector2 end = new Vector2();
                circlePath[i].valueAt(st,t);
                circlePath[i].valueAt(end, t+1/(segments));
                game.shapeRenderer.line(st.x, st.y, end.x, end.y);
            }
        }

        game.shapeRenderer.circle(OriginX, OriginY, radius1);
        game.shapeRenderer.circle(OriginX, OriginY, radius2);
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
