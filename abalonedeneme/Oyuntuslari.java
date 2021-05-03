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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class Oyuntuslari implements KeyListener {

    private final Oyun oyun;

    private final int CokluSecimTus = KeyEvent.VK_SHIFT;
    private final int tasSecmeTus = KeyEvent.VK_SPACE;

    private Map<Integer, İlerleme> tusİlerleme = new HashMap<Integer, İlerleme>();

    private boolean inputEnabled = true;
    private boolean coklusecimtusuBasili = false;

    Oyuntuslari(Oyun oyun) {
        this.oyun = oyun;

        tusİlerleme.put(KeyEvent.VK_Q, İlerleme.nw);
        tusİlerleme.put(KeyEvent.VK_E, İlerleme.ne);
        tusİlerleme.put(KeyEvent.VK_D, İlerleme.e);
        tusİlerleme.put(KeyEvent.VK_X, İlerleme.se);
        tusİlerleme.put(KeyEvent.VK_Z, İlerleme.sw);
        tusİlerleme.put(KeyEvent.VK_A, İlerleme.w);
    }

    void processKeyEvent(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (inputEnabled) {
            int keyCode = e.getKeyCode();

            if (keyCode == CokluSecimTus) {
                coklusecimtusuBasili = true;
            } else if (keyCode == tasSecmeTus) {
                oyun.tekliSecimİslemi();
            } else if (tusİlerleme.keySet().contains(keyCode)) {
                İlerleme ilerleme = tusİlerleme.get(keyCode);
                if (coklusecimtusuBasili) {
                    oyun.cokluSecimİslemi(ilerleme);
                } else {
                    oyun.HareketEt(ilerleme);

                }
            } else if (keyCode == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == CokluSecimTus) {
            coklusecimtusuBasili = false;
        }
    }

    public void setInputEnabled(boolean inputEnabled) {
        this.inputEnabled = inputEnabled;
    }

    public void keyTyped(KeyEvent arg0) {
    }
}
