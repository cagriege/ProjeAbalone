/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abalonedeneme;

/**
 *
 * @author cagri
 */
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class PanoUtils {

	private final static int[] satirUzunluk = new int[] {5, 6, 7, 8, 9, 8, 7, 6, 5};
	private final static int[] satirToplam = new int[10];
	
	static {
		satirToplam[0] = 0;
		for (int i = 0; i < satirUzunluk.length; i++) {
			satirToplam[i + 1] = satirToplam[i] + satirUzunluk[i];
		}
	}
	static List<Integer> getKomsular(int arrayPos) {
		
		List<Integer> komsular = new ArrayList<Integer>();
		Point koord = getCoords(arrayPos);

		int art = -1;
		int düs = 0;
		if (koord.y >= 4) {
			düs = -1;
			if (koord.y > 4) {
				art = 0;
			}
		}
		int komsuX = koord.x + art;
		int komsuY = koord.y - 1;
		komsular.add(getArrayPos(komsuX, komsuY));
		
		komsuX = koord.x + art + 1;
		komsular.add(getArrayPos(komsuX, komsuY));
		
		komsuX = koord.x - 1;
		komsuY = koord.y;
		komsular.add(getArrayPos(komsuX, komsuY));
		
		komsuX = koord.x + 1;
		komsular.add(getArrayPos(komsuX, komsuY));
		
		komsuX = koord.x + düs;
		komsuY = koord.y + 1;
		komsular.add(getArrayPos(komsuX, komsuY));
		
		komsuX = koord.x + düs + 1;
		komsular.add(getArrayPos(komsuX, komsuY));
		
		return komsular;
	}
	
	static int getKomsu(int arrayPos, İlerleme ilerleme) {

		Point koord = getCoords(arrayPos);
		
		int art = -1;
		int düs = 0;
		if (koord.y >= 4) {
			düs = -1;
			if (koord.y > 4) {
				art = 0;
			}
		}
		
		int komsuX = -1;
		int komsuY = -1;
		
		switch (ilerleme) {
			case nw: 
				komsuX = koord.x + art;
				komsuY = koord.y - 1;
				break;
			case ne:
				komsuX = koord.x + art + 1;
				komsuY = koord.y - 1;
				break;
			case w:
				komsuX = koord.x - 1;
				komsuY = koord.y;
				break;
			case e:
				komsuX = koord.x + 1;
				komsuY = koord.y;
				break;
			case sw:
				komsuX = koord.x + düs;
				komsuY = koord.y + 1;
				break;
			case se:
				komsuX = koord.x + düs + 1;
				komsuY = koord.y + 1;
				break;
			}
		if (isValidPos(komsuX, komsuY)) { 
			return getArrayPos(komsuX, komsuY);
		}
		return -1;
	}
	
	static Point getCoords(int arrayPos) {
		Point koord = new Point();
		if (arrayPos < satirToplam[1]) {
			koord.y = 0;
		} else if (arrayPos < satirToplam[2]) {
			koord.y = 1;
		} else if (arrayPos < satirToplam[3]) {
			koord.y = 2;
		} else if (arrayPos < satirToplam[4]) {
			koord.y = 3;
		} else if (arrayPos < satirToplam[5]) {
			koord.y = 4;
		} else if (arrayPos < satirToplam[6]) {
			koord.y = 5;
		} else if (arrayPos < satirToplam[7]) {
			koord.y = 6;
		} else if (arrayPos < satirToplam[8]) {
			koord.y = 7;
		} else { //<= 60
			koord.y = 8;
		}
		koord.x = arrayPos - satirToplam[koord.y];
		return koord;
	}
	
	static int getArrayPos(int x, int y) {
		int rowsPart = satirToplam[y];
		return rowsPart + x;
	}
	
	static List<Integer> getStraightLine(int arrayPos, İlerleme ilerleme) {
		List<Integer> line = new ArrayList<Integer>();
		line.add(arrayPos);
		int komsu = getKomsu(arrayPos, ilerleme);
		while (komsu != -1) {
			line.add(komsu);
			komsu = getKomsu(komsu, ilerleme);
		}
		return line;
	}
	
	static boolean isValidPos(int x, int y) {
		if (x < 0 || y < 0 || y > 8 || x >= satirUzunluk[y]) {
			return false;
		}
		return true;
	}
}
