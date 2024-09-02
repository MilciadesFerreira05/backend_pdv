package com.pdv.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pdv.models.Auditable;
import com.pdv.models.Category;
import com.pdv.models.Log;
import com.pdv.models.User;
import com.pdv.repositories.BaseRepository;

@Service
public abstract class BaseService<T> {

    @Autowired
    protected UserInfoService userInfoService;

    @Autowired
    private LogService logService;

    @Autowired
    protected BaseRepository<T, Long> repository;

    public Page<T> findAll(Pageable pageable) {
       return repository.findAll(pageable);
    }

    public Page<T> findActive(Pageable pageable) {
        return repository.findByDeletedAtIsNull(pageable); 
    }

    public T save(T t) {

        User user = userInfoService.getCurrentUser();
        
        ((Auditable) t).setCreatedBy(user);

        T tSaved = repository.save(t);

        log("create", tSaved.toString(), null, user);
        
        return t;
    }

    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    public abstract T update(T t, Long id);

    public void delete(T t) {
        User user = userInfoService.getCurrentUser();
        ((Auditable) t).setDeletedBy(user);

        log("delete", null, t.toString(), user );

        repository.save(t);
    }

    public void log(String action, String newData, String oldData, User user) {
        new Thread(()->{
            Log log = new Log();
            log.setAction(action);
            log.setDate(LocalDateTime.now());
            log.setNewData(newData);
            log.setOldData(oldData);
            log.setUser(user);
            logService.save(log);
        }).start();
    }

}
