package vn.dev.managementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.dev.managementsystem.Entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM databasems_2207.tbl_user WHERE email = :username AND status = 1")
    public User getUserByName(@Param("username") String username);

    @Query(nativeQuery = true, value = "SELECT * FROM databasems_2207.tbl_user where status = 1 order by role;")
    public List<User> orderByRole();
}
