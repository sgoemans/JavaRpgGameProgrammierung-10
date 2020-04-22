package de.neuromechanics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;

import javax.imageio.ImageIO;

public class TileSet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4924349121534274904L;

	public static final int TILEWIDTH = 64, TILEHEIGHT = 64;

	private Tile[] tiles;

	@SuppressWarnings("rawtypes")
	public HashSet hs;

	public TileSet(String path, int sizeX, int sizeY, int sizeZ, int border, @SuppressWarnings("rawtypes") HashSet hs) {
		this.hs = hs;
		tiles = new Tile[sizeX * sizeY];
		BufferedImage tileSet;
		try {
			tileSet = ImageIO.read(TileSet.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		int i = 0;
		for(int y = 0; y < sizeY; y++) {
			for(int x = 0; x < sizeX; x++) {
				tiles[i] = new Tile(tileSet.getSubimage(x * (TILEWIDTH + border), y * (TILEHEIGHT + border), TILEWIDTH, TILEHEIGHT), hs.contains(i));
				i++;
			}
			for(int z = 1; z < sizeZ; z++) {
				tiles[i-1].addAnimImage(tileSet.getSubimage((sizeX - 1 + z) * (TILEWIDTH + border), y * (TILEHEIGHT + border), TILEWIDTH, TILEHEIGHT));
			}
		}
	}

	public void renderTile(Graphics g, int tileNum, int x, int y){
		g.drawImage(tiles[tileNum].getAnimImage(), x, y, TILEWIDTH, TILEHEIGHT, null);
	}
}