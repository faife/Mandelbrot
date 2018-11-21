package faife.learn.fractals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class FractalRenderer {
	private static final String TAG = FractalRenderer.class.getName();

	private ShaderProgram mandelbrotShader;
	private Mesh canvas;
	private Texture colorPalette;
	private float width, height;
	private float centerX, centerY;
	private float zoomFactor;
	private int maxIterations;
	private float ratio;

	public FractalRenderer() {
		setupShaders();
		setupMesh();
		colorPalette  = new Texture("colorScale.png");
		colorPalette.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		Gdx.gl20.glDisable(GL20.GL_STENCIL_TEST);
		reset();
	}

	public void drawFractal() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mandelbrotShader.begin();
		mandelbrotShader.setUniformf("zoom", zoomFactor);
		mandelbrotShader.setUniform2fv("center", new float[] {centerX, centerY}, 0, 2);
		mandelbrotShader.setUniformi("maxIterations", maxIterations);
		mandelbrotShader.setUniformf("ratio", ratio);
			canvas.render(mandelbrotShader, GL20.GL_TRIANGLE_FAN);
		mandelbrotShader.end();
	}

	private void setupMesh() {
		canvas = new Mesh(true, 4, 6,
				new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
				new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0"));
		canvas.setVertices(new float[]{
			  // x,   y,   z,     u,   v
				-1f, -1f,  0f,    0f, 1f,
				1f,  -1f,  0f,    1f, 1f,
				1f,   1f,  0f,    1f, 0f,
				-1f,  1f,  0f,    0f, 0f});
		canvas.setIndices(new short[] {0, 1, 2, 2, 3, 0});
	}

	private void setupShaders() {
		mandelbrotShader = new ShaderProgram(
				Gdx.files.internal("mandelbrot.vs").readString(),
				Gdx.files.internal("mandelbrot.fs").readString());
		if(!mandelbrotShader.isCompiled()) {
			Gdx.app.log(TAG, "Shader: " + mandelbrotShader.getLog());
		}
		mandelbrotShader.pedantic = false;
	}

	public void reset() {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		ratio = width / height;
		centerX = -.5f;
		centerY = 0;
		zoomFactor = 3f;
		maxIterations = 40;
		colorPalette.bind();
	}

	public void moveTo(float screenX, float screenY) {
		centerX += (screenX - width / 2) * zoomFactor / width;
		centerY -= (screenY - height / 2) * zoomFactor / height;
	}

	public void zoom(float screenX, float screenY, float touchDist) {
		moveTo(screenX, screenY);
		zoomFactor -= touchDist * zoomFactor / width;
		maxIterations *= 1.47f;
		Gdx.app.log(TAG, "touchDistance(complexCoord)" + (touchDist * zoomFactor / width));
	}

	public void dispose() {
		mandelbrotShader.dispose();
		canvas.dispose();
		colorPalette.dispose();
	}
}
