package com.onixx.apolloveiculos.api.Domains.Bodywork;

import com.onixx.apolloveiculos.api.Domains.Standard.Standard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "tb_bodywork")
@SQLDelete(sql="UPDATE tb_bodywork SET dt_delete = CURRENT_TIMESTAMP WHERE id_bodywork = ?")
@Where(clause = "dt_delete is NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Bodywork extends Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_bodywork;
    @Column(name="name")
    private String name;
}
