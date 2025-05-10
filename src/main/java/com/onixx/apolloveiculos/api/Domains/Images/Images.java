package com.onixx.apolloveiculos.api.Domains.Images;

import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Standard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name="tb_images")
@NoArgsConstructor
@AllArgsConstructor
@Data
@SQLDelete(sql="UPDATE tb_images SET dt_delete = CURRENT_TIMESTAMP WHERE id_image = ?")
public class Images extends Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_image;
    @Column(name = "img_url")
    private String img_url;
    @OneToOne
    @JoinColumn(name = "id_car")
    private Cars car;
}
