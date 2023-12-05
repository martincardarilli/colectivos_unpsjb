package colectivo.negocio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.YenKShortestPath;
import org.jgrapht.graph.DirectedMultigraph;
import colectivo.controlador.Coordinador;
import colectivo.controlador.Constantes;
import colectivo.modelo.Linea;
import colectivo.modelo.Parada;
import colectivo.modelo.Tramo;
import colectivo.util.Time;
import net.datastructures.TreeMap;

public class Calculo implements Observer{

	private Graph<Parada, ParadaLinea> red;
	private TreeMap<String, Parada> paradaMap;
	private TreeMap<String, Tramo> tramoMap;
	private Coordinador coordinador;
	
	private Subject subject;
	private boolean actualizar;
	
	private final static Logger logger = Logger.getLogger(Calculo.class);

	public Calculo() {
		
	}
	
	public void init(Subject subject) {
		this.subject = subject;
		this.subject.attach(this);
		this.actualizar = true;
		//refresh? congestiones = coordinador.getCongestion();
	}
	
	public void cargarDatos(TreeMap<String, Parada> paradaMap, TreeMap<String, Linea> lineaMap, List<Tramo> tramos) {
		
		if (actualizar) {
		// Map paradas
		this.paradaMap = paradaMap;
		
		// Map tramo
		tramoMap = new TreeMap<String, Tramo>();
		for (Tramo t : tramos) {
			tramoMap.put(t.getInicio().getId() + "-" + t.getFin().getId(), t);
		}

		red = new DirectedMultigraph<>(null, null, false);

		// Cargar paradas
		for (Parada p : paradaMap.values()) {
			red.addVertex(p);
		}

		// Cargar tramos lineas
		Parada origen, destino;
		for (Linea l : lineaMap.values()) {		
			for (int i = 0; i < l.getParadas().size() - 1; i++) {
				origen = l.getParadas().get(i);
				destino = l.getParadas().get(i + 1);
				red.addEdge(origen, destino, new ParadaLinea(origen, l));
			}
		}

		// Cargar tramos caminando
		Linea linea;
		for (Tramo t : tramos)
			if (t.getTipo() == Constantes.TRAMO_CAMINANDO) {
				linea = new Linea(t.getInicio().getId() + "-" + t.getFin().getId(), Time.toMins("00:00"),
						Time.toMins("24:00"), 0);
				red.addEdge(t.getInicio(), t.getFin(), new ParadaLinea(t.getInicio(), linea));
			}
		
		actualizar = false;
		logger.info("Se actualizaron los datos");
		}else {
			logger.info("No se actualizaron los datos");
		}

	}
	
private Graph<Parada, ParadaLinea> grafoRecorrido(Parada paradaOrigen, Parada paradaDestino){
		
		Set<ParadaLinea> paradaLineas = new HashSet<ParadaLinea>();
		paradaLineas.addAll(red.outgoingEdgesOf(paradaOrigen));
		paradaLineas.addAll(red.incomingEdgesOf(paradaDestino));
		
		Set <Linea> lineas = new HashSet<Linea>(); 
		for (ParadaLinea p: paradaLineas)
			lineas.add(p.getLinea());
		
		Graph<Parada, ParadaLinea> recorrido = new DirectedMultigraph<>(null, null, false);

		// Cargar paradas
		for (Parada p : paradaMap.values())
			recorrido.addVertex(p);

		// Cargar tramos lineas
		Parada origen, destino;
		for (Linea l : lineas)
			for (int i = 0; i < l.getParadas().size() - 1; i++) {
				origen = l.getParadas().get(i);
				destino = l.getParadas().get(i + 1);
				recorrido.addEdge(origen, destino, new ParadaLinea(origen, l));
			}

		// Cargar tramos caminando
		Linea linea;
		for (Tramo t : tramoMap.values())
			if (t.getTipo() == Constantes.TRAMO_CAMINANDO) {
				linea = new Linea(t.getInicio().getId() + "-" + t.getFin().getId(), Time.toMins("00:00"),
						Time.toMins("24:00"), 0);
				recorrido.addEdge(t.getInicio(), t.getFin(), new ParadaLinea(t.getInicio(), linea));
			}

		

		return recorrido;
	}

/* ESTA NO USA LA VARIABLE DE CLASE RED, CREA UN GRAFO EN CADA INSTANCIA */
	public List<List<Tramo>> recorridos(Parada paradaOrigen, Parada paradaDestino, int horario, int nroLineas) {
		
		// crear el grafo
		cargarDatos(coordinador.listarParadas(),coordinador.listarLineas(),coordinador.listarTramos());

		// Crear grafo
		Graph<Parada, ParadaLinea> redConsulta = grafoRecorrido(paradaOrigen, paradaDestino);
		
		// Todos los recorridos
		YenKShortestPath<Parada, ParadaLinea> yksp = new YenKShortestPath<Parada, ParadaLinea>(redConsulta);
		List<GraphPath<Parada, ParadaLinea>> caminos = yksp.getPaths(paradaOrigen, paradaDestino, Integer.MAX_VALUE);

		// Eliminar recorridos superan cambioLineas
		List<Linea> lineas;
		Iterator<GraphPath<Parada, ParadaLinea>> r = caminos.iterator();
		while (r.hasNext()) {
			lineas = new ArrayList<Linea>();
			int cambioLineas = 0;
			for (ParadaLinea pl : r.next().getEdgeList())
				if (lineas.isEmpty())
					lineas.add(pl.getLinea());
				else if (!lineas.get(lineas.size() - 1).equals(pl.getLinea()))
					lineas.add(pl.getLinea());
			for (Linea l : lineas)
				if (l.getFrecuencia() != 0)
					cambioLineas++;
			if (cambioLineas > nroLineas)
				r.remove();
		}

		// Realizar c�lculo de tiempo y preparar resultados
		List<List<Tramo>> listaTramos = new ArrayList<List<Tramo>>();
		Tramo t = null;
		int proximoColectivo;
		int tiempo = 0;
		List<Tramo> tramos;
		List<ParadaLinea> paradalineas;
		List<Parada> paradas;
		Parada origen = null;
		Parada destino = null;
		TreeMap<String, Parada> pMap;
		for (GraphPath<Parada, ParadaLinea> gp : caminos) {
			pMap = new TreeMap<String, Parada>();
			paradas = gp.getVertexList();
			for (Parada p : paradas)
				pMap.put(p.getId(), new Parada(p.getId(), paradaMap.get(p.getId()).getDireccion(),paradaMap.get(p.getId()).getLat(), paradaMap.get(p.getId()).getLng()));
			proximoColectivo = horario;
			tramos = new ArrayList<Tramo>();
			paradalineas = gp.getEdgeList();
			for (int i = 0; i < paradalineas.size(); i++) {
				t = tramoMap.get(paradas.get(i).getId() + "-" + paradas.get(i + 1).getId());
				origen = pMap.get(paradas.get(i).getId());
				origen.agregarLinea(paradalineas.get(i).getLinea());
				destino = pMap.get(paradas.get(i + 1).getId());
				proximoColectivo = proximoColectivo(paradalineas.get(i).getLinea(), paradas.get(i),
						proximoColectivo + tiempo);
				tramos.add(new Tramo(origen, destino, t.getTipo(), proximoColectivo));
				tiempo = t.getTiempo();
			}
			destino.agregarLinea(origen.getLineas().get(0));
			tramos.add(new Tramo(destino, destino, t.getTipo(), proximoColectivo + t.getTiempo()));
			listaTramos.add(tramos);
		}
		return listaTramos;
	}

	
	
	/* ESTA INTENTA USA LA VARIABLE DE CLASE RED, PERO NO ANDA */
	public List<List<Tramo>> recorridos1(Parada paradaOrigen, Parada paradaDestino, int horario, int nroLineas) {
		
		// crear el grafo (en caso de que necesite ser actualizado, patron OBSERVER)
		cargarDatos(coordinador.listarParadas(),coordinador.listarLineas(),coordinador.listarTramos());

		// Todos los recorridos
		YenKShortestPath<Parada, ParadaLinea> yksp = new YenKShortestPath<Parada, ParadaLinea>(red);
		List<GraphPath<Parada, ParadaLinea>> caminos = yksp.getPaths(paradaOrigen, paradaDestino, Integer.MAX_VALUE);

		// Eliminar recorridos superan cambioLineas
		List<Linea> lineas;
		Iterator<GraphPath<Parada, ParadaLinea>> r = caminos.iterator();
		while (r.hasNext()) {
			lineas = new ArrayList<Linea>();
			int cambioLineas = 0;
			for (ParadaLinea pl : r.next().getEdgeList())
				if (lineas.isEmpty())
					lineas.add(pl.getLinea());
				else if (!lineas.get(lineas.size() - 1).equals(pl.getLinea()))
					lineas.add(pl.getLinea());
			for (Linea l : lineas)
				if (l.getFrecuencia() != 0)
					cambioLineas++;
			if (cambioLineas > nroLineas)
				r.remove();
		}

		// Realizar c�lculo de tiempo y preparar resultados
		List<List<Tramo>> listaTramos = new ArrayList<List<Tramo>>();
		Tramo t = null;
		int proximoColectivo;
		int tiempo = 0;
		List<Tramo> tramos;
		List<ParadaLinea> paradalineas;
		List<Parada> paradas;
		Parada origen = null;
		Parada destino = null;
		TreeMap<String, Parada> pMap;
		for (GraphPath<Parada, ParadaLinea> gp : caminos) {
			pMap = new TreeMap<String, Parada>();
			paradas = gp.getVertexList();
			for (Parada p : paradas)
				pMap.put(p.getId(), new Parada(p.getId(), paradaMap.get(p.getId()).getDireccion(),paradaMap.get(p.getId()).getLat(), paradaMap.get(p.getId()).getLng()));
			proximoColectivo = horario;
			tramos = new ArrayList<Tramo>();
			paradalineas = gp.getEdgeList();
			for (int i = 0; i < paradalineas.size(); i++) {
				t = tramoMap.get(paradas.get(i).getId() + "-" + paradas.get(i + 1).getId());
				origen = pMap.get(paradas.get(i).getId());
				origen.agregarLinea(paradalineas.get(i).getLinea());
				destino = pMap.get(paradas.get(i + 1).getId());
				proximoColectivo = proximoColectivo(paradalineas.get(i).getLinea(), paradas.get(i),
						proximoColectivo + tiempo);
				tramos.add(new Tramo(origen, destino, t.getTipo(), proximoColectivo));
				tiempo = t.getTiempo();
			}
			destino.agregarLinea(origen.getLineas().get(0));
			tramos.add(new Tramo(destino, destino, t.getTipo(), proximoColectivo + t.getTiempo()));
			listaTramos.add(tramos);
		}
		return listaTramos;
	}

	/*private Graph<Parada, ParadaLinea> grafoRecorrido(Parada paradaOrigen, Parada paradaDestino){
		
		Set<ParadaLinea> paradaLineas = new HashSet<ParadaLinea>();
		paradaLineas.addAll(red.outgoingEdgesOf(paradaOrigen));
		paradaLineas.addAll(red.incomingEdgesOf(paradaDestino));
		
		Set <Linea> lineas = new HashSet<Linea>(); 
		for (ParadaLinea p: paradaLineas)
			lineas.add(p.getLinea());
		
		Graph<Parada, ParadaLinea> recorrido = new DirectedMultigraph<>(null, null, false);

		// Cargar paradas
		for (Parada p : paradaMap.values())
			recorrido.addVertex(p);

		// Cargar tramos lineas
		Parada origen, destino;
		for (Linea l : lineas)
			for (int i = 0; i < l.getParadas().size() - 1; i++) {
				origen = l.getParadas().get(i);
				destino = l.getParadas().get(i + 1);
				recorrido.addEdge(origen, destino, new ParadaLinea(origen, l));
			}

		// Cargar tramos caminando
		Linea linea;
		for (Tramo t : tramoMap.values())
			if (t.getTipo() == Constantes.TRAMO_CAMINANDO) {
				linea = new Linea(t.getInicio().getId() + "-" + t.getFin().getId(), Time.toMins("00:00"),
						Time.toMins("24:00"), 0);
				recorrido.addEdge(t.getInicio(), t.getFin(), new ParadaLinea(t.getInicio(), linea));
			}

		

		return recorrido;
	}*/
	
	private int proximoColectivo(Linea linea, Parada parada, int horario) {
		int nroParada = linea.getParadas().indexOf(parada);
		// Tramo caminando
		if (nroParada == -1)
			return horario;

		// Calcular el tiempo desde el inicio del recorrido a la parada
		Parada origen, destino;
		int tiempo = 0;
		for (int i = 0; i < nroParada; i++) {
			origen = linea.getParadas().get(i);
			destino = linea.getParadas().get(i + 1);
			tiempo += tramoMap.get(origen.getId() + "-" + destino.getId()).getTiempo();
		}

		// Ya pas� el �ltimo colectivo
		if (linea.getFinaliza() + tiempo < horario)
			return -1;

		// Tiempo del pr�ximo colectivo
		for (int j = linea.getComienza(); j <= linea.getFinaliza(); j += linea.getFrecuencia())
			if (j + tiempo >= horario)
				return j + tiempo;

		return -1;
	}
    public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}
    
    @Override
	public void update() {
		actualizar = true;
	}
    
    

// inner class
	private class ParadaLinea {
		private Parada parada;
		private Linea linea;

		public ParadaLinea(Parada parada, Linea linea) {
			this.parada = parada;
			this.linea = linea;
		}

		public Parada getParada() {
			return parada;
		}

		public void setParada(Parada parada) {
			this.parada = parada;
		}

		public Linea getLinea() {
			return linea;
		}

		public void setLinea(Linea linea) {
			this.linea = linea;
		}

		@Override
		public String toString() {
			return parada.getId() + " " + linea.getNombre();
		}

	}

}
