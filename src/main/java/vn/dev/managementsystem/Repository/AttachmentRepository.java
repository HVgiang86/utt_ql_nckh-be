package vn.dev.managementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dev.managementsystem.Entity.Document;

@Repository
public interface AttachmentRepository extends JpaRepository<Document, Integer> {
}
