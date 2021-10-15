package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, String>, UserCustomRepository {

    List<User> findByFirstname(final String firstname);

    List<User> findByLastname(final String lastname);

    List<User> findByEmailAndIsDeleted(String email, boolean isDeleted);

    List<User> findByFirstnameOrLastnameOrEmail(String firstname, String lastname, String email);

    List<User> findByIsDeleted(boolean isDeleted);

}
