package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {


    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    BitmapFont font;
	SpriteBatch batch;
    //public BitmapFont font;
    Texture img;
    OrthographicCamera camera;
    ArrayList<Circles> allCircles;
    ArrayList<Circles> addToCircles;
    private int width = 1920;
    private int height = 1080;
    ShapeRenderer shapeRenderer;
    boolean stillTouched = false;
    float touchedFor = 0f;
    Vector3 initialPress = new Vector3();
    Vector3 lastPress = new Vector3();
    static float TOUCH_TIME_OUT = .3f;
    long startingScore = 15 * 200;
    long currentScore = startingScore;
    long highScore = startingScore;
    long highestScore = startingScore;
    float runTime = 0;
    long totalScored = 0;
    long Protons = 0;
    boolean pastFirstScreen = false;
    float r = 0;
    float g = 0;
    float b = 0;
    float colorTime = 0;
    float colorLength = 3;
    boolean colorTimeGoingUp = true;
    int colorCounter = 0;
	int timesChanged = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
        camera = new OrthographicCamera(width, height);
        camera.position.x=width/2;
        camera.position.y = height/2;
        camera.update();
        img = new Texture("Hex-grid.png");
        allCircles = new ArrayList<Circles>();
        addToCircles = new ArrayList<Circles>();
        shapeRenderer = new ShapeRenderer();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("SecretST.TTF"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.YELLOW;
        parameter.size = 40;
        font = generator.generateFont(parameter);
	}

	@Override
	public void render () {

        float deltaTime = Gdx.graphics.getDeltaTime();

        if(colorTimeGoingUp) {
            colorTime = colorTime + deltaTime;
        }else{
            colorTime = colorTime - deltaTime;
        }
        if(colorTime >= colorLength){
            colorTimeGoingUp = false;
            colorCounter++;
        }else if(colorTime <= 0){
            colorTimeGoingUp = true;
            colorCounter++;
        }

        if(colorCounter == 5 && timesChanged == 0){
            timesChanged++;
            colorLength = 2.5f;
            colorTimeGoingUp = true;
            colorTime = 0;
        }else if(colorCounter == 12 && timesChanged == 1){
            timesChanged++;
            colorLength = 1.5f;
            colorTimeGoingUp = true;
            colorTime = 0;
        } if(colorCounter == 22 && timesChanged == 2){
            timesChanged++;
            colorLength = .9f;
            colorTimeGoingUp = true;
            colorTime = 0;
        }

        if(colorCounter < 5) {
            r = (70f / 256f) * (colorTime / colorLength);
            g = (30f / 256f) * (colorTime / colorLength);
            b = (100f / 256f) * (colorTime / colorLength);
        }else if(colorCounter < 12){
            r = (70f + (111f * (colorTime / colorLength))) / 256f;
            g = (30f + (66f * (colorTime / colorLength)))/ 256f;
            b = (100f - (26f * (colorTime / colorLength)))/ 256f;
        }else if(colorCounter < 22){
            r = (181f + (34f * (colorTime / colorLength))) / 256f;
            g = (96f + (66f * (colorTime / colorLength)))/ 256f;
            b = (74f - (34f * (colorTime / colorLength)))/ 256f;
        }else{
            r = (215f + (15f * (colorTime / colorLength))) / 256f;
            g = (162f + (93f * (colorTime / colorLength)))/ 256f;
            b = (40f - (14f * (colorTime / colorLength)))/ 256f;
        }

		Gdx.gl.glClearColor(r, g, b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);


            if (currentScore > 0) {
                if (currentScore != startingScore) {
                    runTime = runTime + deltaTime;
                }
                newClick(deltaTime);
                moveCircles(deltaTime);
                addNewCircles();
                tick(deltaTime);
                /*
                batch.begin();
                batch.draw(img, 0, 0);
                batch.end();
                */
                drawCircles();
                drawConnection();
                batch.begin();
                font.draw(batch, "Score: " + currentScore, 0, 20);
                batch.end();
            } else {
                r = 0;
                g = 0;
                b = 0;
                colorTime = 0;
                colorLength = 3;
                colorTimeGoingUp = true;
                colorCounter = 0;
                timesChanged = 0;
                allCircles.clear();
                batch.begin();
                font.draw(batch, "Your high score this round: " + highScore + " . Highest Score: " + highestScore, width / 4, height / 2 + 20);
                font.draw(batch, "Time: " + runTime + ". Total Points Earned: " + totalScored + " Protons paid for: " + Protons, width / 5, height / 2 - 22);
                batch.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setProjectionMatrix(camera.combined);
                shapeRenderer.setColor(1, 0, 0, 1);
                shapeRenderer.circle(width / 2, height / 2 - 250, 200);
                shapeRenderer.end();
                if (Gdx.input.justTouched()) {
                    initialPress.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                    camera.unproject(initialPress);
                    float distance = (float) Math.sqrt(Math.pow(initialPress.x - width / 2, 2) + Math.pow(initialPress.y - (height / 2 - 250), 2));
                    if (distance < 200) {
                        currentScore = 3000;
                        highScore = 3000;
                        runTime = 0;
                        totalScored = 0;
                        Protons = 0;
                    }
                }
            }

	}

    private void addNewCircles() {
        for (Circles circle : addToCircles) {
            allCircles.add(circle);
        }
        addToCircles.clear();
    }

    private void moveCircles(float deltaTime) {
        for(Circles circle : allCircles){
            if(circle.moving){
                circle.move(deltaTime);
            }else{
                circle.applyAttraction(deltaTime);
            }
        }
    }

    private void drawConnection() {
        if(stillTouched){
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.setColor(1,1,0,.3f);
            shapeRenderer.line(initialPress.x,initialPress.y,lastPress.x,lastPress.y);
            shapeRenderer.end();
        }
    }

    private void tick(float deltaTime) {
        ArrayList<Circles> killList = new ArrayList<Circles>();
        for(Circles circle : allCircles){
            circle.tick(deltaTime);
            if(circle.kill){
                killList.add(circle);
            }
        }
        for(Circles circle : killList){
            allCircles.remove(circle);
        }
        killList.clear();
        if(currentScore > highScore){
            highScore = currentScore;
            if(highScore > highestScore){
                highestScore = highScore;
            }
        }
        currentScore = (long)(currentScore - (150 + (totalScored*.01f)) * deltaTime);
    }

    private void drawCircles(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
        for (Circles circle : allCircles) {
            circle.renderCircle(shapeRenderer);
        }
        shapeRenderer.end();
    }

    private void newClick(float deltaTime){
        if(Gdx.input.isTouched()){
            touchedFor = touchedFor + deltaTime;
            if(Gdx.input.justTouched()){
                touchedFor = 0f;
                stillTouched = true;
                initialPress.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(initialPress);
            }
            lastPress.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(lastPress);
        }else{
            if(stillTouched){
                stillTouched = false;
                if(touchedOpenSpace(lastPress) && touchedFor < TOUCH_TIME_OUT) {
                    allCircles.add(new Proton(this,lastPress.x, lastPress.y));
                    currentScore = currentScore - 150;
                    Protons++;
                }else{
                    fuzePoints(initialPress,lastPress);
                }
                touchedFor = 0;
            }
        }
    }

    private void fuzePoints(Vector3 initialPress, Vector3 lastPress) {
        Circles initialCircle = inWhatCircle(initialPress);
        Circles lastCircle = inWhatCircle(lastPress);
        if(initialCircle != null && lastCircle != null && !initialCircle.equals(lastCircle)){
            if(initialCircle.getClass().equals(Proton.class) && lastCircle.getClass().equals(Proton.class)){
                //initialCircle.kill = true;
                //lastCircle.kill = true;
                initialCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, initialCircle);
                //allCircles.add(new Deuterium(this,(initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f));
                currentScore = currentScore + 400;
                totalScored = totalScored + 400;
            }
            if((initialCircle.getClass().equals(Proton.class) && lastCircle.getClass().equals(Deuterium.class))||(initialCircle.getClass().equals(Deuterium.class) && lastCircle.getClass().equals(Proton.class))){
                //initialCircle.kill = true;
                //lastCircle.kill = true;
                initialCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, initialCircle);
                //allCircles.add(new Helion(this,(initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f));
                currentScore = currentScore + 500;
                totalScored = totalScored + 500;
            }
            if(initialCircle.getClass().equals(Helion.class) && lastCircle.getClass().equals(Helion.class)){
                //initialCircle.kill = true;
                //lastCircle.kill = true;
                initialCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, initialCircle);
                //allCircles.add(new Helium(this,(initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f));
                //allCircles.add(new Proton(this,initialPress.x,initialPress.y));
                //allCircles.add(new Proton(this,lastPress.x,lastPress.y));
                currentScore = currentScore + 750;
                totalScored = totalScored + 750;
            }
            if(initialCircle.getClass().equals(Helium.class) && lastCircle.getClass().equals(Helium.class)){
                //initialCircle.kill = true;
                //lastCircle.kill = true;
                initialCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, initialCircle);
                //allCircles.add(new Carbon(this,(initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f));
                currentScore = currentScore + 1000;
                totalScored = totalScored + 1000;
            }
        }
    }
/*
    private void fuzePoints1(Vector3 initialPress, Vector3 lastPress) {
        Circles initialCircle = inWhatCircle(initialPress);
        Circles lastCircle = inWhatCircle(lastPress);
        if(initialCircle != null && lastCircle != null && !initialCircle.equals(lastCircle)){
            if(initialCircle.getClass().equals(Proton.class) && lastCircle.getClass().equals(Proton.class)){
                initialCircle.kill = true;
                lastCircle.kill = true;
                allCircles.add(new Deuterium((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f));
                currentScore = currentScore + 400;
            }
            if((initialCircle.getClass().equals(Proton.class) && lastCircle.getClass().equals(Deuterium.class))||(initialCircle.getClass().equals(Deuterium.class) && lastCircle.getClass().equals(Proton.class))){
                initialCircle.kill = true;
                lastCircle.kill = true;
                allCircles.add(new Helion((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f));
                currentScore = currentScore + 500;
            }
            if(initialCircle.getClass().equals(Helion.class) && lastCircle.getClass().equals(Helion.class)){
                initialCircle.kill = true;
                lastCircle.kill = true;
                allCircles.add(new Helium((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f));
                allCircles.add(new Proton(initialPress.x,initialPress.y));
                allCircles.add(new Proton(lastPress.x,lastPress.y));
                currentScore = currentScore + 750;
            }
            if(initialCircle.getClass().equals(Helium.class) && lastCircle.getClass().equals(Helium.class)){
                initialCircle.kill = true;
                lastCircle.kill = true;
                allCircles.add(new Carbon((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f));
                currentScore = currentScore + 1000;
            }
        }
    }
*/
    private boolean touchedOpenSpace(Vector3 pos){
        float distance = 0;
        for(Circles circle: allCircles){
            distance = (float)Math.sqrt(Math.pow(pos.x - circle.getPosition().x,2)+Math.pow(pos.y - circle.getPosition().y,2));
            if(distance < (Proton.RADIUS + circle.getRadius())){
                return false;
            }
        }
        return true;
    }

    private Circles inWhatCircle(Vector3 pos){
        float distance = 0;
        Circles tempCircle = null;
        for(Circles circle: allCircles){
            distance = (float)Math.sqrt(Math.pow(pos.x - circle.getPosition().x,2)+Math.pow(pos.y - circle.getPosition().y,2));
            if(distance < circle.getRadius()){
                tempCircle = circle;// Using the tempCircle makes us return the top most rendered circle, instead of the bottom most.
            }
        }
        return tempCircle;
    }

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

}
