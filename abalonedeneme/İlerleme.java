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
public enum İlerleme {
	nw, ne, e, se, sw, w;
	
	İlerleme ters() {
		switch(this) {
			case nw : return İlerleme.se;
			case ne : return İlerleme.sw;
			case w : return İlerleme.e;
			case e: return İlerleme.w;
			case sw: return İlerleme.ne;
			case se: return İlerleme.nw;
		}
	return null;
	}
}
