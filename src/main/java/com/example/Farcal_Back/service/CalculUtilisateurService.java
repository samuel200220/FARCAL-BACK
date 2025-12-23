package com.example.Farcal_Back.service;

import com.example.Farcal_Back.model.CalculUtilisateur;
import com.example.Farcal_Back.repository.CalculUtilisateurRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Service
public class CalculUtilisateurService {

    private final CalculUtilisateurRepository repository;

    public CalculUtilisateurService(CalculUtilisateurRepository repository) {
        this.repository = repository;
    }

    /**
     * 📜 Historique des calculs d’un utilisateur
     */
    public Flux<CalculUtilisateur> getHistoriqueUtilisateur(UUID idUtilisateur) {
        return repository.findByIdUtilisateur(idUtilisateur);
    }
}
