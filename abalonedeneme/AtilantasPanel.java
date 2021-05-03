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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
public class AtilantasPanel extends JPanel {   
	
	private static final Color Arkaplan_Renk = Color.lightGray;
	
	private final Image atilantasbeyazResim = new ImageIcon(getClass().getClassLoader().getResource("img/beyaz2.png")).getImage();
	private final Image atilantassiyahResim = new ImageIcon(getClass().getClassLoader().getResource("img/siyah2.png")).getImage();
	private int beyaztasAtildi;
	private int siyahtasAtildi;
	
	private int ÇAP = 15;
	private int boslukX = 5;
	private int boslukY = 7;
	
	AtilantasPanel() {
		setBackground(Arkaplan_Renk);
		
		Dimension dimension = new Dimension(250, 50);
		setSize(dimension);
		setPreferredSize(dimension);
		setMaximumSize(dimension);
		setMinimumSize(dimension);
	}
	
	public void showAtilan(Oyuncu oyuncu) {
		if (oyuncu == Oyuncu.beyaz) {
			beyaztasAtildi++;
		} else {
			siyahtasAtildi++;
		}
		repaint();
		revalidate();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int x = boslukX;
		int y = boslukY;
		
		g.setColor(Oyuncu.beyaz.color);
		for (int i = 0; i < beyaztasAtildi; i++) {
			g.drawImage(atilantasbeyazResim, x, y, null);
			x += ÇAP + boslukX;
		}
		x = boslukX;
		y += ÇAP + boslukY;
			
		g.setColor(Oyuncu.siyah.color);
		for (int i = 0; i < siyahtasAtildi; i++) {
			g.drawImage(atilantassiyahResim, x, y, null);
			x += ÇAP + boslukX;
		}
	}

	void yenile() {
		repaint();
		revalidate();
	}
}
