package com.scheduler.schedulerapp.mapper;

import com.scheduler.schedulerapp.dto.*;

import com.scheduler.schedulerapp.model.*;

import com.scheduler.schedulerapp.repository.DoctorRepository;
import com.scheduler.schedulerapp.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class DTOMapper {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    public DoctorResponseDTO toDoctorResponseDTO(HospitalStaff doctor) {
        DoctorResponseDTO dto = new DoctorResponseDTO();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setEmail(doctor.getEmail());
        dto.setRole(doctor.getRole());
        dto.setStartDate(doctor.getStartDate());
        dto.setEndDate(doctor.getEndDate());
        dto.setIsActive(doctor.getIsActive());
        return dto;
    }

    public PatientResponseDTO toPatientResponseDTO(Patient patient) {
        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setAge(patient.getAge());
        dto.setRole(patient.getRole());
        dto.setIsActive(patient.getIsActive());
        return dto;
    }

    public AppointmentResponseDTO toAppointmentResponseDTO(Appointment appointment) {
        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.setId(appointment.getId());
        dto.setTitle(appointment.getTitle());
        dto.setDescription(appointment.getDescription());
        dto.setDoctorId(appointment.getDoctorId());
        dto.setPatientId(appointment.getPatientId());
        dto.setStartTime(appointment.getStartTime().format(ISO_FORMATTER));
        dto.setEndTime(appointment.getEndTime().format(ISO_FORMATTER));
        dto.setStatus(appointment.getStatus());
        dto.setBranchId(appointment.getBranchId());
        dto.setCreatedAt(appointment.getCreatedAt().format(ISO_FORMATTER));
        dto.setUpdatedAt(appointment.getUpdatedAt().format(ISO_FORMATTER));
        dto.setDuration(calculateDuration(appointment));

        Optional<HospitalStaff> doctor = doctorRepository.findById(appointment.getDoctorId());
        dto.setDoctorName(doctor.map(HospitalStaff::getName).orElse("Unknown doctor"));

        Optional<Patient> patient = patientRepository.findById(appointment.getPatientId());
        dto.setPatientName(patient.map(Patient::getName).orElse("Unknown patient"));

        if (appointment.getBranchId() != null) {
            dto.setBranchLocation("Branch Location");
        }
        return dto;
    }

    private String calculateDuration(Appointment appointment) {
        Duration duration = Duration.between(appointment.getStartTime(), appointment.getEndTime());
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        if (hours == 0) {
            return minutes + " minutes";
        } else if (minutes == 0) {
            return hours + " hours";
        } else {
            return hours + " hours " + minutes + " minutes";
        }
    }

    public HospitalBranchResponseDTO toHospitalBranchResponseDTO(HospitalBranch branch) {
        HospitalBranchResponseDTO dto = new HospitalBranchResponseDTO();
        dto.setId(branch.getId());
        dto.setBranchCode(branch.getBranchCode());
        dto.setAddress(branch.getAddress());
        dto.setCity(branch.getCity());
        dto.setState(branch.getState());
        dto.setZipCode(branch.getZipCode());
        dto.setEmail(branch.getEmail());
        dto.setPhoneNumber(branch.getPhoneNumber());
        dto.setStartedAt(branch.getStartedAt() != null ? branch.getStartedAt().toString() : null);
        dto.setClosedAt(branch.getClosedAt() != null ? branch.getClosedAt().toString() : null);
        dto.setIsActive(branch.getIsActive());
        return dto;
    }

    public DoctorBranchMappingResponseDTO toDoctorBranchMappingResponseDTO(
            StaffBranchMapping mapping,
            String doctorName,
            String branchCode) {

        DoctorBranchMappingResponseDTO dto = new DoctorBranchMappingResponseDTO();
        dto.setId(mapping.getId());
        dto.setDoctorId(mapping.getDoctorId());
        dto.setBranchId(mapping.getBranchId());
        dto.setDoctorName(doctorName);
        dto.setBranchCode(branchCode);
        return dto;
    }

    public ActivityLogResponseDTO toActivityLogResponseDTO(ActivityLog activityLog) {
        ActivityLogResponseDTO dto = new ActivityLogResponseDTO();
        dto.setId(activityLog.getId());
        dto.setEntityType(activityLog.getEntityType());
        dto.setEntityId(activityLog.getEntityId());
        dto.setActionType(activityLog.getActionType());
        dto.setDescription(activityLog.getDescription());
        dto.setPerformedBy(activityLog.getPerformedBy());
        dto.setPerformedByName(activityLog.getPerformedByName());
        dto.setTimestamp(activityLog.getTimestamp() != null ? activityLog.getTimestamp().toString() : null);

        dto.setState(activityLog.getStateAsString());
        dto.setRelatedEntities(activityLog.getRelatedEntitiesAsString());
        dto.setImpactSummary(activityLog.getImpactSummary());

        dto.setStaffRole(activityLog.getStaffRole());
        dto.setStaffName(activityLog.getStaffName());
        dto.setBranchCode(activityLog.getBranchCode());
        dto.setBranchLocation(activityLog.getBranchLocation());
        return dto;
    }
}