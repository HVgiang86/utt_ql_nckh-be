package vn.dev.managementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dev.managementsystem.Entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
}
