package eu.ancroft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


public class EndGame implements Screen {
	VPGame game;
	SpriteBatch batch;
	BitmapFont font;
	Skin skin;
	Stage ui;

	public EndGame(VPGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		font.setColor(.0f, .0f, .2f, 1.0f);
		font.draw(
				batch,
				"Final Score ="
						.concat(String
								.valueOf((GamePlay.endTime - GamePlay.startTime) / 1000000000))
						.concat(" seconds"), 1, 300);
		batch.end();
		// Draw Stage with button
		ui.draw();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {

		batch = new SpriteBatch();

		font = new BitmapFont(
				Gdx.files.internal("fonts/ElementDropEndGame.fnt"),
				Gdx.files.internal("fonts/ElementDropEndGame.png"), false);

		skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));
		ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(ui);

		Table table = new Table();
		ui.addActor(table);
		table.setSize(700, 800);
		table.setPosition(0, 0);
		table.debug();

		TextButton button = new TextButton("PLAY AGAIN", skin);

		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
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
