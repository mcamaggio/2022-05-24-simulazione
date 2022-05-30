package it.polito.tdp.itunes.model;

public class Adiacenza {
	
	private Track t1;
	private Track t2;
	private int peso;
	
	public Adiacenza(Track t1, Track t2, int peso) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.peso = peso;
	}

	public Track getT1() {
		return t1;
	}

	public void setT1(Track t1) {
		this.t1 = t1;
	}

	public Track getT2() {
		return t2;
	}

	public void setT2(Track t2) {
		this.t2 = t2;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return t1.getName() +" *** "+ t2.getName() +" -> "+ this.peso ;
	}
	
	
	

}
