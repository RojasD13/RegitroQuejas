package com.uptc.edu.main;

import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.repository.CompanyRepo;
import com.uptc.edu.main.service.CompanyService;

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
    private CompanyRepo empresaRepo;

    @InjectMocks
    private CompanyService service; // Ejemplo de servicio que usaría EmpresaRepo

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarEmpresa() {
        Company empresa = new Company();
        empresa.setId(1L);
        empresa.setName("Empresa Test");

        when(empresaRepo.save(any(Company.class))).thenReturn(empresa);

        Company saved = empresaRepo.save(new Company());
        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getName()).isEqualTo("Empresa Test");

        verify(empresaRepo, times(1)).save(any(Company.class));
    }

    @Test
    void testBuscarEmpresaPorNombre() {
        Company empresa = new Company();
        empresa.setId(2L);
        empresa.setName("Empresa Unica2");

        when(empresaRepo.findByName("Empresa Unica2"))
                .thenReturn(Optional.of(empresa));

        Company encontrada = empresaRepo.findByName("Empresa Unica2").orElse(null);
        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getName()).isEqualTo("Empresa Unica2");

        verify(empresaRepo, times(1)).findByName("Empresa Unica2");
    }

    @Test
    void testEliminarEmpresa() {
        Long id = 3L;

        doNothing().when(empresaRepo).deleteById(id);
        when(empresaRepo.findById(id)).thenReturn(Optional.empty());

        empresaRepo.deleteById(id);
        Company eliminada = empresaRepo.findById(id).orElse(null);

        assertThat(eliminada).isNull();
        verify(empresaRepo, times(1)).deleteById(id);
        verify(empresaRepo, times(1)).findById(id);
    }
}
