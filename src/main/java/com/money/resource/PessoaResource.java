package com.money.resource;
import com.money.event.RecursoCriadoEvento;
import com.money.model.Pessoa;
import com.money.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    public List<Pessoa> List(){
        return pessoaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Pessoa> Create(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response){
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);
        publisher.publishEvent(new RecursoCriadoEvento(this,response, pessoa.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @GetMapping("/{codigo}")
    public Pessoa findByCode(@PathVariable Long codigo){
        return this.pessoaRepository.findById(codigo).orElse(null);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByCode(@PathVariable Long codigo){
        this.pessoaRepository.deleteById(codigo);
    }
    @PutMapping("{codigo}")
    public Pessoa update(@PathVariable Long codigo, @RequestBody Pessoa pessoa) {
        Pessoa pessoaSalva = this.pessoaRepository.findById(codigo)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
        return this.pessoaRepository.save(pessoaSalva);
    }
}
