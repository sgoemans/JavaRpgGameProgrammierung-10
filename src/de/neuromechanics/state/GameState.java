package de.neuromechanics.state;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import de.neuromechanics.Camera;
import de.neuromechanics.Game;
import de.neuromechanics.KeyManager;
import de.neuromechanics.Level;
import de.neuromechanics.Player;
import de.neuromechanics.SpriteSheet;
import de.neuromechanics.TileSet;

public class GameState extends State implements Serializable {
	private static final long serialVersionUID = -5205085269605782656L;
	private Player player;
	private Level level;
	private Camera gameCamera;
//	private AnimEntity fire; 
	private KeyManager keyManager;
	private SpriteSheet playerSprite;

	public GameState(Game game) {
		super(game);
		
		initLevel();
		playerSprite = initPlayerSprite();
		player = new Player(this, "Player", 320, 320, Player.PLAYER_DEFAULT_WIDTH, Player.PLAYER_DEFAULT_HEIGHT, 
				Player.DEFAULT_HEALTH, Player.DEFAULT_SPEED, playerSprite, true);
		
		//		SpriteSheet fireSprite = new SpriteSheet("/sprites/fire_big.png", 3, 1, 64, 128);
		
		gameCamera = new Camera(level.getSizeX(), level.getSizeY());

		//		fire = new AnimEntity(this, "Fire", fireSprite, 280, 280, 64, 192);

		keyManager = game.getKeyManager();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initLevel() {
		TileSet[] tileSet = new TileSet[3];
		// Boden Tileset für Layer mit blockenden Kacheln, der hinter der Spielfigur gerenderd wird
		HashSet hs = new HashSet(Arrays.asList(115, 116, 117, 127, 129, 139, 140, 141));
		tileSet[0] = new TileSet("/tiles/rpg.png", 12 /*sizeX*/, 12/*sizeY*/, 0 /*sizeZ*/, 3 /*border*/, hs);

		// Zweites Tileset fï¿½r Layer mit blockenden Kacheln, der über der Spielfigur gerendered wird
		hs = new HashSet(Arrays.asList(33, 138, 139, 145, 146, 170, 171));
		tileSet[1] = new TileSet("/tiles/tilec.png", 17 /*sizeX*/, 16 /*sizeY*/, 3 /*sizeZ*/, 0 /*border*/, hs);

		// Transparent Z / foreground layer tileset, no blocking tiles
		tileSet[2] = new TileSet("/tiles/tilec.png", 17 /*sizeX*/, 16 /*sizeY*/, 3 /*sizeZ*/, 0 /*border*/, hs);

		String[] paths = new String[3];
		paths[0] = "/level/level2.txt";
		paths[1] = "/level/level2a.txt";
		paths[2] = "/level/level2b.txt";
		level = new Level(this, paths, tileSet);
	}

	public SpriteSheet initPlayerSprite() {
		return new SpriteSheet("/sprites/player.png", 3, 4, 64, 64);
	}

	@Override
	public void update() {
		keyManager.update();
		Point p = game.getKeyManager().getInput();
		/* 
		 * p.x == -99 if no directional key was pressed but a command key like ESC. 
		 * ESC switches from gameState to menuState. 
		 */
		if(p.y == KeyEvent.VK_ESCAPE) {
			saveState();
			State.setState(game.getMenuState());
			return;
		}
		player.setMove(p);
		player.update();

		//		fire.update();
	}
	@Override
	public void render(Graphics g) {
		level.render(g);
		player.render(g);
		//		fire.render(g);
		level.renderZ(g);
	}

	public Camera getGameCamera(){
		return gameCamera;
	}


	private void saveState() {
		Player player = ((GameState) State.getState()).getPlayer();
		try {
			FileOutputStream fos = new FileOutputStream("player.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(player);
			oos.close();
			System.out.println("Player saved!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;		
	}

	public Level getLevel() {
		return level;
	}

}
