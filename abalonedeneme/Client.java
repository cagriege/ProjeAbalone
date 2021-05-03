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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Client implements Runnable {

    private Oyun oyun;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    static final String NONE = "0";
    static final String SİYAH = "1";
    static final String BEYAZ = "2";
    static final String OYUNCU_TEKLİF = "pr";
    static final String SET_OYUNCU = "pl";
    static final String AT = "k";
    static final String OYUN_DURUM = "g";

    Client(String serverIp) {
        try {
            socket = new Socket(serverIp, 4445);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setOyun(Oyun oyun) {
        this.oyun = oyun;
    }

    public void run() {
        try {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String Serverdanal;

            while ((Serverdanal = in.readLine()) != null) {
                if (Serverdanal.substring(0, 1).equals(OYUN_DURUM)) {
                    oyundurumAL(Serverdanal);
                } else {
                    System.out.println("Client.run() hata " + Serverdanal);
                }
            }
            System.out.println("client kapandı");
            out.close();
            in.close();
            stdIn.close();
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("localhost yok.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Localhost IO baglantısı yok.");
            System.exit(1);
        }
    }

    private void send(String message) {
        out.println(message);
    }

    private Oyuncu[] deserializeBoard(String serializedBoard) {
        String[] parts = serializedBoard.split(" ");
        if (parts.length != 61) {
            throw new RuntimeException("hatalı pano uzunlugu: " + parts.length);
        }
        Oyuncu[] pano = new Oyuncu[61];
        Oyuncu team;
        for (int i = 0; i < 61; i++) {
            if (parts[i].equals(NONE)) {
                team = null;
            } else if (parts[i].equals(SİYAH)) {
                team = Oyuncu.siyah;
            } else {
                team = Oyuncu.beyaz;
            }
            pano[i] = team;
        }
        return pano;
    }

    /**
     * iki client bağlanan kadar bekle
     */
    Oyuncu getOyuncu(Oyuncu oyuncuTeklif) {
        String oyuncuTeklifString = getProtokol(oyuncuTeklif);
        send(OYUNCU_TEKLİF + " " + oyuncuTeklifString);

        Oyuncu oyuncu = null;
        String Serverdanal;
        try {
            while ((Serverdanal = in.readLine()) != null) {
                if (Serverdanal.length() > 1 && Serverdanal.substring(0, 2).equals(SET_OYUNCU)) {
                    String playerString = Serverdanal.substring(3, 4);
                    oyuncu = getOyuncu(playerString);
                    break;
                } else {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (oyuncu == null) {
            throw new RuntimeException("Client.getOyuncu() oyuncu is null");
        }
        return oyuncu;
    }

    void sendOyunDurum(List<Integer> ilerledi, Pano pano, boolean atildi) {
        String serialized = OYUN_DURUM + " " + ilerledi.size() + " ";
        for (int i : ilerledi) {
            serialized += i + " ";
        }
        if (atildi) {
            serialized += AT + " ";
        }
        send(serialized + pano.serialize());
    }

    private void oyundurumAL(String mesaj) {
        List<Integer> hareketetmisPozisyon = new ArrayList<Integer>();

        int movedPositionsCount = Integer.parseInt(mesaj.substring(2, 3));
        StringTokenizer st = new StringTokenizer(mesaj.substring(4));
        int offset = 4;
        String token;
        for (int i = 0; i < movedPositionsCount; i++) {
            token = st.nextToken();
            hareketetmisPozisyon.add(Integer.parseInt(token));
            offset += token.length() + 1;
        }

        Oyuncu[] oyunDurum;
        boolean atildi = false;
        if (mesaj.substring(offset, offset + 1).equals(AT)) {
            atildi = true;
            oyunDurum = deserializeBoard(mesaj.substring(offset + 2));
        } else {
            oyunDurum = deserializeBoard(mesaj.substring(offset));
        }
        oyun.oyundurumAL(oyunDurum, hareketetmisPozisyon, atildi);
    }

    static String getProtokol(Oyuncu oyuncu) {
        if (oyuncu == null) {
            return NONE;
        }
        switch (oyuncu) {
            case beyaz:
                return BEYAZ;
            case siyah:
                return SİYAH;
            default:
                return null;
        }
    }

    static Oyuncu getOyuncu(String protokol) {
        if (protokol.equals(SİYAH)) {
            return Oyuncu.siyah;
        } else if (protokol.equals(BEYAZ)) {
            return Oyuncu.beyaz;
        }
        return null;
    }

}
