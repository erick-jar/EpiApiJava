package com.senac.api_epi.repositories;

import com.senac.api_epi.entities.TreinamentoRealizado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreinamentoRealizadoRepository extends JpaRepository<TreinamentoRealizado, Long> {

    // Treinamentos que um funcionário já concluiu (mais recente primeiro)
    List<TreinamentoRealizado> findByFuncionarioIdOrderByDataConclusaoDesc(
            Long funcionarioId);

    // Este funcionário já concluiu este treinamento?
    boolean existsByFuncionarioIdAndTreinamentoId(
            Long funcionarioId,
            Long treinamentoId);
}
