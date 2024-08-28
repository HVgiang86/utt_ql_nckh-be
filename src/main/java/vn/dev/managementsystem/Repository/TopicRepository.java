package vn.dev.managementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.dev.managementsystem.Entity.Topic;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM databasems_2207.tbl_topic order by current_state;")
    public List<Topic> getOrderByStatus();

    @Query(nativeQuery = true, value = "SELECT * FROM databasems_2207.tbl_topic order by lecturer_id;")
    public List<Topic> getOrderByLecturer();

    @Query(nativeQuery = true, value = "SELECT * FROM databasems_2207.tbl_topic order by create_date;")
    public List<Topic> getOrderByCreateDate();
}
