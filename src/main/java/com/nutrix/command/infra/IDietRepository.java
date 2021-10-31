package com.nutrix.command.infra;

import com.nutrix.command.domain.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IDietRepository extends JpaRepository<Diet, Integer> {
}
