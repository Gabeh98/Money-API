package com.money.resource;

import com.money.event.RecursoCriadoEvento;
import com.money.model.Lancamento;
import com.money.repository.LancamentoRepository;
import com.money.repository.filter.LancamentoFilter;
import com.money.service.LancamentoService;
import com.money.service.exception.PessoaInactiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
    @Autowired
    LancamentoRepository lancamentoRepository;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private LancamentoService lancamentoService;

    @GetMapping
    public Page<Lancamento> filter(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoRepository.filter(lancamentoFilter, pageable);
    }

    @GetMapping("/{codigo}")
    public Lancamento findById(@PathVariable Long codigo) {
        return this.lancamentoRepository.findById(codigo).orElse(null);
    }

    @PostMapping
    public ResponseEntity<Lancamento> create(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
        Lancamento lancamentoSalvo = lancamentoService.save(lancamento);
        publisher.publishEvent(new RecursoCriadoEvento(this, response, lancamento.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByCode(@PathVariable Long codigo) {
        this.lancamentoRepository.deleteById(codigo);
    }
    @ExceptionHandler({PessoaInactiveException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlePessoaInactiveException(RuntimeException ex) {
    }
}
