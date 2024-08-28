package vn.dev.managementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.dev.managementsystem.Entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(nativeQuery = true, value = "select * from databasems_2207.tbl_role where name=:name")
    public Role getRoleByName(@Param("name") String roleName);
}
