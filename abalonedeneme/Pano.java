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
public class Pano {

    private Oyuncu[] durum = new Oyuncu[Oyun.POZİSYONLAR];

    void setTeam(Oyuncu team, int pozisyon) {
        durum[pozisyon] = team;
    }

    Oyuncu getOyuncu(int pozisyon) {
        return durum[pozisyon];
    }

    void setDurum(Oyuncu[] durum) {
        this.durum = durum;
    }

    String serialize() {
        String serialized = "";
        String field;
        for (Oyuncu oyuncu : durum) {
            field = Client.getProtokol(oyuncu);
            serialized += field + " ";
        }
        return serialized;
    }

}
