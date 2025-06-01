package com.onixx.apolloveiculos.api.Domains.Traction;

import com.onixx.apolloveiculos.api.Domains.Standard.Standard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "tb_traction")
@SQLDelete(sql = "UPDATE tb_traction SET dt_delete = CURRENT_TIMESTAMP WHERE id_traction = ?")
@Where(clause = "dt_delete IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Traction extends Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_traction")
    private Long id_traction;

    @Column(name = "name")
    private String name;
}