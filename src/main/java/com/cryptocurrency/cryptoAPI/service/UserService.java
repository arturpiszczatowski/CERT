package com.cryptocurrency.cryptoAPI.service;

import com.cryptocurrency.cryptoAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends CrudService<User>{
    public UserService(JpaRepository<User, Long> repository) {
        super(repository);
    }

    @Override
    public User createOrUpdate(User updateEntity) {
        return null;
    }
}
