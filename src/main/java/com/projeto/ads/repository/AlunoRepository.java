package com.projeto.ads.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import com.projeto.ads.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
	
	public Aluno findByCpf(String Cpf);
	
	@Query("SELECT a FROM Aluno a WHERE a.id=(SELECT Max(a2.id) FROM Aluno a2)")
	public Aluno findLastInsertedAluno();
	
	@Query("SELECT a FROM Aluno a ORDER BY a.id")
	public List<Aluno> findAllOrderedById();

}
