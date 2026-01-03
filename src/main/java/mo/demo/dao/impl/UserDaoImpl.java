package mo.demo.dao.impl;

import lombok.RequiredArgsConstructor;
import mo.demo.dao.UserDao;
import mo.demo.model.User;
import mo.demo.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    
    private final UserRepository userRepository;
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    
    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
    
    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
}
