package subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.domain.entity.Line;
import subway.line.exception.LineException;
import subway.line.exception.LineExceptionType;

public interface LineRepository extends JpaRepository<Line, Long> {

    default Line findByIdOrThrow(Long id) {
        return findById(id)
            .orElseThrow(() -> new LineException(LineExceptionType.LINE_NOT_FOUND));
    }
}