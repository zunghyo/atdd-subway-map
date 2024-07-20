package subway.line.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.LineRequest;
import subway.line.application.dto.LineResponse;
import subway.line.domain.entity.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.entity.LineSection;
import subway.line.domain.entity.LineSections;
import subway.station.application.dto.StationResponse;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {

        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), new LineSections()));
        LineSection lineSection = new LineSection(line, stationRepository.findByIdOrThrow(lineRequest.getUpStationId()), stationRepository.findByIdOrThrow(lineRequest.getDownStationId()), lineRequest.getDistance());

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
        Line line = lineRepository.findByIdOrThrow(lineId);

        List<Long> stationIds = line.getLineSections().getStationIds();
        List<StationResponse> stations = getStationResponsesByStationIds(stationIds);

        return createLineResponse(line, stations);
    }

    @Transactional
    public void updateLine(Long lineId, LineRequest lineRequest) {
        Line line = lineRepository.findByIdOrThrow(lineId);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long lineId) {
        Line line = lineRepository.findByIdOrThrow(lineId);
        lineRepository.delete(line);
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
