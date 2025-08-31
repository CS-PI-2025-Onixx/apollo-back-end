package com.onixx.apolloveiculos.api.Domains.Images;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Standard.Standard;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_car", nullable = false)
    @JsonIgnore
    @JsonBackReference
    private Cars car;
}
