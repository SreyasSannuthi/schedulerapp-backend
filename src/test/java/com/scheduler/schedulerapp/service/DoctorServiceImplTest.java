package com.scheduler.schedulerapp.service;

import com.scheduler.schedulerapp.model.HospitalStaff;
import com.scheduler.schedulerapp.repository.DoctorRepository;
import com.scheduler.schedulerapp.service.doctor.DoctorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private HospitalStaff doctor1;
    private HospitalStaff doctor2;
    private HospitalStaff doctor3;
    private HospitalStaff adminDoctor;

    @BeforeEach
    void setUp() {
        doctor1 = new HospitalStaff("1", "Dr. John Smith", "john.smith@hospital.com", "doctor", "password123",
                "July 22 2025 5:51 PM", "", true);
        doctor2 = new HospitalStaff("2", "Dr. Jane Doe", "jane.doe@hospital.com", "doctor", "password123",
                "July 22 2025 5:51 PM", "", true);
        doctor3 = new HospitalStaff("3", "Dr. Bob Wilson", "bob.wilson@hospital.com", "doctor", "password123",
                "July 22 2025 5:51 PM", "", true);
        adminDoctor = new HospitalStaff("4", "Dr. Admin", "admin@hospital.com", "admin", "admin123",
                "July 22 2025 5:51 PM", "", true);
    }

    @Test
    void getAllDoctors_ShouldReturnAllDoctors() {
        List<HospitalStaff> expectedDoctors = Arrays.asList(doctor1, doctor2, doctor3, adminDoctor);
        when(doctorRepository.findAll()).thenReturn(expectedDoctors);
        List<HospitalStaff> result = doctorService.getAllDoctors();
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(expectedDoctors, result);
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void getAllDoctors_WhenNoDoctorsExist_ShouldReturnEmptyList() {
        when(doctorRepository.findAll()).thenReturn(Arrays.asList());
        List<HospitalStaff> result = doctorService.getAllDoctors();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void getDoctorById_WhenDoctorExists_ShouldReturnDoctor() {
        String doctorId = "1";
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor1));
        Optional<HospitalStaff> result = doctorService.getDoctorById(doctorId);
        assertTrue(result.isPresent());
        assertEquals(doctor1, result.get());
        assertEquals("1", result.get().getId());
        assertEquals("Dr. John Smith", result.get().getName());
        verify(doctorRepository, times(1)).findById(doctorId);
    }

    @Test
    void getDoctorById_WhenDoctorDoesNotExist_ShouldReturnEmptyOptional() {
        String doctorId = "999";
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());
        Optional<HospitalStaff> result = doctorService.getDoctorById(doctorId);
        assertFalse(result.isPresent());
        verify(doctorRepository, times(1)).findById(doctorId);
    }

    @Test
    void getDoctorById_WithNullId_ShouldReturnEmptyOptional() {
        when(doctorRepository.findById(null)).thenReturn(Optional.empty());
        Optional<HospitalStaff> result = doctorService.getDoctorById(null);
        assertFalse(result.isPresent());
        verify(doctorRepository, times(1)).findById(null);
    }

    @Test
    void getDoctorsByRole_WhenDoctorsExist_ShouldReturnDoctorsWithRole() {
        String role = "doctor";
        List<HospitalStaff> expectedDoctors = Arrays.asList(doctor1, doctor2, doctor3);
        when(doctorRepository.findByRole(role)).thenReturn(expectedDoctors);
        List<HospitalStaff> result = doctorService.getDoctorsByRole(role);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(doctor -> role.equals(doctor.getRole())));
        verify(doctorRepository, times(1)).findByRole(role);
    }

    @Test
    void getDoctorsByRole_WhenNoDoctorsWithRole_ShouldReturnEmptyList() {
        String role = "nurse";
        when(doctorRepository.findByRole(role)).thenReturn(Arrays.asList());
        List<HospitalStaff> result = doctorService.getDoctorsByRole(role);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(doctorRepository, times(1)).findByRole(role);
    }

    @Test
    void getDoctorsByRole_WithInvalidRole_ShouldReturnEmptyList() {
        String role = "invalidRole";
        when(doctorRepository.findByRole(role)).thenReturn(Arrays.asList());
        List<HospitalStaff> result = doctorService.getDoctorsByRole(role);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(doctorRepository, times(1)).findByRole(role);
    }

    @Test
    void getDoctorsByRole_WithNullRole_ShouldReturnEmptyList() {
        when(doctorRepository.findByRole(null)).thenReturn(Arrays.asList());
        List<HospitalStaff> result = doctorService.getDoctorsByRole(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(doctorRepository, times(1)).findByRole(null);
    }

    @Test
    void createDoctor_ShouldSaveAndReturnDoctor() {
        HospitalStaff newDoctor = new HospitalStaff(null, "Dr. New Doctor", "new.doctor@hospital.com", "doctor", "password123",
                "July 22 2025 5:51 PM", "", true);
        HospitalStaff savedDoctor = new HospitalStaff("5", "Dr. New Doctor", "new.doctor@hospital.com", "doctor", "password123",
                "July 22 2025 5:51 PM", "", true);
        when(doctorRepository.save(any(HospitalStaff.class))).thenReturn(savedDoctor);
        HospitalStaff result = doctorService.createDoctor(newDoctor);
        assertNotNull(result);
        assertEquals("5", result.getId());
        assertEquals("Dr. New Doctor", result.getName());
        assertEquals("new.doctor@hospital.com", result.getEmail());
        assertEquals("doctor", result.getRole());
        verify(doctorRepository, times(1)).save(newDoctor);
    }

    @Test
    void createDoctor_WithNullDoctor_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            doctorService.createDoctor(null);
        });
        verify(doctorRepository, never()).save(any());
    }

    @Test
    void updateDoctor_ShouldSetIdAndSaveDoctor() {
        String doctorId = "1";
        HospitalStaff updatedDoctorInput = new HospitalStaff("999", "Dr. Updated Name", "updated@hospital.com", "admin", "admin123",
                "July 22 2025 5:51 PM", "", true);
        HospitalStaff savedDoctor = new HospitalStaff("1", "Dr. Updated Name", "updated@hospital.com", "admin", "admin123",
                "July 22 2025 5:51 PM", "", true);
        when(doctorRepository.save(any(HospitalStaff.class))).thenReturn(savedDoctor);
        HospitalStaff result = doctorService.updateDoctor(doctorId, updatedDoctorInput);
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Dr. Updated Name", result.getName());
        assertEquals("updated@hospital.com", result.getEmail());
        assertEquals("admin", result.getRole());
        verify(doctorRepository, times(1)).save(updatedDoctorInput);
        assertEquals(doctorId, updatedDoctorInput.getId());
    }

    @Test
    void updateDoctor_WithDifferentOriginalId_ShouldOverwriteIdAndSave() {
        String doctorId = "1";
        HospitalStaff doctorWithDifferentId = new HospitalStaff("999", "Dr. Different", "different@hospital.com", "doctor",
                "password123", "July 22 2025 5:51 PM", "", true);
        HospitalStaff savedDoctor = new HospitalStaff("1", "Dr. Different", "different@hospital.com", "doctor", "password123",
                "July 22 2025 5:51 PM", "", true);
        when(doctorRepository.save(any(HospitalStaff.class))).thenReturn(savedDoctor);
        HospitalStaff result = doctorService.updateDoctor(doctorId, doctorWithDifferentId);
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Dr. Different", result.getName());
        verify(doctorRepository, times(1)).save(doctorWithDifferentId);
        assertEquals(doctorId, doctorWithDifferentId.getId());
    }

    @Test
    void updateDoctor_WithNullDoctor_ShouldThrowException() {
        String doctorId = "1";
        assertThrows(IllegalArgumentException.class, () -> {
            doctorService.updateDoctor(doctorId, null);
        });
        verify(doctorRepository, never()).save(any());
    }

    @Test
    void deleteDoctor_WithValidId_ShouldDeleteSuccessfully() {
        String doctorId = "1";
        doNothing().when(doctorRepository).deleteById(doctorId);
        assertDoesNotThrow(() -> {
            doctorService.deleteDoctor(doctorId);
        });
        verify(doctorRepository, times(1)).deleteById(doctorId);
    }

    @Test
    void deleteDoctor_WithNonExistentId_ShouldCompleteWithoutError() {
        String doctorId = "999";
        doNothing().when(doctorRepository).deleteById(doctorId);
        assertDoesNotThrow(() -> {
            doctorService.deleteDoctor(doctorId);
        });
        verify(doctorRepository, times(1)).deleteById(doctorId);
    }

    @Test
    void deleteDoctor_WithNullId_ShouldHandleGracefully() {
        doNothing().when(doctorRepository).deleteById(null);
        assertDoesNotThrow(() -> {
            doctorService.deleteDoctor(null);
        });
        verify(doctorRepository, times(1)).deleteById(null);
    }
}