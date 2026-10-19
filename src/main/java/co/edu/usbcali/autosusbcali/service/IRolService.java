package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.dto.response.ObtenerRolResponse;

import java.util.List;
import java.util.Optional;

public interface IRolService {
    // Buscar todos los roles sin importar su estado
    List<ObtenerRolResponse> obtenerTodos();

    // Buscamos los roles por estado (ya sea activo o inactivo)
    List<ObtenerRolResponse> buscarRolPorEstado(Boolean activo);

    ObtenerRolResponse obtenerRolPorNombre(String nombre);

    ObtenerRolResponse obtenerRolPorId(Long id);
}
