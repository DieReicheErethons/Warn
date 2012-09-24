package com.dre.warn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfVerwarnung {
	
	public static Set<ConfVerwarnung> ConfVerwarnungen=new HashSet<ConfVerwarnung>();
	
	int Nummer;
	List<String> BefehlListe=new ArrayList<String>();
	
	public int getNummer(){
		return Nummer;
	}
	public void setNummer(int num){
		this.Nummer=num;
	}
	
	public ConfVerwarnung(){
		ConfVerwarnungen.add(this);
	}
	
	
}
