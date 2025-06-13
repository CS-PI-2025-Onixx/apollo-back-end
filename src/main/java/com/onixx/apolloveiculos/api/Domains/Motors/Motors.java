package com.onixx.apolloveiculos.api.Domains.Motors;

import com.onixx.apolloveiculos.api.Domains.Standard.Standard;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="tb_motors")
@NoArgsConstructor
@AllArgsConstructor
@Data
@RequiredArgsConstructor
@SQLDelete(sql="UPDATE tb_motors SET dt_delete = CURRENT_TIMESTAMP, status='deactive' WHERE id_motor = ?")
@Where(clause = "dt_delete is NULL")

public class Motors extends Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_motor;
    @Column(name="name", nullable = false)
    @NonNull
    private String name;

}