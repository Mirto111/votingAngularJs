package myProject.voting.service;

import myProject.voting.AuthorizedUser;
import myProject.voting.model.User;
import myProject.voting.repository.datajpa.CrudUserRepository;
import myProject.voting.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    private static final Sort SORT_NAME_EMAIL = Sort.by("name", "email");

    @Autowired
    private CrudUserRepository crudRepository;

    @Override
    public User save(User user) {
        return crudRepository.save(user);
    }

    @Override
    public boolean delete(int id) {
        get(id);
        return crudRepository.delete(id) != 0;
    }

    @Override
    public User get(int id) {
        return crudRepository.findById(id).orElseThrow(()->new NotFoundException("User with id="+ id+" not found"));
    }

    @Override
    public Collection<User> getAll() {
        return crudRepository.findAll(SORT_NAME_EMAIL);
    }

    @Override
    public User getByEmail(String email) {
        return Optional.ofNullable(crudRepository.getByEmail(email)).orElseThrow(()->new NotFoundException("User with email="+ email+" not found"));
    }

    @Override
    public AuthorizedUser loadUserByUsername(String s) throws UsernameNotFoundException {
        User u = crudRepository.getByEmail(s.toLowerCase());
        if (u == null) {
            throw new UsernameNotFoundException("User " + s + " is not found");
        }
        return new AuthorizedUser(u);
    }
}
