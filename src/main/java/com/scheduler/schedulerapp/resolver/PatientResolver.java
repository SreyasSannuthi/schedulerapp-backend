package com.scheduler.schedulerapp.resolver;

import com.scheduler.schedulerapp.dto.PatientResponseDTO;
import com.scheduler.schedulerapp.dto.PatientUpdateInputDTO;
import com.scheduler.schedulerapp.mapper.DTOMapper;
import com.scheduler.schedulerapp.model.Patient;
import com.scheduler.schedulerapp.service.patient.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PatientResolver {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DTOMapper dtoMapper;

    @QueryMapping
    public List<PatientResponseDTO> patients() {
        return patientService.getAllPatients().stream()
                .map(dtoMapper::toPatientResponseDTO)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public PatientResponseDTO patient(@Argument String id) {
        return patientService.getPatientById(id)
                .map(dtoMapper::toPatientResponseDTO)
                .orElse(null);
    }

    @QueryMapping
    public List<PatientResponseDTO> patientsByRole(@Argument String role) {
        return patientService.getPatientsByRole(role).stream()
                .map(dtoMapper::toPatientResponseDTO)
                .collect(Collectors.toList());
    }

    @MutationMapping
    public PatientResponseDTO updatePatient(@Argument String id, @Argument @Valid PatientUpdateInputDTO input) {
        try {
            Patient existingPatient = patientService.getPatientById(id)
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

            if (input.getName() != null) {
                existingPatient.setName(input.getName().trim());
            }
            if (input.getEmail() != null) {
                existingPatient.setEmail(input.getEmail().toLowerCase().trim());
            }
            if (input.getPhoneNumber() != null) {
                existingPatient.setPhoneNumber(input.getPhoneNumber().trim());
            }
            if (input.getAge() != null) {
                existingPatient.setAge(input.getAge());
            }
            if (input.getIsActive() != null) {
                existingPatient.setIsActive(input.getIsActive());
            }

            Patient updatedPatient = patientService.updatePatient(id, existingPatient);
            return dtoMapper.toPatientResponseDTO(updatedPatient);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update patient: " + e.getMessage());
        }
    }

    @MutationMapping
    public Boolean deletePatient(@Argument String id) {
        try {
            patientService.deletePatient(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete patient: " + e.getMessage());
        }
    }
}
