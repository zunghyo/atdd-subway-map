package subway.station.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.station.exception.StationException;
import subway.station.exception.StationExceptionType;

public interface StationRepository extends JpaRepository<Station, Long> {

    default Station findByIdOrThrow(Long id) {
        return findById(id)
            .orElseThrow(() -> new StationException(StationExceptionType.STATION_NOT_FOUND));
    }
}