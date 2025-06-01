package com.onixx.apolloveiculos.api.Domains.Direction;

import com.onixx.apolloveiculos.api.Domains.Standard.Standard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="tb_direction")
@NoArgsConstructor
@AllArgsConstructor
@Data
@SQLDelete(sql="UPDATE tb_direction SET dt_delete = CURRENT_TIMESTAMP WHERE id_direction = ?")
@Where(clause = "dt_delete is NULL")
public class Direction extends Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_direction;
    @Column(name="name")
    private String name;
}
