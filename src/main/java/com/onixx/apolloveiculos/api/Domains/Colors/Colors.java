    package com.onixx.apolloveiculos.api.Domains.Colors;


    import com.onixx.apolloveiculos.api.Domains.Standard.Standard;

    import lombok.NonNull;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.RequiredArgsConstructor;

    import org.hibernate.annotations.SQLDelete;
    import org.hibernate.annotations.Where;


    @Entity
    @Table(name="tb_colors")
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @RequiredArgsConstructor
    @SQLDelete(sql="UPDATE tb_colors SET dt_delete = CURRENT_TIMESTAMP WHERE id_colors = ?")
    @Where(clause = "dt_delete is NULL")
    public class Colors extends Standard {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id_color;
        @Column(name="name", nullable = false)
        @NonNull
        private String name;
    }
