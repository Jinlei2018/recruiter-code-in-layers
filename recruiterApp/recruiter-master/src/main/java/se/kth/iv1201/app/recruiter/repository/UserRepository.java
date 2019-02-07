package se.kth.iv1201.app.recruiter.repository;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se\kth.iv1201.app.recruiter.domain.User;
import java.util.List;

/**
 * Contains all database access concerning users.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY) // Applies only to methods explicitly declared in this interface,
// not to those inherited from JpaRepository. This is quite dangerous, there will be no error if an inherited method is
// called, but default transaction configuration will be used instead of what is declared here.
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsernameAndPassword(String username, String password);

    @Override
    Set<User> findAll();

    @Override
    User save(User user);
}
