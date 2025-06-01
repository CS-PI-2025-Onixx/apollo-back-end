package com.onixx.apolloveiculos.api.Domains.Marks;

import com.onixx.apolloveiculos.api.Domains.Standard.Standard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="tb_mark")
@NoArgsConstructor
@AllArgsConstructor
@Data
@SQLDelete(sql="UPDATE tb_mark SET dt_delete = CURRENT_TIMESTAMP WHERE id_mark = ?")
@Where(clause = "dt_delete is NULL")
public class Mark extends Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_mark;
    @Column(name="name")
    private String name;
}
