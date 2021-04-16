package com.example.lutaac2.repositorio;

import com.example.lutaac2.dominio.Lutador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LutadorRepository extends JpaRepository<Lutador, Integer> {
    @Query("select count(l.nome) from Lutador l where l.vivo = true")
    List<Lutador> findByAlive();

    @Query("select count(l.nome) from Lutador l where l.vida = 0.0")
    List<Lutador> findByDeath();
}
