package com.onixx.apolloveiculos.api.Domains.Motors;

import com.onixx.apolloveiculos.api.Domains.Standard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="tb_motors")
@NoArgsConstructor
@AllArgsConstructor
@Data
@SQLDelete(sql="UPDATE tb_motors SET dt_delete = CURRENT_TIMESTAMP WHERE id_motors = ?")
@Where(clause = "dt_delete is NULL")
public class Motors extends Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_motors;
    @Column(name="name")
    private String name;
}