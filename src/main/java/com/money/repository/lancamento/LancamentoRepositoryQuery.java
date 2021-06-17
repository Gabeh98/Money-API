package com.money.repository.lancamento;

import com.money.model.Lancamento;
import com.money.repository.filter.LancamentoFilter;

import java.util.List;

public interface LancamentoRepositoryQuery {
    public List<Lancamento> filter(LancamentoFilter lancamentoFilter);
}
