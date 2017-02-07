package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {

	SpriteBatch batch;
    Texture img;
    OrthographicCamera camera;
    ArrayList<Circles> allCircles;
    private int width = 1920;
    private int height = 1080;
    ShapeRenderer shapeRenderer;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        camera = new OrthographicCamera(width, height);
        camera.position.x=width/2;
        camera.position.y = height/2;
        camera.update();
        img = new Texture("Hex-grid.png");
        allCircles = new ArrayList<Circles>();
        shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render () {
        float deltaTime = Gdx.graphics.getDeltaTime();
        tick(deltaTime);
        checkClick();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
        drawCircles();

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
    }

    private void drawCircles(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
        for (Circles circle : allCircles) {
            setCircleColor(circle.getColorInt());
            shapeRenderer.circle(circle.getPosition().x, circle.getPosition().y, circle.getRadius());
        }
        shapeRenderer.end();
    }

    private void setCircleColor(float[] type){
        /*
        switch(type) {
            case 1:
                shapeRenderer.setColor(0, 1, 0, 1);
                break;
            case 2:
                shapeRenderer.setColor(1, 1, 0, 1);
                break;
            default:
                shapeRenderer.setColor(0, 1, 1, 1);
                break;
        }
        */
        shapeRenderer.setColor(type[0], type[1], type[2], type[3]);
    }

    private void checkClick(){
        if (Gdx.input.justTouched()){
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if(touchedOpenSpace(touchPos)) {
                allCircles.add(new Proton(touchPos.x, touchPos.y));
            }else{

            }
        }
    }

    private boolean touchedOpenSpace(Vector3 pos){
        float distance = 0;
        for(Circles circle: allCircles){
            distance = (float)Math.sqrt(Math.pow(pos.x - circle.getPosition().x,2)+Math.pow(pos.y - circle.getPosition().y,2));
            if(distance < (14 + circle.getRadius())){
                return false;
            }
        }
        return true;
    }
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

}
