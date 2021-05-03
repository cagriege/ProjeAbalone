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
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;


public class Arayüz extends JFrame {

	private PanoPanel boardPanel;
	private PanelBilgi infoPanel;
	private BeklemeEkran beklemeDialog;
	
	Arayüz(KeyListener keyListener, Oyuncu oyuncu, final Oyun oyun) { 
		setTitle("Abalone");
		
		beklemeDialog = new BeklemeEkran(this);
		boardPanel = new PanoPanel(keyListener);
		infoPanel = new PanelBilgi(oyuncu);
		add(boardPanel);
		add(infoPanel, BorderLayout.EAST);
		pack();
		setVisible(true);
	}
	
	void showAtilan(Oyuncu oyuncu) {
		infoPanel.showAtilan(oyuncu);
	}

	void showKazanan(String kazanan) {
		infoPanel.showKazanan(kazanan);
	}
	
	void drawPozisyonDurum(int panoPozisyon, Oyuncu team, boolean secili, boolean üstünde, boolean karsihamleüstünde) {
		boardPanel.drawPozisyonDurum(panoPozisyon, team, secili, üstünde, karsihamleüstünde);
	}

	void temizlePano() {
		boardPanel.temizlePano();
	}

	void flushBoard() {
		boardPanel.flush();
	}

	
	void showMessagePane(String mesaj) {
		JOptionPane.showMessageDialog(this, mesaj);
	}
	
	public void setbeklemeDialogGörünüm(boolean visible) {
		beklemeDialog.setLocationRelativeTo(this);
		beklemeDialog.setVisible(visible);
	}
}