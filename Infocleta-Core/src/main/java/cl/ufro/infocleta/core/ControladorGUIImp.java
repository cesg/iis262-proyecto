package cl.ufro.infocleta.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.ufro.infocleta.beans.Alumno;
import cl.ufro.infocleta.beans.ListaAlumnos;
import cl.ufro.infocleta.core.data.DataManager;
import cl.ufro.infocleta.core.imp.linked.ListaEnlazada;
import cl.ufro.infocleta.gui.ControladorGUI;

/**
 * <p>
 * Controlador de la GUI de la aplicación
 * </p>
 * 
 * @author c3sg
 */
public class ControladorGUIImp implements ControladorGUI {
    private static final Logger log = LoggerFactory
	    .getLogger(ControladorGUIImp.class);
    private ListaAlumnos lista;
    private Queue<Alumno> cola;
    private DataManager db;
    private boolean primer;

    public ControladorGUIImp() {
	db = new DataManager();
	lista = new ListaEnlazada();
	cola = new LinkedList<>();
	primer = true;
    }

    @Override
    public void agregarAlumno(Alumno a) {
	ToStringBuilder sb = new ToStringBuilder(a,
	        ToStringStyle.SHORT_PREFIX_STYLE);
	sb.append("Matricula", a.getMatricula());
	sb.append("Nombre", a.getNombre());
	log.debug("Insertado {}", sb);
	lista.insertar(a);
    }

    @Override
    public Alumno buscarAlumno(Alumno a) {
	ToStringBuilder sb = new ToStringBuilder(a,
	        ToStringStyle.SHORT_PREFIX_STYLE);
	sb.append("Matricula", a.getMatricula());
	sb.append("Nombre", a.getNombre());
	log.debug("# Buscando {}", sb);
	return lista.obtener(a.getMatricula());
    }

    @Override
    public ListaAlumnos todosAlumnos() {
	if (primer) {
	    List<Alumno> alumnosDB = db.obtenerTodos();
	    if (!alumnosDB.isEmpty())
		for (Alumno alumno : alumnosDB) {
		    lista.insertar(alumno);
		}
	}
	primer = false;
	return lista;
    }

    @Override
    public boolean borrarAlumno(Alumno a) {
	ToStringBuilder sb = new ToStringBuilder(a,
	        ToStringStyle.SHORT_PREFIX_STYLE);
	sb.append("Matricula", a.getMatricula());
	sb.append("Nombre", a.getNombre());
	log.debug("Eliminando {}", sb);
	db.borrar(a);
	return lista.eliminar(a);
    }

    @Override
    public Alumno buscarAlumno(String codigo) {
	return lista.obtener(codigo);
    }

    @Override
    public boolean borrarAlumno(String codigo) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void agregarACola(Alumno a) {
	cola.add(a);
    }

    @Override
    public Queue<Alumno> obtenerAlumnosEnCola() {
	return cola;
    }

    @Override
    public void vaciarCola() {
	log.debug("Vaciando cola, elementos {}", cola.size());
	for (Alumno alumno : cola) {
	    boolean exists = lista.eliminar(alumno);
	    if (exists)
		log.debug("Alumno eliminado de la lista  id: {}", alumno);
	    else
		log.debug("No se encontro al alumno en la lista id: {}", alumno);
	    cola.remove(alumno);
	}
    }

    @Override
    public int alumnosEnCola() {
	return cola.size();
    }

    public void obtenerDB() {
	List<Alumno> l = db.obtenerTodos();
	for (Alumno alumno : l) {
	    lista.insertar(alumno);
	}
    }
}
