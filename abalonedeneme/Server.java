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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server extends Thread {

    private Socket[] clients = new Socket[2];
    ServerSocket serverSocket;

    //bağlanan ilk client değişkeni seçilen oyuncuya alır
    volatile private String oncesecilenOyuncu = null;

    public void run() {
        try {
            serverSocket = new ServerSocket(4445);         //baglantı icin  port
        } catch (IOException e) {
            System.err.println("Port Dinlenmiyor 4445.");
            System.exit(1);
        }
        Socket clientSocket = null;
        for (int i = 0; i < 2; i++) {
            try {
                clientSocket = serverSocket.accept();
                clients[i] = clientSocket;
                new ServerThread(i, clientSocket).start();
            } catch (IOException e) {
                System.err.println("Kabul Edilmedi");
                System.exit(1);
            }
        }
    }
    private synchronized String getOyuncu(String teklifOyuncu, int benimID) {
        String oyuncu = null;
        if (oncesecilenOyuncu == null) { //ilk client geldiyse
            if (teklifOyuncu.equals(Client.NONE)) { //ilk client seçmedi - ikinciyi bekle
                oncesecilenOyuncu = Client.NONE;
                try {

                    wait();

                    return getKarsidaki(oncesecilenOyuncu); //ikinci seçim yaptı, ilk clientın karşısına geldi
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                oncesecilenOyuncu = teklifOyuncu; //ikinci gelen aynı şeyi seçerse bu kazanır
                oyuncu = teklifOyuncu;
            }
        } else {
            if (oncesecilenOyuncu.equals(Client.NONE)) { //ilk gelen seçim yapmadı
                if (teklifOyuncu.equals(Client.NONE)) { //ikincide yapmadı - ikinci gelen rastgele client aldı birinciye bildirdi
                    oyuncu = getrastgeleOyuncu();
                    oncesecilenOyuncu = oyuncu;
                    notify();
                    return oyuncu;
                } else {
                    oncesecilenOyuncu = teklifOyuncu;
                    notify();
                    return teklifOyuncu;
                }
            } else {
                if (teklifOyuncu.equals(Client.NONE)) {
                    return getKarsidaki(oncesecilenOyuncu);
                } else {
                    if (isKarsidaki(oncesecilenOyuncu, teklifOyuncu)) {
                        return teklifOyuncu;
                    } else {
                        return getKarsidaki(oncesecilenOyuncu);
                    }
                }
            }

        }
        if (oyuncu == null) {
            System.out.println("Server.getOyuncu - oyuncu is null");
        }
        return oyuncu;
    }

    private boolean isKarsidaki(String oyuncu1, String oyuncu2) {
        return !oyuncu1.equals(oyuncu2);
    }

    private String getKarsidaki(String oyuncu) {
        if (oyuncu.equals(Client.SİYAH)) {
            return Client.BEYAZ;
        } else {
            return Client.SİYAH;
        }
    }

    private String getrastgeleOyuncu() {
        int random = new Random().nextInt(1);
        switch (random) {
            case 0:
                return Client.SİYAH;
            case 1:
                return Client.BEYAZ;
            default:
                throw new RuntimeException("SERVER getrastgeleOyuncu - hata");
        }
    }

    class ServerThread extends Thread {

        private int benimID;
        private int karsidakiID;

        ServerThread(int benimID, Socket benimClient) {
            this.benimID = benimID;
            karsidakiID = benimID == 0 ? 1 : 0;
        }

        public void run() {
            PrintWriter karsiYazdirci;
            PrintWriter yazdir;
            try {
                while (clients[0] == null || clients[1] == null) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                    }
                }

                Socket benimClient = clients[benimID];
                Socket karsiClient = clients[karsidakiID];
                yazdir = new PrintWriter(benimClient.getOutputStream(), true);
                karsiYazdirci = new PrintWriter(karsiClient.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(benimClient.getInputStream()));

                String inputLine;
                while ((inputLine = br.readLine()) != null) {

                    if (inputLine.substring(0, 1).equals(Client.OYUN_DURUM) ) {
                        karsiYazdirci.println(inputLine);

                    } else if (inputLine.length() > 1 && inputLine.substring(0, 2).equals(Client.OYUNCU_TEKLİF)) {
                        String teklifOyuncu = inputLine.substring(3, 4);
                        String oyuncu = getOyuncu(teklifOyuncu, benimID);
                        yazdir.println(Client.SET_OYUNCU + " " + oyuncu);
                    } else {
                        System.out.println("Server.ServerThread.run() - hata: " + inputLine);
                    }
                }

                br.close();
                benimClient.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
