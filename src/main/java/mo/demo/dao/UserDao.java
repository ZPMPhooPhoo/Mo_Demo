package mo.demo.dao;

import mo.demo.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    
    Optional<User> findByEmail(String email);
    
    User save(User user);
    
    Optional<User> findById(UUID id);
    
    void deleteById(UUID id);
}
