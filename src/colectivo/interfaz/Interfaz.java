package colectivo.interfaz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import colectivo.controlador.Constantes;
import colectivo.controlador.Coordinador;
import colectivo.modelo.Linea;
import colectivo.modelo.Parada;
import colectivo.modelo.Tramo;
import colectivo.util.Time;
import interfaz.waypoint.MyWaypoint;
import interfaz.waypoint.WaypointRenderer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import net.datastructures.TreeMap;


public class Interfaz extends JDialog{

    private final Set<MyWaypoint> waypoints = new HashSet<>();
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JXMapViewer mapViewer;
	
	@SuppressWarnings("unused")
	private Coordinador coordinador;

	
	public Interfaz() {
		setBounds(100, 100, 663, 514);
		 setTitle("Representacion del primer camino propuesto");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Crear visualizador del mapa
		mapViewer = new JXMapViewer();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(mapViewer, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(mapViewer, GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
		);
		contentPane.setLayout(gl_contentPane);

        GroupLayout jXMapViewerLayout = new GroupLayout(mapViewer);
        mapViewer.setLayout(jXMapViewerLayout);
		init();
		//this.setVisible(true);
		 // Centrar el mapa en Puerto Madryn
       // mapViewer.setAddressLocation(PUERTO_MADRYN_POSITION);
      //  mapViewer.setZoom(14); // Ajusta el nivel de zoom según sea necesario
	}
	
	private void createMouseMovement() {
		// Crear evento del mouse para detectar zoom y movimiento del mapa
		MouseInputListener movementListener = new PanMouseInputListener(mapViewer);
		mapViewer.addMouseListener(movementListener);
		mapViewer.addMouseMotionListener(movementListener);
		ZoomMouseWheelListenerCenter zoomListener = new ZoomMouseWheelListenerCenter(mapViewer);
		mapViewer.addMouseWheelListener(zoomListener);
	}

	private void init() {
		// Inicializar visaulizador del mapa
		TileFactoryInfo info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
		mapViewer.setTileFactory(tileFactory);

		createMouseMovement();		
		
	}
	
	public void mostrarResultado(List<Tramo> recorrido) {
	    // Mostrar solo las paradas de la ruta más rápida
	    createWaypoints(recorrido);
	}

	
	private void createWaypoints(List<Tramo> recorrido) {
		
		
		
		WaypointPainter<MyWaypoint> wp = new WaypointRenderer();
		
		// Limpiar tramo anterior
		if (wp != null) {
			// Esto limpia el mapa de todos los componentes añadidos previamente
            mapViewer.removeAll(); 
            waypoints.clear(); 
            mapViewer.setOverlayPainter(wp);
	    }
	    

		
	    List<Parada> paradas = new ArrayList<>();

	    // Obtener todas las paradas del recorrido
	    for (Tramo t : recorrido) {
	        if (!paradas.contains(t.getInicio()))
	            paradas.add(t.getInicio());
	      
	        if (!paradas.contains(t.getFin())) {
	            paradas.add(t.getFin());
	        }
	    }
	    
	  //  System.out.println(paradas);
	  //  System.out.println("test aca");
	   // System.out.println(paradas.get(0).getLat());

	    // Obtener la ruta más rápida entre todas las paradas
	    List<GeoPosition> fastestRoute = new ArrayList<>();
	    for (int i = 0; i < paradas.size() - 1; i++) {
	    	
	        String coords = paradas.get(i).getLng() + "," + paradas.get(i).getLat() + ";"
	            + paradas.get(i + 1).getLng() + "," + paradas.get(i + 1).getLat();
	        
	    //    System.out.println(coords);
	        List<GeoPosition> route = HttpRequester.getFastestRoute(coords);
	    //    System.out.println(route);
	        if (route != null)
	            fastestRoute.addAll(route);
	    }
	    
	   // System.out.println(fastestRoute);
	    
	   /* if(fastestRoute.size() == 0) {
	    	System.out.println("recorridos esta vacio (no encontro caminos posibles)");
			JOptionPane.showMessageDialog(null, "Hay estaciones que hacen referencia a esta l�nea!");
			//logger.error("Error al borrar linea");
			return;
	    } */

	    // Agregar puntos de la ruta más rápida como waypoints
	    for (GeoPosition point : fastestRoute)
	       waypoints.add(new MyWaypoint("ruta", point, true));
	    
	    // LOCALIZAR MAPA
	    
	    GeoPosition startingPoint = new GeoPosition(paradas.get(0).getLat(), paradas.get(0).getLng());
	    mapViewer.setAddressLocation(startingPoint);
		mapViewer.setZoom(5);
	    
	    // Agregar puntos originales (estaciones)
        for (Parada e : paradas) {
        	waypoints.add(new MyWaypoint(e.getId(), new GeoPosition(e.getLat(), e.getLng()), false));
        }

	    // Dibujar los puntos en el mapa
	    wp.setWaypoints(waypoints);
	    mapViewer.setOverlayPainter(wp);
	    for (MyWaypoint d : waypoints) {
	        mapViewer.add(d.getIcon());
	    }
	}
	
	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}
	
	public int opcion() {
		return Constantes.MAS_RAPIDO;
	}
	
	public Parada ingresarParadaOrigen(TreeMap<String, Parada> paradas) {
	    // Obtén la primera parada en el mapa (si existe)
		
		Parada terceraParada = null;
	    int contador = 0;

	    for (Parada parada : paradas.values()) {
	        if (contador == 0) { 
	            terceraParada = parada;
	            break;
	        }
	        contador++;
	    }

	    return terceraParada;
	}

	public Parada ingresarParadaDestino(TreeMap<String, Parada> paradas) {
	    Parada terceraParada = null;
	    int contador = 0;

	    for (Parada parada : paradas.values()) {
	        if (contador == 2) { 
	            terceraParada = parada;
	            break;
	        }
	        contador++;
	    }

	    return terceraParada;
	}
	public static void resultado(List<List<Tramo>> recorridos, int horario, TreeMap<String, Linea> lineas) {
		Linea linea;
		Tramo tramo;
		String nombreLinea;
		for (List<Tramo> tramos : recorridos) {
			System.out.println(Time.toTime(horario) + " - Llega a la parada");
			for (int i = 0; i < tramos.size() - 1; i++) {
				tramo = tramos.get(i);
				linea = tramo.getInicio().getLineas().get(0);
				nombreLinea = linea.getNombre();
				if (lineas.get(linea.getNombre())==null)
					nombreLinea = "CAMINANDO";
				System.out.println(Time.toTime(tramo.getTiempo()) + " - " + nombreLinea + " ("
						+ tramo.getInicio().getDireccion() + " " + " > " + tramo.getFin().getDireccion() + ")");
			}
			tramo = tramos.get(tramos.size() - 1);
			System.out.println(Time.toTime(tramo.getTiempo()) + " - Fin de recorrido");
			System.out.println("Tiempo Total: " + Time.toTime(tramo.getTiempo() - horario));
			System.out.println("============================================================");
			System.out.println();
		}
	}




}