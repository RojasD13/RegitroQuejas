package com.uptc.edu.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uptc.edu.main.dto.StateHistoryDTO;
import com.uptc.edu.main.model.Company; // Importar el nuevo DTO
import com.uptc.edu.main.model.Complaint;
import com.uptc.edu.main.model.State; 
import com.uptc.edu.main.repository.ComplaintRepo;

import jakarta.servlet.http.HttpSession;

// Si tienes una entidad para el historial de estados, descomenta esta línea
// import com.uptc.edu.main.repository.StateHistoryRepo;

@Service
public class ComplaintService {
    @Autowired 
    private ComplaintRepo complaintRepo;

    // Si tienes una tabla separada para el historial de estados, descomenta y autowirea su repositorio
    // @Autowired 
    // private StateHistoryRepo stateHistoryRepo;

    public Complaint saveComplaint(Complaint complaint) {
        return complaintRepo.save(complaint);
    }

    public List<Complaint> listComplaints() {
        return complaintRepo.findAll();
    }

    public List<Complaint> getComplaintsByCompany(Company company) {
        return complaintRepo.findByCompany(company);
    }

    public Optional<Complaint> searchById(Long id) {
        return complaintRepo.findById(id);
    }

    public List<Complaint> obtainVisibleComplaints(Long companyId) {
        return (companyId == null)
                ? complaintRepo.findByIsVisibleTrue()
                : complaintRepo.findByCompanyIdAndIsVisibleTrue(companyId);
    }

    /**
     * Oculta una queja existente. Este método está diseñado para ser usado por controladores
     * que manejan vistas tradicionales con redirecciones.
     */
    public void hideComplaintIfExists(Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        complaintRepo.findById(id).ifPresentOrElse(complaint -> {
            complaint.setVisible(false);
            complaintRepo.save(complaint);
            redirectAttributes.addFlashAttribute("mensaje", "Queja eliminada exitosamente");
            session.setAttribute("ultimaEmpresaBuscada", complaint.getCompany().getId());
        }, () -> redirectAttributes.addFlashAttribute("error", "La queja no existe"));
    }

    /**
     * Cambia el estado de una queja. Este método está diseñado para ser usado por endpoints de API REST.
     * No maneja redirecciones, sino que lanza excepciones en caso de error.
     *
     * @param id El ID de la queja a modificar.
     * @param state El nuevo estado en formato de texto (ej. "PROCESO").
     * @throws RuntimeException si la queja no existe o si el estado no es válido.
     */
    public void changeComplaintState(Long id, String state) {
        complaintRepo.findById(id).ifPresentOrElse(complaint -> {
            try {
                State newState = State.valueOf(state.toUpperCase());
                complaint.setState(newState);
                complaintRepo.save(complaint);
                
            } catch (IllegalArgumentException e) {
                
                throw new RuntimeException("Estado no válido: " + state);
            }
        }, () -> {
            
            throw new RuntimeException("La queja con ID " + id + " no existe.");
        });
    }

    /**
     * Obtiene el historial de cambios de estado para una queja específica.
     *
     * @param complaintId El ID de la queja.
     * @return Una lista de DTOs con el historial de estados.
     */
    public List<StateHistoryDTO> getStateHistoryForComplaint(Long complaintId) {
        // --- IMPORTANTE: DEBES IMPLEMENTAR LA LÓGICA AQUÍ ---
        // Este es un ejemplo de cómo podrías implementarlo si tuvieras una entidad 'StateHistory'
        // y su repositorio correspondiente 'StateHistoryRepo'.
        /*
        return stateHistoryRepo.findByComplaintIdOrderByChangeDateAsc(complaintId)
                .stream()
                .map(history -> new StateHistoryDTO(
                    history.getNewState().getDisplayName(), // O history.getNewState().toString()
                    history.getChangeDate()
                ))
                .collect(Collectors.toList());
        */

        // Por ahora, devuelve una lista vacía para evitar errores de compilación.
        // Reemplaza esto con tu implementación real que consulte la base de datos.
        return List.of(); 
    }
}