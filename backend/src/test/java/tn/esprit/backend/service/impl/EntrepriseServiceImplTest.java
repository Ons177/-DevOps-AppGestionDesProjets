package tn.esprit.backend.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.repository.EntrepriseRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntrepriseServiceImplTest {

    @Mock
    EntrepriseRepository entrepriseRepository;

    @InjectMocks
    EntrepriseServiceImpl service;

    @Test
    void addEntreprise_shouldSaveAndReturn() {
        Entreprise e = mock(Entreprise.class);
        when(entrepriseRepository.save(e)).thenReturn(e);
        assertSame(e, service.addEntreprise(e));
        verify(entrepriseRepository).save(e);
    }

    @Test
    void updateEntreprise_shouldSaveAndReturn() {
        Entreprise e = mock(Entreprise.class);
        when(entrepriseRepository.save(e)).thenReturn(e);
        assertSame(e, service.updateEntreprise(e));
        verify(entrepriseRepository).save(e);
    }

    @Test
    void deleteEntreprise_shouldCallDeleteById() {
        service.deleteEntreprise(1L);
        verify(entrepriseRepository).deleteById(1L);
    }

    @Test
    void getEntrepriseById_whenFound_shouldReturnIt() {
        Entreprise e = mock(Entreprise.class);
        when(entrepriseRepository.findById(1L)).thenReturn(Optional.of(e));
        assertSame(e, service.getEntrepriseById(1L));
    }

    @Test
    void getEntrepriseById_whenMissing_shouldReturnNull() {
        when(entrepriseRepository.findById(99L)).thenReturn(Optional.empty());
        assertNull(service.getEntrepriseById(99L));
    }

    @Test
    void getAllEntreprises_shouldReturnList() {
        List<Entreprise> list = List.of(mock(Entreprise.class), mock(Entreprise.class));
        when(entrepriseRepository.findAll()).thenReturn(list);
        assertEquals(2, service.getAllEntreprises().size());
    }
}
