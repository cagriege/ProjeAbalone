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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Bekleme Ekranı-karsı tarafın baglanması icin bekle
 */
public class BeklemeEkran extends JDialog {

    BeklemeEkran(JFrame owner) {
        super(owner, false);
        setTitle("Bağlantı");

        getContentPane().add(new JLabel("Karşı Taraf bekleniyor..."));
        setLocationRelativeTo(owner);
        pack();
    }
}
