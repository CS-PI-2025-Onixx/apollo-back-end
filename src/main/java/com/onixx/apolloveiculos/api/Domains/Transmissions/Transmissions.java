package com.onixx.apolloveiculos.api.Domains.Transmissions;

import com.onixx.apolloveiculos.api.Domains.Standard.Standard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="tb_transmissions")
@SQLDelete(sql = "UPDATE tb_transmissions SET dt_delete CURRENT_TIMESTAP WHERE id_transmission = ? ")
@Where(clause = "dt_delete is NULL")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transmissions extends Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_transmission;
    @Column(name="name")
    private String name;
}