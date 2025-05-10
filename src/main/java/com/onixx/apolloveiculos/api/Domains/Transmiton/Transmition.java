package com.onixx.apolloveiculos.api.Domains.Transmiton;

import com.onixx.apolloveiculos.api.Domains.Standard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="tb_transmition")
@SQLDelete(sql = "UPDATE tb_transmition SET dt_delete CURRENT_TIMESTAP WHERE id_transmition = ? ")
@Where(clause = "dt_delete is NULL")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transmition extends Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_transmition;
    @Column(name="name")
    private String name;
}