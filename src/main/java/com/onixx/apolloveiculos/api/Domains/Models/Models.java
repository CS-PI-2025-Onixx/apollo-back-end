package com.onixx.apolloveiculos.api.Domains.Models;

import com.onixx.apolloveiculos.api.Domains.Marks.Mark;
import com.onixx.apolloveiculos.api.Domains.Standard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name="tb_models")
@SQLDelete(sql="UPDATE tb_models SET dt_delete = CURRENT_TIMESTAMP WHERE id_model = ?")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Models extends Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_model;
    @Column(name="name")
    private String name;
    @OneToOne
    @JoinColumn(name="id_mark")
    private Mark mark;
}
