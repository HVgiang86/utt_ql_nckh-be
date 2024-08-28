package vn.dev.managementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dev.managementsystem.Entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
}
