package it.polito.tdp.itunes.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		Genre g = new Genre(2, "Jazz");		
		m.creaGrafo(g);
		System.out.println(m.getDeltaMassimo());
	}

}
