package com.pdv.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdv.models.Client;
import com.pdv.repositories.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserInfoService userInfoService;

    public List<Client> findAll() {
       return clientRepository.findAll();
    }

    public List<Client> findActive() {
        return clientRepository.findByDeletedAtIsNull(); 
    }

    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public Client save(Client client) {
        client.setCreatedBy(userInfoService.getCurrentUser());
        return clientRepository.save(client);
    }

    public Client update(Client client, Long id) {
        Client currentClient = clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
        currentClient.setName(client.getName());
        currentClient.setRuc(client.getRuc());
        currentClient.setContactName(client.getContactName());
        currentClient.setEmail(client.getEmail());
        currentClient.setPhone(client.getPhone());
        currentClient.setAddress(client.getAddress());
        currentClient.setUpdatedBy(userInfoService.getCurrentUser());
        return clientRepository.save(currentClient);
    }
    public void delete(Client client) {
        client.setDeletedBy(userInfoService.getCurrentUser());
        clientRepository.save(client);
    }

}

