package com.onixx.apolloveiculos.api.Domains.User;

import com.onixx.apolloveiculos.api.Domains.Standard;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_user")
@SQLDelete(sql = "UPDATE tb_user SET dt_delete = CURRENT_TIMESTAMP WHERE id_user = ?")
@Where(clause = "dt_delete IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends Standard implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id_user;

    @Column(name = "name")
    @NotNull
    @NotBlank(message = "O nome não pode ser vazio")
    private String name;

    @Column(name = "email")
    @NotBlank(message = "O email não pode ser vazio")
    private String email = "";

    @Column(name = "password")
    @NotNull
    @NotBlank(message = "A senha não pode ser vazio")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRoles role= UserRoles.ROLE_USER;

    public User(String name, String email, String password, UserRoles role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRoles.ROLE_ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER")) ;
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return name;
    }


}