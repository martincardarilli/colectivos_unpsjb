package colectivo.modelo;

import java.util.ArrayList;
import java.util.List;
/**
 * Clase que representa una línea de colectivo.
 * Contiene información sobre el nombre de la línea y las paradas asociadas para los trayectos de ida y regreso.
 * @author maiac
 * @author martincardarilli
 * @author mpacheco
 */
public class Linea {

	private String nombre;
	private int comienza;
	private int finaliza;
	private int frecuencia;
	private List<Parada> paradas;
	
	public Linea(String nombre) {
		this.nombre = nombre;
		this.paradas = new ArrayList<Parada>();	
	}
	public Linea(String nombre, int comienza, int finaliza, int frecuencia) {
		super();		
		this.nombre = nombre;
		this.comienza = comienza;
		this.finaliza = finaliza;
		this.frecuencia = frecuencia;
		this.paradas = new ArrayList<Parada>();		
	}

	public void agregarParada(Parada parada) {
		if (parada != null) {
			paradas.add(parada);
			parada.agregarLinea(this);
		}

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getComienza() {
		return comienza;
	}

	public void setComienza(int comienza) {
		this.comienza = comienza;
	}

	public int getFinaliza() {
		return finaliza;
	}

	public void setFinaliza(int finaliza) {
		this.finaliza = finaliza;
	}

	public int getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(int frecuencia) {
		this.frecuencia = frecuencia;
	}

	public List<Parada> getParadas() {
		return paradas;
	}

	public void setParadas(List<Parada> paradas) {
		this.paradas = paradas;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Linea other = (Linea) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nombre;
	}	
	
}
