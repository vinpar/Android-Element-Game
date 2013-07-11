package eu.ancroft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Splash implements Screen {

	VPGame game;
	private SpriteBatch spriteBatch;
	private Texture splsh;
	Stage ui;
	Skin skin;
	Table table;
	OrthographicCamera camera;
	float graphX = Gdx.graphics.getWidth();
	float graphY = Gdx.graphics.getHeight();
	float touch_x, touch_y;

	public Splash(VPGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Draw Splash Screen
		spriteBatch.begin();
		spriteBatch.draw(splsh, 0, 0, graphX, graphY);
		spriteBatch.end();

		// Draw Stage with button
		ui.draw();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		camera = new OrthographicCamera();
		camera.setToOrtho(false, graphX, graphY);

		spriteBatch = new SpriteBatch();
		splsh = new Texture(
				Gdx.files.internal("Splash Screen Element Drop V2.png"));

		// Initialize skin
		skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));
		ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(ui);

		Table table = new Table();
		ui.addActor(table);
		table.setSize(50, 50);
		table.setPosition(graphX / 2, (float) (graphY*.05 ));
		table.debug();

		TextButton button = new TextButton("PLAY", skin);

		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				
				
				
				Gdx.app.log("tag", "message");
				game.setScreen(game.gamePlay);

				// System.out.println("touchDown 1");
				return false;
			}
		});

		table.add(button);
		
	
		
		
		

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
