package com.money.repository.lancamento;

import com.money.model.Lancamento;
import com.money.repository.filter.LancamentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LancamentoRepositoryQuery {
    public Page<Lancamento> filter(LancamentoFilter lancamentoFilter, Pageable pageable);
}
