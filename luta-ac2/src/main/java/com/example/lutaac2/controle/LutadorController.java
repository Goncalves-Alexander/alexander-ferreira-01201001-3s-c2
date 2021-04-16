package com.example.lutaac2.controle;

import com.example.lutaac2.dominio.Lutador;
import com.example.lutaac2.repositorio.LutadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lutadores")
public class LutadorController {

    @Autowired
    private LutadorRepository repository;

    @GetMapping
    public ResponseEntity getLutadores(){
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/vivos")
    public ResponseEntity getLutadoresVivos(){
        return ResponseEntity.ok(repository.findByAlive());
    }

    @GetMapping("/mortos")
    public ResponseEntity getLutadoresMortos(){
        return ResponseEntity.ok(repository.findByDeath());
    }

    @PostMapping("/{id}/concentrar")
    public ResponseEntity postConcentracao(@PathVariable Integer id){

        if(repository.getOne(id).getConcentracoesRealizadas() > 3){
            return ResponseEntity.status(400).body("Lutador já se concentrou 3 vezes!");
        }else if(id > repository.count()){
            return ResponseEntity.status(400).build();
        }else{
            repository.getOne(id).setVida(repository.getOne(id).getVida()*1.15);
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/golpe")
    public ResponseEntity postGolpe(@RequestBody Integer idLutadorBate, @RequestBody Integer idLutadorApanha){
        if(idLutadorBate > repository.count() || idLutadorBate < 0
                || idLutadorApanha > repository.count() || idLutadorApanha < 0){
            return ResponseEntity.status(400).build();
        }else if(!repository.getOne(idLutadorBate).getVivo() || !repository.getOne(idLutadorApanha).getVivo()){
            return ResponseEntity.status(400).body("Ambos os lutadores devem estar vivos!");
        }else if(repository.getOne(idLutadorApanha).getVida() <
                repository.getOne(idLutadorBate).getForcaGolpe()){
            repository.getOne(idLutadorApanha).setVida(0.0);
            repository.getOne(idLutadorApanha).setVivo(false);
            return ResponseEntity.ok().body("Lutador morto");
        }else{
            repository.getOne(idLutadorApanha).setVida(
                    repository.getOne(idLutadorApanha).getVida() -
                            repository.getOne(idLutadorBate).getForcaGolpe());
            return ResponseEntity.status(201).body(repository.findById(idLutadorBate));
        }
    }

    @PostMapping
    public ResponseEntity postLutador(@RequestBody Lutador lutador){
        repository.save(lutador);
        return ResponseEntity.status(201).build();
    }
}
