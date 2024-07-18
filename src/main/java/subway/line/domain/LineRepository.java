package subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.domain.entity.Line;

public interface LineRepository extends JpaRepository<Line, Long> {

}