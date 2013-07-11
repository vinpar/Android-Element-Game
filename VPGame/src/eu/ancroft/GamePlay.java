package eu.ancroft;

import java.util.Iterator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Screen;

public class GamePlay implements Screen {

	VPGame game;
	BitmapFont font;
	CharSequence str = "Hello World!";
	Texture elementSymbols;
	Texture bucketImage;
	Sound wrongSound;
	Sound rightSound;
	Music backMusic;
	SpriteBatch batch;
	OrthographicCamera camera;

	Array<vpRect> eledrops;
	long lastDropTime;
	static long startTime;
	static long endTime;
	static int score;
	int winScore = 10;
	Skin skin;
	Stage ui;

	float scaleSymbol=1f; //Scale symbols graphics by this factor
	float scaleBucket=2f; //Scale bucket graphics by this factor
	int bucketSpriteWidth=40; //graphic original is 40-px wide
	int bucketSpriteHeight=40; //graphic original is 40-px tall
	int symbolSpriteWidth = 128;//graphic original is 128-px wide
	int symbolSpriteHeight=90;//graphic original is 90-px tall
	float symbolHeight=symbolSpriteHeight*scaleSymbol;
	float symbolWidth=symbolSpriteWidth*scaleSymbol;
	Rectangle bucket;

	public GamePlay(VPGame game) {
		this.game = game;
	}

	class vpRect extends Rectangle {
		private static final long serialVersionUID = 394900531458860630L;

		private int row;
		private int col;
		public boolean correct;

		vpRect() {
		}

		void setrow(int r) {
			row = r;

		}

		void setcol(int c) {
			col = c;

		}

		int getrow() {

			return row;
		}

		int getcol() {

			return col;
		}

	}

	// this is to allow other classes to see score
	int getscore() {
		return score;
	}

	@Override
	public void render(float delta) {
	    

		// clear the screen
		Gdx.gl.glClearColor(50, 50, 50, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		font.setColor(0f, 0f, 0f, 1.0f);
		
		// Draw Stage with button
		ui.draw();
		// begin a new batch and draw the bucket and
		// all drops
		batch.begin();
        		
        		/*
        		 draw(Texture 	texture,
					float x,
					float y,
					float originX,
					float originY,
					float (new) width,
					float (new) height,
					float scaleX,
					float scaleY,
					float rotation,
					int srcX,
					int srcY,
					int srcWidth,
					int srcHeight,
					boolean flipX,
					boolean flipY) 
        	    */
					batch.draw(	bucketImage,
							(float)bucket.x,
							(float)bucket.y,
							0f,
							0f,
							(float)bucketImage.getWidth()*scaleBucket,
							(float)bucketImage.getHeight()*scaleBucket,
							1f,
							1f,
							0f,
							(int)0,
							(int)0,
							(int)bucketSpriteWidth,
							(int)bucketSpriteHeight,
							false,
							false);
        				
        		for (vpRect eledrop : eledrops) {
        		    
        		    
                           batch.draw(	elementSymbols,
                        	   	(float)eledrop.x,
                        	   	(float)eledrop.y,
                        	   	(float)0,
                        	   	(float)0,
                        	   	(float)eledrop.getWidth()*scaleSymbol,
                        	   	(float)eledrop.getHeight()*scaleSymbol,
                        	   	1f,
                        	   	1f,
                        	   	0f,
                        	   	(int)(7+(eledrop.col - 1) * symbolWidth),
                        	   	(int)((eledrop.row - 1) * symbolHeight),
                        	   	(int)symbolSpriteWidth-35,
                        	   	(int)symbolSpriteHeight,
                        	   	false,
                        	   	false );
        		}
        
        		font.draw(batch, "Number correct =".concat(String.valueOf(score)), 10,
				300);
		batch.end();

		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			bucket.x = touchPos.x-(bucketImage.getWidth()/2);
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// make sure the bucket stays within the screen bounds
		if (bucket.x < 0)
			bucket.x = 0;
		if (bucket.x > Gdx.graphics.getWidth() - bucketImage.getWidth()*scaleBucket)
			bucket.x = Gdx.graphics.getWidth() -bucketImage.getWidth()*scaleBucket;
		
		
		

		
		

		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > (300000000*scaleSymbol)) {
			spawnRaindrop();
		}


		//Gdx.app.log("300000000/scaleSymbo",String.valueOf(300000000*scaleSymbol));
		//Gdx.app.log("BW",String.valueOf(bucketImage.getWidth()));
		//Gdx.app.log("bucket.x",String.valueOf(bucket.x));

		
		
		
		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we play back
		// a sound effect as well.
		Iterator<vpRect> iter = eledrops.iterator();
		while (iter.hasNext()) {
			vpRect raindrop = iter.next();
			
   			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			
			if (raindrop.y + bucketImage.getHeight()*scaleBucket < 0)
				iter.remove();
			
			
			
			if (raindrop.overlaps(bucket)) {
			    
			    
				iter.remove();

				if (raindrop.correct) {
					rightSound.play();
					score += 1;
				}
				if (!raindrop.correct) {
					wrongSound.play();
					if (score > 0)
						score -= 1;
				}
				if (score > winScore) {
					endTime = TimeUtils.nanoTime();
					Gdx.gl.glClearColor(0, 0, 0, 1);
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					dispose();
					game.setScreen(game.endGame);

				}
				//Gdx.app.log("Time",String.valueOf((endTime - startTime) / 1000000000));

			}
			//Gdx.app.log("bucket.x",String.valueOf(bucket.x));
			//Gdx.app.log("bucket.width",String.valueOf(bucket.width));
			


			
			
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		score=0;
		startTime = TimeUtils.nanoTime();

		elementSymbols = new Texture(
				Gdx.files.internal("Element Symbols right & wrong2.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		
		
	
		

		font = new BitmapFont(Gdx.files.internal("fonts/ElementDropGame.fnt"),
				Gdx.files.internal("fonts/ElementDropGame.png"), false);

		// load the drop sound effect and the rain background "music"
		rightSound = Gdx.audio.newSound(Gdx.files.internal("Ding.wav"));
		wrongSound = Gdx.audio.newSound(Gdx.files.internal("bike-horn-1.wav"));

		backMusic = Gdx.audio.newMusic(Gdx.files
				.internal("Tom Lehrer s  The Elements  animated (SD).wav"));

		// start the playback of the background music immediately
		backMusic.setLooping(true);
		
		
		
		
		
		
		backMusic.play();

		batch = new SpriteBatch();

		// create a vpRect to logically represent the bucket
		bucket = new Rectangle();
		bucket.x = Gdx.graphics.getWidth()*.5f; // center the bucket horizontally
		bucket.y = 10;
		bucket.width=40*scaleBucket;

						
		// create the raindrops array and spawn the first raindrop
		eledrops = new Array<vpRect>();
		spawnRaindrop();
		
		
		
		//*******************************************************************
		//Setup skin for the button
		skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));
		ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(ui);

		Table table = new Table();
		ui.addActor(table);
		table.setSize(10, 10);
		table.setPosition((int)(Gdx.graphics.getWidth()*.5),(int)(Gdx.graphics.getHeight()*.9));
		table.debug();

		TextButton restartButton = new TextButton("Start Again", skin);
		TextButton biggerSymbolButton = new TextButton("Bigger Symbols", skin);
		TextButton smallerSymbolButton = new TextButton("Smaller Symbols", skin);
		TextButton biggerBucketButton = new TextButton("Bigger Bucket", skin);
		TextButton smallerBucketButton = new TextButton("Smaller Bucket", skin);

		
		
		
		restartButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
			    	dispose();
				game.setScreen(game.splash);
				return false;
			}
		});
		
		biggerSymbolButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
			    
			    scaleSymbol=(float) (scaleSymbol+.05);
			   
				return false;
			}
		});
		smallerSymbolButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
			    
			    scaleSymbol=(float) (scaleSymbol-.05);
			   
				return false;
			}
		});
		biggerBucketButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
			    
			 
			    scaleBucket=(float)(scaleBucket+.05);
				return false;
			}
		});
		smallerBucketButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
			    
			     scaleBucket=(float)(scaleBucket-.05);
				return false;
			}
		});

		table.add(restartButton);
		table.add(biggerSymbolButton);
		table.add(smallerSymbolButton);
		table.add(biggerBucketButton);
		table.add(smallerBucketButton);
		
		//**********************************************************************

		
	}

	private void spawnRaindrop() {
		vpRect raindrop = new vpRect();
		raindrop.x = MathUtils.random(0, Gdx.graphics.getWidth() - (Gdx.graphics.getWidth()/20));


		// col and row are offsets for individual element symbols in the texture
		raindrop.setcol(MathUtils.random(1, 3));
		raindrop.setrow(MathUtils.random(1, 18));

		// Correct symbols are in the first col (column) 
		if (raindrop.col == 1)
			raindrop.correct = true;

		raindrop.y = Gdx.graphics.getHeight()-(Gdx.graphics.getWidth()/20);
		raindrop.width = symbolWidth*scaleSymbol;
		raindrop.height =symbolHeight*scaleSymbol;
		eledrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		backMusic.dispose();
		elementSymbols.dispose();
		bucketImage.dispose();
		rightSound.dispose();
		wrongSound.dispose();
		backMusic.dispose();
		batch.dispose();
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
		// dispose of all the native resources


		
	}

}
