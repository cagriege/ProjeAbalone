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
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PanoPanel extends JPanel {

    private static final int PozisyonlarArasiBosluk = 5;
    static final int YAN = 350;
    private int ÇAP;

    //panonun gösterilmesi için ekran konumları sadece bir kerelik
    private Point[] ekranKoord = new Point[Oyun.POZİSYONLAR];

    private static final Color Arkaplan_Renk = new Color(200, 200, 250);
    private static final Color Pozisyon_Sınır_Renk = Color.black;
    private static final Color Bos_Renk = new Color(0.7f, 0.7f, 0.7f);
    private static final Color Secilmis_Renk = new Color(1f, 0.4f, 0.4f, 0.7f);
    private static final Color Üstünde_Renk = new Color(0.7f, 0.7f, 1f, 0.7f);
    private static final Color KarsininHamle_Renk = new Color(1f, 0.7f, 0.7f, 0.7f);

    private Image bosPanoResim;
    private Image oyunPanoResim;

    private Image siyahTasResim = new ImageIcon(getClass().getClassLoader().getResource("img/siyah0.png")).getImage();
    private Image beyazTasResim = new ImageIcon(getClass().getClassLoader().getResource("img/beyaz0.png")).getImage();

    PanoPanel(KeyListener keyListener) {

        initbosPanoResim();
        initoyunPanoResim();

        setFocusable(true);
        requestFocus();

        Dimension dimension = new Dimension(YAN, YAN);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        setSize(dimension);

        addKeyListener(keyListener);
    }

    private void initoyunPanoResim() {
        oyunPanoResim = createBufferedImage(YAN, YAN);
        temizlePano();
    }

    private void initbosPanoResim() {

        //en uzun satır için toplam alan
        int dokuzBosluk = PozisyonlarArasiBosluk * 10;

        //dairelerin alan hesaplaması
        ÇAP = (Math.round((float) (YAN - dokuzBosluk) / 9));

        bosPanoResim = createBufferedImage(YAN, YAN);
        Graphics boardGraphics = bosPanoResim.getGraphics();
        boardGraphics.setColor(Arkaplan_Renk);
        boardGraphics.fillRect(0, 0, YAN, YAN);
        boardGraphics.setColor(Pozisyon_Sınır_Renk);

        //her daire için x ve y koordinatı hesaplaması
        int x = PozisyonlarArasiBosluk;
        int y = PozisyonlarArasiBosluk;

        //tek satırlar için offset
        int offset = ÇAP / 2;

        //ilk satır uzunluğu
        int satırdakiDaireler = 5;
        int circleCount = 0;
        byte arttır = -1;
        byte arttırDaire = 1;
        int say1 = 2;
        int say2 = 1;

        //satırlar
        for (int j = 0; j < 9; j++) {

            if (j % 2 == 0) {
                x = say1 * ÇAP + (say1 + 1) * PozisyonlarArasiBosluk;
                say1 = say1 + arttır;
            } else {
                x = say2 * ÇAP + (say2 + 1) * PozisyonlarArasiBosluk + offset;
                say2 = say2 + arttır;
            }

            for (int i = 0; i < satırdakiDaireler; i++) {
                boardGraphics.drawOval(x, y, ÇAP, ÇAP);
                ekranKoord[circleCount] = new Point(x, y);
                circleCount++;
                x = x + ÇAP + PozisyonlarArasiBosluk;
            }

            //4.satırdaki daireler için eksiltme
            if (j == 4) {
                arttırDaire = -1;
                say1 += 2;
                say2++;
                arttır = 1;
            }
            satırdakiDaireler += arttırDaire;

            y = y + ÇAP + PozisyonlarArasiBosluk;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(oyunPanoResim, 0, 0, null);
    }

    private Image createBufferedImage(int genislik, int yükseklik) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        return gc.createCompatibleImage(genislik, yükseklik, Transparency.OPAQUE);
    }

    private Point getEkranKoord(int panoPozisyon) {
        return ekranKoord[panoPozisyon];
    }

    void drawPozisyonDurum(int panoPozisyon, Oyuncu oyuncu, boolean secili, boolean üstünde, boolean karsihamleüstünde) {
        if (secili && üstünde) {
            System.out.println("PanoPanel.drawPozisyonDurum() uyarı - seçilmiş ve mouse üstünde");
        }
        Point ekranKoord = getEkranKoord(panoPozisyon);
        Graphics g = oyunPanoResim.getGraphics();

        drawPozisyon(g, ekranKoord, oyuncu);

        if (secili) {
            g.setColor(Secilmis_Renk);
            g.fillOval(ekranKoord.x, ekranKoord.y, ÇAP, ÇAP);
        }
        if (üstünde) {
            g.setColor(Üstünde_Renk);
            g.fillOval(ekranKoord.x, ekranKoord.y, ÇAP, ÇAP);
        }
        if (karsihamleüstünde) {
            g.setColor(KarsininHamle_Renk);
            g.fillOval(ekranKoord.x, ekranKoord.y, ÇAP, ÇAP);
        }
    }

    private void drawPozisyon(Graphics g, Point koord, Oyuncu oyuncu) {
        if (oyuncu == null) {
            g.setColor(Bos_Renk);
            g.fillOval(koord.x, koord.y, ÇAP, ÇAP);
            g.setColor(Pozisyon_Sınır_Renk);
            g.drawOval(koord.x, koord.y, ÇAP, ÇAP);
        } else {
            Image tasResim;
            if (oyuncu == Oyuncu.siyah) {
                tasResim = siyahTasResim;
            } else {
                tasResim = beyazTasResim;
            }
            g.drawImage(tasResim, koord.x, koord.y, null);
        }
    }

    void flush() {
        repaint();
        revalidate();
    }

    void temizlePano() {
        Graphics g = oyunPanoResim.getGraphics();
        g.drawImage(bosPanoResim, 0, 0, null);
    }
}
