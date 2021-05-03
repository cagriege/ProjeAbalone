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
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;


public class PanelBilgi extends JPanel {

	private AtilantasPanel atilantasPanel;
	private static final int WIDTH = 125;
	private static final Color Arkaplan_Renk = Color.black; 
	private JLabel kazananLabel;
	
	PanelBilgi(Oyuncu oyuncu) {
		setBackground(Arkaplan_Renk);
		Dimension panelBilgiDimension = new Dimension(WIDTH, PanoPanel.YAN);
		setPreferredSize(panelBilgiDimension);
		BoxLayout boxLayout = new BoxLayout(this, javax.swing.BoxLayout.Y_AXIS);
		setLayout(boxLayout);
		
		Border line = BorderFactory.createMatteBorder(0, 2, 0, 0, new java.awt.Color(100, 100, 100));
		setBorder(line);
		
		JPanel oyuncuPanel = new JPanel();
		oyuncuPanel.setLayout(new FlowLayout());
		oyuncuPanel.setBackground(Color.gray);
		Dimension oyuncuPanelDimension = new Dimension(125, 35);
		oyuncuPanel.setSize(oyuncuPanelDimension);
		oyuncuPanel.setPreferredSize(oyuncuPanelDimension);
		oyuncuPanel.setMaximumSize(oyuncuPanelDimension);
		oyuncuPanel.setMinimumSize(oyuncuPanelDimension);
		
		ImageIcon oyuncuResim;
		if (oyuncu == Oyuncu.beyaz) {
			 oyuncuResim = new ImageIcon(getClass().getClassLoader().getResource("img/beyaz1.png"));
		} else {
			 oyuncuResim = new ImageIcon(getClass().getClassLoader().getResource("img/siyah1.png"));
		}
		oyuncuPanel.add(new JLabel("Oyuncu: "));
		oyuncuPanel.add(new JLabel(oyuncuResim));
		
		atilantasPanel = new AtilantasPanel();
		
		kazananLabel = new JLabel();
		kazananLabel.setForeground(Color.white);
		kazananLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		add(oyuncuPanel);
		add(atilantasPanel);
		add(kazananLabel);
	}
	
	
	void showAtilan(Oyuncu oyuncu) {
		atilantasPanel.showAtilan(oyuncu);
	}

	void showKazanan(String team) {
		kazananLabel.setText(team + " Kazandı!");
		repaint();
		revalidate();
	}
}
