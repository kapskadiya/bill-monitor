package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public interface UserRepository extends PagingAndSortingRepository<User, String>, UserCustomRepository {

    List<User> findByFirstname(final String firstname);

    List<User> findByLastname(final String lastname);

    List<User> findByEmailAndIsDeleted(String email, boolean isDeleted);

    List<User> findByFirstnameContainingOrLastnameContainingOrEmailContaining(String firstname, String lastname, String email);

    List<User> findByIsDeleted(boolean isDeleted);

}
