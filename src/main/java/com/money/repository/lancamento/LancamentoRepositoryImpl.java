package com.money.repository.lancamento;

import com.money.model.Lancamento;
import com.money.repository.filter.LancamentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Lancamento> filter(LancamentoFilter lancamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicates = createRestrictions(lancamentoFilter, builder, root);
        criteria.where(predicates);
        TypedQuery<Lancamento> query = manager.createQuery(criteria);
        restricoesDePaginacao(query,pageable);
        return new PageImpl<>(query.getResultList(),pageable,total(lancamentoFilter));
    }
    private Predicate[] createRestrictions(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (lancamentoFilter.getDescricao() != null) {
            predicates.add(builder.like(builder.lower(root.get("descricao")), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
        }
        if (lancamentoFilter.getDataVencimentoDe() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataVencimento"),lancamentoFilter.getDataVencimentoDe()));
        }
        if (lancamentoFilter.getDataVencimentoAte() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("dataVencimento"),lancamentoFilter.getDataVencimentoAte()));
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }
    private void restricoesDePaginacao(TypedQuery<Lancamento> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistroPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistroPorPagina;
        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistroPorPagina);
    }
    private Long total(LancamentoFilter lancamentoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long>criteria = builder.createQuery(Long.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);
        Predicate[] predicates = createRestrictions(lancamentoFilter,builder,root);
        criteria.where(predicates);
        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
