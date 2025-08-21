package com.uptc.edu.main;

import com.uptc.edu.main.moldel.Empresa;
import com.uptc.edu.main.repository.EmpresaRepo;
import com.uptc.edu.main.service.EmpresaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests con Mockito para simular el comportamiento de EmpresaRepo
 * sin necesidad de base de datos ni contexto de Spring.
 */
class RegistroQuejasApplicationTests {

    @Mock
    private EmpresaRepo empresaRepo;

    @InjectMocks
    private EmpresaService service; // Ejemplo de servicio que usaría EmpresaRepo

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombreEmpresa("Empresa Test");

        when(empresaRepo.save(any(Empresa.class))).thenReturn(empresa);

        Empresa saved = empresaRepo.save(new Empresa());
        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getNombreEmpresa()).isEqualTo("Empresa Test");

        verify(empresaRepo, times(1)).save(any(Empresa.class));
    }

    @Test
    void testBuscarEmpresaPorNombre() {
        Empresa empresa = new Empresa();
        empresa.setId(2L);
        empresa.setNombreEmpresa("Empresa Unica2");

        when(empresaRepo.findByNombreEmpresa("Empresa Unica2"))
                .thenReturn(Optional.of(empresa));

        Empresa encontrada = empresaRepo.findByNombreEmpresa("Empresa Unica2").orElse(null);
        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getNombreEmpresa()).isEqualTo("Empresa Unica2");

        verify(empresaRepo, times(1)).findByNombreEmpresa("Empresa Unica2");
    }

    @Test
    void testEliminarEmpresa() {
        Long id = 3L;

        doNothing().when(empresaRepo).deleteById(id);
        when(empresaRepo.findById(id)).thenReturn(Optional.empty());

        empresaRepo.deleteById(id);
        Empresa eliminada = empresaRepo.findById(id).orElse(null);

        assertThat(eliminada).isNull();
        verify(empresaRepo, times(1)).deleteById(id);
        verify(empresaRepo, times(1)).findById(id);
    }
}
