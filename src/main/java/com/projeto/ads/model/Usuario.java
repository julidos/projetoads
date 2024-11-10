package com.projeto.ads.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal; //datas
import javax.persistence.TemporalType;

import javax.persistence.JoinColumn;

@Entity
@Table(name = "usuarios")
public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true)
        private String email;

        private String password;

        @Column(unique = true)
        private String username;

        @Temporal(TemporalType.DATE)
        private Date dataNascimento;

        private boolean ativo;
        private String token;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "role_id")
        private Role role;

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public Date getDataNascimento() {
                return dataNascimento;
        }

        public void setDataNascimento(Date dataNascimento) {
                this.dataNascimento = dataNascimento;
        }

        public boolean isAtivo() {
                return ativo;
        }

        public void setAtivo(boolean ativo) {
                this.ativo = ativo;
        }

        public String getToken() {
                return token;
        }

        public void setToken(String token) {
                this.token = token;
        }

        public Role getRole() {
                return role;
        }

        public void setRole(Role role) {
                this.role = role;
        }

        @Override
        public int hashCode() {
                return Objects.hash(id);
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                Usuario other = (Usuario) obj;
                return Objects.equals(id, other.id);
        }

}