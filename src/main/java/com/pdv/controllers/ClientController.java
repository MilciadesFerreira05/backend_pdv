package com.pdv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pdv.models.Client;
import com.pdv.services.ClientService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;


@RestController
@RequestMapping("/api/clients")
@Validated
public class ClientController {


    @Autowired
    private ClientService clientService;

    @GetMapping
    @PreAuthorize("hasAuthority('Client.active')")
    public List<Client> getActive() {
        return clientService.findActive();
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('Client.all')")
    public List<Client> getAll() {
        return clientService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Client.create')")
    public ResponseEntity<Client> create(@RequestBody Client client) {        
        Client savedClient = clientService.save(client);
        return ResponseEntity.ok(savedClient);
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
    public ResponseEntity<Client> updateClient(@PathVariable @Positive Long id,
                                              @Valid @RequestBody Client updatedClient) {
        return clientService.findById(id)
                .map(client -> {
                    client.setName(updatedClient.getName());
                    client.setAddress(updatedClient.getAddress());
                    Client savedClient = clientService.save(client);
                    return ResponseEntity.ok(savedClient);
                })
                .orElse(ResponseEntity.notFound().build());
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

