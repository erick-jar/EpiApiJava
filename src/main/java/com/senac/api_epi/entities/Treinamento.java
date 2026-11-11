package com.senac.api_epi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Treinamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @Column(length = 200)
    private String arquivoUrl;

    // ATRIBUTO ADICIONADO PARA O DESAFIO EXTRA
    // Pode ser nulo se o treinamento nunca expirar
    @Column(name = "validade_meses")
    private Integer validadeMeses;
}
