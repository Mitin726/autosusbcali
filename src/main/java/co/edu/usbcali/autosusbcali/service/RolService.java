package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.domain.Rol;
import co.edu.usbcali.autosusbcali.dto.request.CrearModificarRolRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerRolResponse;
import co.edu.usbcali.autosusbcali.mapper.RolMapper;
import co.edu.usbcali.autosusbcali.repository.RolRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RolService implements IRolService{
    private final RolRepository rolRepository;

    @Override
    public List<ObtenerRolResponse> obtenerTodos() {
        return RolMapper.listaRolesAListaObtenerRolResponse(rolRepository.findAll());
    }

    @Override
    public List<ObtenerRolResponse> buscarRolPorEstado(Boolean activo) {
        List<Rol> rolesPorEstado = rolRepository.findByActivo(activo);
        return RolMapper.listaRolesAListaObtenerRolResponse(rolesPorEstado);
    }

    @Override
    public ObtenerRolResponse obtenerRolPorNombre(String nombre) {
        Rol rolPorNombre = rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el rol por nombre: "+nombre));
        return RolMapper.rolAObtenerRolResponse(rolPorNombre);
    }

    @Override
    public ObtenerRolResponse obtenerRolPorId(Long id) {
        Rol rolPorId = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el rol por id "+id));
        return RolMapper.rolAObtenerRolResponse(rolPorId);
    }

    @Override
    public ObtenerRolResponse crearRol(CrearModificarRolRequest crearModificarRolRequest) throws Exception {
        // Generar validaciones del objeto #CrearModificarRolRequest
        if (crearModificarRolRequest == null) {
            throw new Exception("El objeto CrearModificarRolRequest no puede ser nulo.");
        }
        if (crearModificarRolRequest.nombre() == null
        || crearModificarRolRequest.nombre().isBlank()) {
            throw new Exception("El nombre no puede ser nulo ni estar vacío.");
        }
        if (crearModificarRolRequest.descripcion() == null
        || crearModificarRolRequest.descripcion().isBlank()) {
            throw new Exception("El descripcion no puede ser nulo ni estar vacío.");
        }
        //buscar si exsiste un rol con el nombre que se quiere agregar
        /*
        * Optional<Rol> rolPorNombre = rolRepository.findByNombre(CrearModificarRolRequest.nombre());
        if (rolPorNombre.isPresent()) {
            throw new Exception("El rol existe en la base de datos "+CrearModificarRolRequest.nombre());
        }*/

        //buscar si exsiste un rol con el nombre que se quiere agregar opcion 2
        if (rolRepository.existsByNombre(crearModificarRolRequest.nombre())) {
           throw new Exception("Ya existe el rol con el mismo nombre.");
        }

        // Usar mapper para convertir DTO a objeto de Domain
        Rol rol = RolMapper.CrearModificarRolRequestARol(crearModificarRolRequest);

        // persistir el Rol en base de datos
        rol = rolRepository.save(rol);

        // crear DTO response obtenerRolResponse
        ObtenerRolResponse rolResponse = RolMapper.rolAObtenerRolResponse(rol);

        return rolResponse;
    }

    @Override
    public ObtenerRolResponse modificarRol(Long id, CrearModificarRolRequest crearModificarRolRequest) throws Exception {
        // Validaciones del id
        if (id == null) {
            throw new Exception("El id no puede ser nulo.");
        }

        // Validaciones del objeto CrearModificarRolRequest
        if (crearModificarRolRequest == null) {
            throw new Exception("El objeto CrearModificarRolRequest no puede ser nulo.");
        }
        if (crearModificarRolRequest.nombre() == null
                || crearModificarRolRequest.nombre().isBlank()) {
            throw new Exception("El nombre no puede ser nulo ni estar vacío.");
        }
        if (crearModificarRolRequest.descripcion() == null
                || crearModificarRolRequest.descripcion().isBlank()) {
            throw new Exception("La descripcion no puede ser nula ni estar vacía.");
        }

        // Buscar el rol que se quiere modificar
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new Exception("No existe un rol con el id " + id));

        // Validar que el nuevo nombre no lo tenga OTRO rol
        // (si el nombre es el mismo que ya tiene este rol, no hay problema)
        if (!rol.getNombre().equals(crearModificarRolRequest.nombre())
                && rolRepository.existsByNombre(crearModificarRolRequest.nombre())) {
            throw new Exception("Ya existe otro rol con el mismo nombre.");
        }

        // Actualizar los campos sobre el objeto que ya existe en BD
        rol.setNombre(crearModificarRolRequest.nombre());
        rol.setDescripcion(crearModificarRolRequest.descripcion());

        // Persistir
        rol = rolRepository.save(rol);

        return RolMapper.rolAObtenerRolResponse(rol);
    }

    @Override
    public void eliminarRol(Long id) throws Exception {
        if (id == null) {
            throw new Exception("El id no puede ser nulo.");
        }

        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new Exception("No existe un rol con el id " + id));

        // Evitar "eliminar" algo que ya está inactivo
        if (!rol.getActivo()) {
            throw new Exception("El rol ya se encuentra inactivo.");
        }

        rol.setActivo(false);
        rolRepository.save(rol);
    }
}