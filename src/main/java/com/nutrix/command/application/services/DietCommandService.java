package com.nutrix.command.application.services;

import com.nutrix.command.domain.Diet;
import com.nutrix.command.infra.IDietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DietCommandService extends Diet {

    @Autowired
    private IDietRepository dietRepository;

    @Override
    @Transactional
    public Diet save(Diet diet) throws Exception {
        return dietRepository.save(diet);
    }

    @Override
    @Transactional
    public void delete(Integer id) throws Exception {
        dietRepository.deleteById(id);
    }
}
