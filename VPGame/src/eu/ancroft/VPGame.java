package eu.ancroft;

import com.badlogic.gdx.Game;


public class VPGame extends Game {
	
	Splash splash;
	GamePlay gamePlay;
	EndGame endGame;
	

	@Override
	public void create() {
		splash = new Splash(this);
		gamePlay=new GamePlay(this);
		endGame=new EndGame(this);
		
		setScreen(splash);
		
		// TODO Auto-generated method stub
		
	}
}
