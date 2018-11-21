package faife.learn.fractals;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class Fractals extends GestureDetector.GestureAdapter implements ApplicationListener {
	private static final String TAG = Fractals.class.getName();

	private FractalRenderer fRenderer;
	private Vector2 touchPos;
	private float touchDist;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.graphics.setContinuousRendering(false);
		fRenderer = new FractalRenderer();
		touchPos = new Vector2();
		touchDist = 1;
		Gdx.input.setInputProcessor(new GestureDetector(this));
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if(count == 1) {
			fRenderer.moveTo(x, y);
		}
		if(count == 2) {
			fRenderer.reset();
		}
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		Gdx.app.log(TAG, "pinch! \ninit1 " + initialPointer1 + "\ninit2 " + initialPointer2 + "\nend1 " + pointer1 + "\nend2 " + pointer2);
		Gdx.app.log(TAG, "pinchstart" + (initialPointer1.add(initialPointer2).scl(.5f)));
		touchPos = initialPointer1.add(initialPointer2).scl(.5f);
		touchDist = pointer1.dst(pointer2);
		return true;
	}

	@Override
	public void pinchStop() {
		fRenderer.zoom(touchPos.x, touchPos.y, touchDist);
	}

	@Override
	public boolean longPress(float x, float y) {
		fRenderer.reset();
		return true;
	}

	@Override
	public void render () {
		fRenderer.drawFractal();
	}
	
	@Override
	public void dispose () {
		fRenderer.dispose();
	}

	@Override
	public void resize(int width, int height) {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
}
