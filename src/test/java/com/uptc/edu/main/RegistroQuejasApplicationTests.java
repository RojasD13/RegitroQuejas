package com.uptc.edu.main;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import com.uptc.edu.main.repository.EmpresaRepo;
import com.uptc.edu.main.moldel.Empresa;

@SpringBootTest
class RegistroQuejasApplicationTests {

    @Autowired
    private EmpresaRepo empresaRepo;

    @Test
    void contextLoads() {
    }

    @Test
    void testGuardarEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setNombreEmpresa("Empresa Test");
        Empresa saved = empresaRepo.save(empresa);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNombreEmpresa()).isEqualTo("Empresa Test");
    }

	@Test
    void testBuscarEmpresaPorNombre() {
        Empresa empresa = new Empresa();
        empresa.setNombreEmpresa("Empresa Unica");
        empresaRepo.save(empresa);

        Empresa encontrada = empresaRepo.findByNombreEmpresa("Empresa Unica").orElse(null);
        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getNombreEmpresa()).isEqualTo("Empresa Unica");
    }

    @Test
    void testEliminarEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setNombreEmpresa("Empresa Para Eliminar");
        Empresa saved = empresaRepo.save(empresa);

        empresaRepo.deleteById(saved.getId());
        Empresa eliminada = empresaRepo.findById(saved.getId()).orElse(null);
        assertThat(eliminada).isNull();
    }

}
