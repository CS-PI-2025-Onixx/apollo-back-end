package com.onixx.apolloveiculos.api.Domains.User;

import com.onixx.apolloveiculos.api.Domains.Standard.Standard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email = "";

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoles role= UserRoles.ROLE_USER;

    @Column(name="full_name")
    private String fullName;

    @Column(name = "cep")
    private String cep;

    @Column(name="state")
    private String state;

    @Column(name="city")
    private String city;

    @Column(name="cellphone")
    private String cellphone;

    @Column(name = "date_birth")
    private String dateBirth;

    @Column(name="gender")
    private String gender;

    public User(String name, String email, String password, UserRoles role, String fullName, String cellphone) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.fullName = fullName;
        this.cellphone = cellphone;
        this.password = password;
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