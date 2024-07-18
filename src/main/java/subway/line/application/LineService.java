package subway.line.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.LineRequest;
import subway.line.application.dto.LineResponse;
import subway.line.application.dto.SectionRequest;
import subway.line.domain.entity.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.entity.LineSection;
import subway.line.domain.entity.LineSections;
import subway.line.exception.LineException;
import subway.line.exception.LineExceptionType;
import subway.station.application.dto.StationResponse;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.exception.StationException;
import subway.station.exception.StationExceptionType;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {

        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), new LineSections()));
        LineSection lineSection = new LineSection(findStationById(lineRequest.getUpStationId()), findStationById(lineRequest.getDownStationId()), lineRequest.getDistance());

        line.getLineSections().addSection(lineSection);

        List<StationResponse> stations = getStationResponsesByStationIds(line.getLineSections().getStationIds());

        return createLineResponse(line, stations);
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
            .map(this::createGroupedLineResponse)
            .collect(Collectors.toList());
    }

    public LineResponse findLine(Long lineId) {
        Line line = findLineById(lineId);

        List<Long> stationIds = line.getLineSections().getStationIds();
        List<StationResponse> stations = getStationResponsesByStationIds(stationIds);

        return createLineResponse(line, stations);
    }

    @Transactional
    public void updateLine(Long lineId, LineRequest lineRequest) {
        Line line = findLineById(lineId);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long lineId) {
        Line line = findLineById(lineId);
        lineRepository.delete(line);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(() -> new StationException(StationExceptionType.STATION_NOT_FOUND));
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId)
            .orElseThrow(() -> new LineException(LineExceptionType.LINE_NOT_FOUND));
    }

    private LineResponse createLineResponse(Line line, List<StationResponse> stations) {
        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            stations
        );
    }

    private LineResponse createGroupedLineResponse(Line line) {
        List<Long> stationIds = line.getLineSections().getStationIds();
        return createLineResponse(line, getStationResponsesByStationIds(stationIds));
    }

    private List<StationResponse> getStationResponsesByStationIds(Iterable<Long> stationIds) {
        Map<String, Station> stationMap = stationRepository.findAllById(stationIds).stream()
            .collect(Collectors.toMap(
                Station::getName,
                station -> station,
                (existing, replacement) -> existing
            ));

        return stationMap.values().stream()
            .map(station -> new StationResponse(station.getId(), station.getName()))
            .collect(Collectors.toList());
    }

}
