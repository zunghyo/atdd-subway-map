package subway.line.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.SectionRequest;
import subway.line.domain.LineRepository;
import subway.line.domain.entity.Line;
import subway.line.domain.entity.LineSection;
import subway.line.exception.LineException;
import subway.line.exception.LineExceptionType;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.exception.StationException;
import subway.station.exception.StationExceptionType;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class LineSectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public void saveSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findByIdOrThrow(lineId);

        Station upStation = stationRepository.findByIdOrThrow(sectionRequest.getUpStationId());
        Station downStation = stationRepository.findByIdOrThrow(sectionRequest.getDownStationId());

        line.addSection(new LineSection(upStation, downStation, sectionRequest.getDistance()));
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findByIdOrThrow(lineId);
        Station station = stationRepository.findByIdOrThrow(stationId);

        line.deleteSection(station.getId());
    }

}
