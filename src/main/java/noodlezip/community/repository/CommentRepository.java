
package noodlezip.community.repository;

import noodlezip.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {}
