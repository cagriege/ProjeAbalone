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
import java.util.List;

public class HareketSonuc {    //hareket sonuçları eğer taş yenildiyse at yenilmediyse diğer hareketler
	boolean başarılı;
	List<Integer> ilerledi;
	boolean atildi;
	
	HareketSonuc(boolean başarılı) {
		this.başarılı = başarılı;
	}
	
	HareketSonuc(boolean başarılı, boolean atildi) {
		this.başarılı = başarılı;
		this.atildi = atildi;
	}
	
	HareketSonuc(boolean başarılı, boolean atildi, List<Integer> ilerledi) {
		this.başarılı = başarılı;
		this.ilerledi = ilerledi;
		this.atildi = atildi;
	}
}
