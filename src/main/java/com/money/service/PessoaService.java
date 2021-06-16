package com.money.service;

import com.money.model.Pessoa;
import com.money.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa findPessoaByCode(Long codigo) {
        return this.pessoaRepository.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }
}
