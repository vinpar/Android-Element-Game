package eu.ancroft;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ElementCatch";
		cfg.useGL20 = true;
		cfg.width = 1024;
		cfg.height = 800;
		
		new LwjglApplication(new VPGame(), cfg);
	}
}
