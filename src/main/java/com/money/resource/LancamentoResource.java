package com.money.resource;

import com.money.event.RecursoCriadoEvento;
import com.money.model.Lancamento;
import com.money.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
    @GetMapping
    public List<Lancamento> findAll (){
        return lancamentoRepository.findAll();
    }
    @GetMapping("/{codigo}")
    public Lancamento findById(@PathVariable Long codigo){
        return this.lancamentoRepository.findById(codigo).orElse(null);
    }
    @PostMapping
    public ResponseEntity<Lancamento> create(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response){
        Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
        publisher.publishEvent(new RecursoCriadoEvento(this,response,lancamento.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
    }
}
