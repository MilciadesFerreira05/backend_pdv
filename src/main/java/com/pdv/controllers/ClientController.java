package com.pdv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pdv.models.Client;
import com.pdv.services.ClientService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;



@RestController
@RequestMapping("/api/clients")
@Validated
public class ClientController {


    @Autowired
    private ClientService clientService;

    @GetMapping
    @PreAuthorize("hasAuthority('Client.active')")
    public Page<Client> getActive(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction),sort));
        return clientService.findActive(pageable);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('Client.all')")
    public Page<Client> getAll(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));
        return clientService.findAll(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Client.create')")
    public ResponseEntity<Client> create(@RequestBody Client client) {        
        Client savedClient = clientService.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Client.read')")
    public ResponseEntity<Client> getById(@PathVariable @Positive Long id) {
        return clientService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Client.update')")
    public ResponseEntity<Client> updateClient(@PathVariable @Positive Long id, @Valid @RequestBody Client client) {
        return ResponseEntity.ok(clientService.update(client, id));

    }
    

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Client.delete')")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        return clientService.findById(id)
                .map(client -> {
                    clientService.delete(client);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

