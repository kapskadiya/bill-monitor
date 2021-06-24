package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends ElasticsearchRepository<User, String>, UserESRepository {

    @Override
    User save(final User entity);

    @Override
    Optional<User> findById(final String id);

    Optional<User> findByUsername(final String username);

    List<User> findByFirstname(final String firstname);

    List<User> findByLastname(final String lastname);

    @Override
    void deleteById(String username);

}
