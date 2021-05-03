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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Oyun {

    public static final int POZİSYONLAR = 61;

    private int suankiHover;
    private final List<Integer> secilenPozisyon = new ArrayList<Integer>();
    private List<Integer> hareketetmisPozisyon = new ArrayList<Integer>();
    private final Pano pano;

    private final Arayüz arayüz;

    private final Oyuncu ben;
    private final Oyuncu düsman;

    private int beniAt;
    private int digeroyuncuAt;

    private final Oyuntuslari oyuntuslari;

    private Client client;

    Oyun(Client client, Oyuncu oyuncu) {
        client.setOyun(this);
        new Thread(client).start();
        this.client = client;

        oyuntuslari = new Oyuntuslari(this);
        arayüz = new Arayüz(oyuntuslari, oyuncu, this);
        pano = new Pano();

        initPozisyon();

        ben = oyuncu;
        düsman = ben == Oyuncu.beyaz ? Oyuncu.siyah : Oyuncu.beyaz;

        initHover();
    }

    private void initPozisyon() {
        Oyuncu[] initDurum = new Oyuncu[POZİSYONLAR];
        for (int i = 0; i < 11; i++) {
            initDurum[i] = Oyuncu.beyaz;
        }
        for (int i = 13; i < 16; i++) {
            initDurum[i] = Oyuncu.beyaz;
        }
        for (int i = 45; i < 48; i++) {
            initDurum[i] = Oyuncu.siyah;
        }
        for (int i = 50; i < 61; i++) {
            initDurum[i] = Oyuncu.siyah;
        }
        pano.setDurum(initDurum);
        anlıkPanoDurum();
    }

    private void initHover() {
        suankiHover = ben == Oyuncu.beyaz ? 0 : 60;
    }

    boolean validateCokluSecim(int arrayPos) {
        //coklu secim yapıldımı
        return true;
    }

    void HareketEt(İlerleme ilerleme) {
        durdurHareket();
        if (secilenPozisyon.isEmpty()) {
            moveHover(ilerleme);
        } else {
            HareketSonuc hareketSonuc = move(secilenPozisyon, ilerleme);
            if (hareketSonuc.başarılı) {
                setInputEnabled(false);
                secilenPozisyon.clear();
                sendOyunDurum(hareketSonuc.atildi, hareketSonuc.ilerledi);
                anlıkPanoDurum();
            }
        }
    }

    private HareketSonuc move(List<Integer> secili, İlerleme ilerleme) {
        boolean başarılı = false;
        List<Integer> ilerledi = new ArrayList<Integer>();
        boolean atildi = false;

        Oyuncu team = pano.getOyuncu(secili.get(0));
        if (team == ben) {

            if (secili.size() == 1) {
                int secim = secili.get(0);
                List<Integer> line = PanoUtils.getStraightLine(secim, ilerleme); //panonun sınırına kadar olan yön çizgisi alma
                if (line.size() > 1) { //mevcut pozisyon pano sınırları içinde degilse
                    Oyuncu sıradakitasTeam = pano.getOyuncu(line.get(1));

                    if (sıradakitasTeam == null) { //hedef pozisyon boş ise ilerle
                        pano.setTeam(ben, line.get(1));
                        pano.setTeam(null, secim);
                        başarılı = true;

                    } else if (sıradakitasTeam == ben) { 
                        int kendiSay = 0;
                        while (pano.getOyuncu(line.get(kendiSay)) == ben) { //yanımda tuttugum tas dahil kactane benim tasımdan var
                            kendiSay++;
                            if (line.size() == kendiSay) {
                                return new HareketSonuc(false, false); //tuttugum tasın etrafında kendi taslarım varsa
                            }
                        }
                        boolean düsmanTas = pano.getOyuncu(line.get(kendiSay)) == düsman; //yakınlarda düsman bir tas varmı
                        int toplamSay = kendiSay;
                        if (düsmanTas) {
                            while (toplamSay < line.size() && pano.getOyuncu(line.get(toplamSay)) == düsman) { //kendi taslarım ve düsman tasları kactane ardaşık var
                                toplamSay++;
                            }
                        }
                        int enemyCount = toplamSay - kendiSay; //ne kadar düsman tası var
                        
                        if (!düsmanTas || ((kendiSay > enemyCount && kendiSay < 4)
                                && (toplamSay < line.size() ? !(pano.getOyuncu(line.get(toplamSay)) == ben) : true))) {
                            başarılı = true;

                            pano.setTeam(null, secim);
                            int pozisyon;
                            for (int i = 0; i < kendiSay; i++) { 
                                pozisyon = line.get(i + 1);
                                pano.setTeam(ben, pozisyon);
                                ilerledi.add(pozisyon);
                            }
                            if (düsmanTas) {
                                for (int i = kendiSay; i < toplamSay - 1; i++) {
                                    pozisyon = line.get(i + 1);
                                    pano.setTeam(düsman, pozisyon);
                                    ilerledi.add(pozisyon);
                                }
                                if (toplamSay == line.size()) { //hat yani line doluysa düsman tasını it
                                    KICK(düsman);
                                    atildi = true;
                                } else {
                                    pano.setTeam(düsman, line.get(toplamSay)); //son rakip parcayı itme
                                }
                            }
                        }
                    }
                }
            } else {

            }
        }
        return new HareketSonuc(başarılı, atildi, ilerledi);
    }

    private void sendOyunDurum(boolean atildi, List<Integer> ilerledi) {
        client.sendOyunDurum(ilerledi, pano, atildi);
    }

    void oyundurumAL(Oyuncu[] oyunDurum, List<Integer> ilerledi, boolean atildi) {
        setYeniPanoDurum(oyunDurum);
        showgeldigiPozisyon(ilerledi);
        if (atildi) {
            KICK(ben);
        }
        setInputEnabled(true);
    }

    private void showgeldigiPozisyon(List<Integer> ilerledi) {
        hareketetmisPozisyon = ilerledi;
        for (int i : ilerledi) {
            arayüz.drawPozisyonDurum(i, pano.getOyuncu(i), false, false, true);
        }
        arayüz.flushBoard();
    }

    public void durdurHareket() {
        if (!hareketetmisPozisyon.isEmpty()) {
            for (int i : hareketetmisPozisyon) {
                arayüz.drawPozisyonDurum(i, pano.getOyuncu(i), false, false, false);
            }
            hareketetmisPozisyon.clear();
            arayüz.flushBoard();
        }
    }

    private void KICK(Oyuncu oyuncu) {
        if (oyuncu == ben) {
            beniAt++;
        } else {
            digeroyuncuAt++;
        }
        arayüz.showAtilan(oyuncu);
        if (beniAt == 6) {
            arayüz.showKazanan(düsman.toString());
            setInputEnabled(false);
        } else if (digeroyuncuAt == 6) {
            arayüz.showKazanan(ben.toString());
            setInputEnabled(false);
        }
    }

    void setInputEnabled(boolean inputEnabled) {
        oyuntuslari.setInputEnabled(inputEnabled);
    }

    void setYeniPanoDurum(Oyuncu[] panoDurum) {
        pano.setDurum(panoDurum);
        anlıkPanoDurum();
    }

    void tekliSecimİslemi() {
        durdurHareket();
        if (secilenPozisyon.isEmpty()) {
            addSecim();
        } else {
            temizleSecim();
        }
    }

    private void addSecim() {
        if (pano.getOyuncu(suankiHover) == ben) {
            secilenPozisyon.add(suankiHover);
            arayüz.drawPozisyonDurum(suankiHover, ben, true, false, false);
        }
    }

    private void addSecim(İlerleme ilerleme) {
        System.out.println("Game.addSecim() SS");
    }

    private void temizleSecim() {
        for (int i = 0; i < secilenPozisyon.size(); i++) {
            arayüz.drawPozisyonDurum(secilenPozisyon.get(i), ben, false, false, false);
        }
        arayüz.flushBoard();
        secilenPozisyon.clear();
    }

    void cokluSecimİslemi(İlerleme ilerleme) {
        durdurHareket();
        addSecim(ilerleme);
    }

    private void moveHover(İlerleme ilerleme) {
        int newHovered = PanoUtils.getKomsu(suankiHover, ilerleme);
        if (newHovered != -1) {
            arayüz.drawPozisyonDurum(suankiHover, pano.getOyuncu(suankiHover), false, false, false);
            arayüz.drawPozisyonDurum(newHovered, pano.getOyuncu(newHovered), false, true, false);
            arayüz.flushBoard();
            suankiHover = newHovered;
        }
    }

    private void anlıkPanoDurum() {
        arayüz.temizlePano();
        for (int i = 0; i < POZİSYONLAR; i++) {
            arayüz.drawPozisyonDurum(i, pano.getOyuncu(i), false, false, false);
        }
        arayüz.drawPozisyonDurum(suankiHover, pano.getOyuncu(suankiHover), false, true, false);
        arayüz.flushBoard();
    }

}
