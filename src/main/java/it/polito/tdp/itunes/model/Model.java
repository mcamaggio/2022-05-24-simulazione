package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	
	private Graph<Track, DefaultWeightedEdge> grafo;
	private Map<Integer, Track> idMap;
	
	private List<Track> listaMigliore;
	
	// RICORSIONE DI OTTIMIZZAZIONE
	public List<Track> cercaLista(Track c, int m) {
		// RECUPERO LA COMPONENTE CONNESSA DI C
		List<Track> canzoniValide = new ArrayList<Track>();
		ConnectivityInspector<Track, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo);
		canzoniValide.addAll(ci.connectedSetOf(c)); //Tutti i vertici connessi a C in qualche modo
		
		List<Track> parziale = new ArrayList<>();
		listaMigliore = new ArrayList<>();
		parziale.add(c);
		
		cerca(parziale, canzoniValide, m);
		
		return listaMigliore;
	}
	
	private void cerca(List<Track> parziale, List<Track> canzoniValide, int m) {
		
		// CONTROLLO SOLUZIONE MIGLIORE
		if(parziale.size() > listaMigliore.size()) {
			listaMigliore = new ArrayList<>(parziale);
		}
		
		for(Track t : canzoniValide) {
			if(!parziale.contains(t) && sommaMemoria(parziale)+t.getBytes() <= m) {
				parziale.add(t);
				cerca(parziale, canzoniValide, m);
				parziale.remove(parziale.size()-1);
			}
			
			
		}
			
				
	}
	
	private int sommaMemoria(List<Track> canzoni) {
		int somma = 0;
		for(Track t : canzoni) {
			somma += t.getBytes();
		}
		return somma;
	}

	public Model() {	
		dao = new ItunesDAO();
		idMap = new HashMap<>();
		
		this.dao.getAllTracks(idMap);
	}
	
	public void creaGrafo(Genre genere) {
		// CREO IL GRAFO
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// AGGIUNGO I VERTICI
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(genere, idMap));
		
		// AGGIUNGO GLI ARCHI
		for(Adiacenza a : this.dao.getArchi(genere, idMap)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getT1(), a.getT2(), a.getPeso());
		}
		
		System.out.println("Grafo Creato!");
		System.out.println(String.format("# VERTICI: %d", this.grafo.vertexSet().size()));
		System.out.println(String.format("# ARCHI: %d", this.grafo.edgeSet().size()));
	}
	
	public List<Track> getVertici() {		
		return new ArrayList<>(this.grafo.vertexSet());
	}
	
	public List<Adiacenza> getDeltaMassimo() {
		
		List<Adiacenza> result = new ArrayList<Adiacenza>();	
		int max = 0;
		
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			int peso = (int)this.grafo.getEdgeWeight(e);
			
			if(peso > max) {
				result.clear();
				result.add(new Adiacenza(this.grafo.getEdgeSource(e), 
						this.grafo.getEdgeTarget(e), peso));
				max = peso;
			} else if(peso == max) {
				result.add(new Adiacenza(this.grafo.getEdgeSource(e), 
						this.grafo.getEdgeTarget(e), peso));
				
			}
		}
		return result;
	}

	public boolean grafoCreato() {
		if(this.grafo == null)
			return false;
		else
			return true;
	}
	
	public List<Genre> getGeneri(){
		return dao.getAllGenres();
	}

	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	
	
	
	
}
