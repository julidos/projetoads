package com.projeto.ads.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.ads.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Role findByNome(String role);   
}
