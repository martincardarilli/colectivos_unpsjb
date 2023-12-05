package colectivo.controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import colectivo.modelo.Linea;

import colectivo.negocio.Empresa;
import colectivo.negocio.LineaExisteException;

public class AplicacionConsultaTest {

    private Empresa empresa;

    @Before
    public void setUp() {
        this.empresa = Empresa.getEmpresa();
    }

    @Test
    public void testAgregarLinea() throws LineaExisteException {
        Linea nuevaLinea = new Linea("NuevaLinea");
        empresa.agregarLinea(nuevaLinea);

        assertNotNull(empresa.getLineas().get("NuevaLinea"));
    }

    @Test(expected = LineaExisteException.class)
    public void testAgregarLineaExistente() throws LineaExisteException {
        Linea lineaExistente = empresa.getLineas().firstEntry().getValue(); // Tomar una línea existente
        empresa.agregarLinea(lineaExistente);
    }

    /* @Test
    public void testModificarLinea() {
        Linea lineaExistente = empresa.getLineas().firstEntry().getValue(); // Tomar una línea existente

        if (lineaExistente != null) {
            lineaExistente.setNombre("NuevaLinea");
            empresa.modificarLinea(lineaExistente);

            assertEquals("NuevaLinea", empresa.getLineas().get(lineaExistente.getNombre()).getNombre());
        } else {
            fail("La línea existente es nula");
        }
    }*/


    @Test
    public void testBorrarLinea() {
        Linea lineaExistente = empresa.getLineas().firstEntry().getValue(); // Tomar una línea existente

        empresa.borrarLinea(lineaExistente);

        assertNull(empresa.getLineas().get(lineaExistente.getNombre()));
    }

    @Test
    public void testBuscarLinea() {
        Linea lineaExistente = empresa.getLineas().firstEntry().getValue(); // Tomar una línea existente

        Linea lineaEncontrada = empresa.buscarLinea(lineaExistente);

        assertEquals(lineaExistente, lineaEncontrada);
    }
}
