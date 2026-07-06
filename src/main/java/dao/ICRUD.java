package dao;

import java.util.List;

public interface ICRUD<T> {

    boolean guardar(T objeto);

    boolean actualizar(T objeto);

    boolean eliminar(int id);

    List<T> listar();

    T buscarPorId(int id);
}