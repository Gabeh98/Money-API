package com.money.resource;

import com.money.event.RecursoCriadoEvento;
import com.money.model.Categoria;
import com.money.repository.CategoriaRepository;
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
@RequestMapping("/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    public List<Categoria> List(){
        return categoriaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Categoria> Create (@Valid @RequestBody Categoria categoria, HttpServletResponse response){
       Categoria categoriaSalva = categoriaRepository.save(categoria);
        publisher.publishEvent(new RecursoCriadoEvento(this,response, categoria.getCodigo()));
       return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
    }

    @GetMapping("/{codigo}")
    public Categoria FindByCode(@PathVariable Long codigo){
        return this.categoriaRepository.findById(codigo).orElse(null);
    }
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByCode(@PathVariable Long codigo){
        this.categoriaRepository.deleteById(codigo);
    }
    @PutMapping("/{codigo}")
    public Categoria update (@PathVariable Long codigo, @RequestBody Categoria categoria){
        Categoria categoriaSalva = this.categoriaRepository.findById(codigo).orElseThrow(()-> new EmptyResultDataAccessException(1));
        BeanUtils.copyProperties(categoria,categoriaSalva,"codigo");
        return this.categoriaRepository.save(categoriaSalva);
    }
}
