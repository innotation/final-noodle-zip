package noodlezip.community.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {}
