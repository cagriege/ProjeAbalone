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
public enum Oyuncu {
	beyaz(Color.white),
	siyah(Color.black);
	
	Color color;
	
	Oyuncu(Color color) {
		this.color = color;
	}
}
