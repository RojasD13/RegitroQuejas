package com.uptc.edu.main;

import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.model.Complaint;
import com.uptc.edu.main.repository.CompanyRepo;
import com.uptc.edu.main.repository.ComplaintRepo;
import com.uptc.edu.main.service.CompanyService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;

import java.util.List;
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

    @Mock
    private ComplaintRepo complaintRepo;

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

    @Test
    void testListCompanies_usingService() {
        Company c1 = new Company();
        c1.setId(10L);
        c1.setName("A");
        Company c2 = new Company();
        c2.setId(11L);
        c2.setName("B");
        when(empresaRepo.findAll()).thenReturn(List.of(c1, c2));
        List<Company> result = service.listCompanies();
        assertThat(result).hasSize(2).containsExactlyElementsOf(List.of(c1, c2));
        verify(empresaRepo, times(1)).findAll();
    }

    @Test
    void testCreateComplaintForExistingCompany_createsComplaintAndSetsModelMessage() {
        Company company = new Company();
        company.setId(5L);
        company.setName("CompX");
        when(empresaRepo.findByName("CompX")).thenReturn(Optional.of(company));
        var model = new ConcurrentModel();
        service.createComplaintForExistingCompany("CompX", "Descripcion prueba", model);
        verify(complaintRepo, times(1)).save(any(Complaint.class));
        assertThat(model.getAttribute("mensaje")).isEqualTo("La queja fue registrada exitosamente.");
        assertThat(model.getAttribute("tipoMensaje")).isEqualTo("success");
    }

    @Test
    void testGetCompanySummaries_countsVisibleComplaints() {
        Company company = new Company();
        company.setId(7L);
        company.setName("EntidadY");
        when(empresaRepo.findAllByOrderByNameAsc()).thenReturn(List.of(company));
        when(complaintRepo.findByCompanyIdAndIsVisibleTrue(7L))
                .thenReturn(List.of(new Complaint(), new Complaint(), new Complaint()));
        var summaries = service.getCompanySummaries();
        assertThat(summaries).hasSize(1);
        assertThat(summaries.get(0).getTotalComplaints()).isEqualTo(3L);
        verify(empresaRepo, times(1)).findAllByOrderByNameAsc();
        verify(complaintRepo, times(1)).findByCompanyIdAndIsVisibleTrue(7L);
    }
}
