package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, String>, UserCustomRepository {

    Optional<User> findByUsername(final String username);

    List<User> findByFirstname(final String firstname);

    List<User> findByLastname(final String lastname);

}
