package pl.gromada.vaadin_project_yerba.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.gromada.vaadin_project_yerba.backend.model.User;
import pl.gromada.vaadin_project_yerba.backend.model.UserYerba;
import pl.gromada.vaadin_project_yerba.backend.repo.UserYerbaRepository;

import java.util.List;

@Service
public class UserYerbaService {

    private UserYerbaRepository userYerbaRepository;

    @Autowired
    public UserYerbaService(UserYerbaRepository userYerbaRepository) {
        this.userYerbaRepository = userYerbaRepository;
    }

    public List<UserYerba> findAllUserYerba(User user) {
        return userYerbaRepository.findAllByUser(user);
    }

    public void saveUserYerba(UserYerba userYerba) {
        userYerbaRepository.save(userYerba);
    }

    public void deleteUserYerba(UserYerba userYerba) {
        userYerbaRepository.delete(userYerba);
    }
}
