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
//Main kısım
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainEkran extends JFrame {

    private Server server;
    private BaglantiPencere baglantiPencere;

    public static void main(String[] args) {
        @SuppressWarnings("kullanılmadı")
        MainEkran connectionFrame = new MainEkran();
    }

    MainEkran() {
        setTitle("Abalone");

        JLabel resimLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("img/abalonekapak.jpg")));

        JPanel baglantiPanel = new JPanel();
        BoxLayout nextPanelLayout = new BoxLayout(baglantiPanel, javax.swing.BoxLayout.Y_AXIS);
        baglantiPanel.setLayout(nextPanelLayout);

        JPanel renkPanel = new JPanel();
        renkPanel.setLayout(new FlowLayout());
        renkPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        JLabel renkSecimLabel = new JLabel("Renk: ");

        final ImageIcon beyazResim = new ImageIcon(getClass().getClassLoader().getResource("img/beyaz1.png"));
        final ImageIcon siyahResim = new ImageIcon(getClass().getClassLoader().getResource("img/siyah1.png"));
        final ImageIcon beyazSecimResim = new ImageIcon(getClass().getClassLoader().getResource("img/beyaz3.png"));
        final ImageIcon siyahSecimResim = new ImageIcon(getClass().getClassLoader().getResource("img/siyah3.png"));

        final JLabel beyazResimLabel = new JLabel(beyazResim);
        final JLabel siyahResimLabel = new JLabel(siyahResim);

        final Oyuncu[] secilenOyuncu = new Oyuncu[1];

        beyazResimLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent arg0) {
                siyahResimLabel.setIcon(siyahResim);
                beyazResimLabel.setIcon(beyazSecimResim);
                secilenOyuncu[0] = Oyuncu.beyaz;
            }
        });
        siyahResimLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent arg0) {
                beyazResimLabel.setIcon(beyazResim);
                siyahResimLabel.setIcon(siyahSecimResim);
                secilenOyuncu[0] = Oyuncu.siyah;
            }
        });

        JPanel butonPanel = new JPanel();
        JButton serverButton = new JButton("Server Olarak Başla");
        serverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                server = new Server();
                server.start();
                Client client = new Client("localhost");
                ClientBaslat clientBaslat = new ClientBaslat(MainEkran.this, client, secilenOyuncu[0]);
                new Thread(clientBaslat).start();
                baglantiPencere = new BaglantiPencere(MainEkran.this);
                baglantiPencere.setVisible(true);
            }
        });
        JButton clientButton = new JButton("Client Olarak Başla");
        clientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String serverIp = JOptionPane.showInputDialog(MainEkran.this, "Server IP Giriniz:", "Server'a Bağlan", JOptionPane.PLAIN_MESSAGE);
                Client client = new Client(serverIp);
                setVisible(false);
                Oyuncu oyuncu = client.getOyuncu(secilenOyuncu[0]);
                @SuppressWarnings("kullanılmadı")
                Oyun oyun = new Oyun(client, oyuncu);
            }
        });

        renkPanel.add(renkSecimLabel);
        renkPanel.add(beyazResimLabel);
        renkPanel.add(siyahResimLabel);

        butonPanel.add(serverButton);
        butonPanel.add(clientButton);

        baglantiPanel.add(renkPanel);
        baglantiPanel.add(butonPanel);

        add(resimLabel, BorderLayout.NORTH);
        add(baglantiPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    void gizle() {
        setVisible(false);
        baglantiPencere.setVisible(false);
    }

}

class BaglantiPencere extends JDialog {

    private MainEkran initFrame;

    public BaglantiPencere(MainEkran initFrame) {
        super(initFrame, true);
        this.initFrame = initFrame;

        setTitle("Bağlantı");

        JPanel panel = new JPanel();
        getContentPane().add(panel);

        panel.add(new JLabel("Karşı Taraf Bekleniyor..."));

        pack();
        setLocationRelativeTo(initFrame);
    }
}

class ClientBaslat implements Runnable {

    private MainEkran initFrame;
    private Client client;
    private Oyuncu selectedPlayer;

    ClientBaslat(MainEkran initFrame, Client client, Oyuncu selectedPlayer) {
        this.initFrame = initFrame;
        this.client = client;
        this.selectedPlayer = selectedPlayer;
    }

    public void run() {
        Oyuncu oyuncu = client.getOyuncu(selectedPlayer);
        initFrame.gizle();
        @SuppressWarnings("kullanılmadı")
        Oyun oyun = new Oyun(client, oyuncu);
        System.out.println("Client Baslatması bitti");
    }
}
