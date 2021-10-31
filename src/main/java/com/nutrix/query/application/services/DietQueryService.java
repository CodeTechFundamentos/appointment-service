package com.nutrix.query.application.services;

import com.nutrix.command.domain.Diet;
import com.nutrix.command.infra.IDietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DietQueryService extends Diet {

    @Autowired
    private IDietRepository dietRepository;

    @Override
    public List<Diet> getAll() throws Exception {
        return dietRepository.findAll();
    }

    @Override
    public Optional<Diet> getById(Integer id) throws Exception {
        return dietRepository.findById(id);
    }
}
