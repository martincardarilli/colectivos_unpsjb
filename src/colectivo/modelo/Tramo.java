package colectivo.modelo;

/**
 * Representa un tramo entre dos paradas en una línea de colectivo.
 * Contiene información como la parada de inicio, la parada de fin, el tipo de tramo y el tiempo estimado para recorrerlo.
 */
public class Tramo {

	private Parada inicio;
	private Parada fin;
	private int tipo;
	private int tiempo;
	
	
	
	public Tramo () {
		
	}
	
	 /**
     * Constructor que permite establecer la parada de inicio, la parada de fin, el tipo de tramo y el tiempo estimado.
     *
     * @param inicio La parada de inicio del tramo.
     * @param fin    La parada de fin del tramo.
     * @param tipo   El tipo de tramo.
     * @param tiempo El tiempo estimado para recorrer el tramo.
     */
	
	public Tramo(Parada inicio, Parada fin, int tipo, int tiempo) {
		super();
		this.inicio = inicio;
		this.fin = fin;
		this.tipo = tipo;
		this.tiempo = tiempo;
	}
	
	



	public Parada getInicio() {
		return inicio;
	}

	public void setInicio(Parada inicio) {
		this.inicio = inicio;
	}

	public Parada getFin() {
		return fin;
	}

	public void setFin(Parada fin) {
		this.fin = fin;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fin == null) ? 0 : fin.hashCode());
		result = prime * result + ((inicio == null) ? 0 : inicio.hashCode());
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
		Tramo other = (Tramo) obj;
		if (fin == null) {
			if (other.fin != null)
				return false;
		} else if (!fin.equals(other.fin))
			return false;
		if (inicio == null) {
			if (other.inicio != null)
				return false;
		} else if (!inicio.equals(other.inicio))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tramo [inicio=" + inicio + ", fin=" + fin + ", tipo=" + tipo + ", tiempo=" + tiempo + "]";
	}
	
	
			
}
