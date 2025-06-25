package com.onixx.apolloveiculos.api.Domains.Fuels;

import com.onixx.apolloveiculos.api.Domains.Standard.Standard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "tb_fuel")
@SQLDelete(sql = "UPDATE tb_fuel SET dt_delete = CURRENT_TIMESTAMP WHERE id_fuel = ?")
@Where(clause = "dt_delete IS NULL")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Fuels extends Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fuel")
    private Long id_fuel;

    @Column(name = "name")
    private String name;

    public Fuels(String name) {
        this.name = name;
    }
}