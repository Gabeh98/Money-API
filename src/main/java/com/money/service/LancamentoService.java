package com.money.service;

import com.money.model.Lancamento;
import com.money.model.Pessoa;
import com.money.repository.LancamentoRepository;
import com.money.repository.PessoaRepository;
import com.money.service.exception.PessoaInactiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LancamentoService {
    @Autowired
    private PessoaRepository pessoaService;
    @Autowired
    private LancamentoRepository lancamentoRepository;

    public Lancamento save(Lancamento lancamento) {
        Pessoa pessoa = pessoaService.findById(lancamento.getPessoa().getCodigo()).orElse(null);
        if (pessoa == null || pessoa.isInactive()) {
            throw new PessoaInactiveException();
        }
        return lancamentoRepository.save(lancamento);
    }
}
