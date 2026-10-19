package co.edu.usbcali.autosusbcali.controller;

import co.edu.usbcali.autosusbcali.dto.response.ObtenerRolResponse;
import co.edu.usbcali.autosusbcali.service.RolService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
@Slf4j
public class RolController {
    private final RolService rolService;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping
    public List<ObtenerRolResponse> obtenerTodos(){
        return rolService.obtenerTodos();
    }

    @GetMapping("/activos")
    public List<ObtenerRolResponse> obtenerTodosActivos(){
        return rolService.buscarRolPorEstado(true);
    }

    @GetMapping("/inactivos")
    public List<ObtenerRolResponse> obtenerTodosInactivos(){
        return rolService.buscarRolPorEstado(false);
    }

    @GetMapping("/por-nombre/{nombre}")
    public ResponseEntity<ObtenerRolResponse> obtenerPorNombre(@PathVariable String nombre){
        return new ResponseEntity<>(rolService.obtenerRolPorNombre(nombre), HttpStatus.OK);
    }

    @GetMapping("/por-id/{id}")
    public ResponseEntity<ObtenerRolResponse> obtenerPorId(@PathVariable Long id){
        return new ResponseEntity<>(rolService.obtenerRolPorId(id), HttpStatus.OK);
    }
}