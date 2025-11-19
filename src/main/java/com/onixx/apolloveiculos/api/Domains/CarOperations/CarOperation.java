package com.onixx.apolloveiculos.api.Domains.CarOperations;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Standard.Standard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_car_operation")
@SQLDelete(sql = "UPDATE tb_car_operation SET dt_delete = CURRENT_TIMESTAMP WHERE id_car = ?")
@Where(clause = "dt_delete IS NULL")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class CarOperation extends Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nomeCliente")
    private String nomeCliente;

    @Column(name = "dataOperacao")
    private LocalDateTime dataOperacao;

    @Column(name = "valor")
    private double valor;

    @Column(name="dataDevolucao")
    private LocalDateTime dataDevolucao;

    @Column(name = "tipoOperacao")
    @Comment("0 - Venda, 1 - Aluguel")
    private int tipoOperacao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_car", nullable = false)
    @JsonIgnore
    @JsonBackReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Cars car;
}
