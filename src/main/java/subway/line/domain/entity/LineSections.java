package subway.line.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.common.exception.SubwayException;
import subway.common.exception.SubwayExceptionType;
import subway.line.exception.InvalidDownStationException;
import subway.line.exception.InvalidUpStationException;
import subway.station.domain.Station;

@Embeddable
@Getter
@NoArgsConstructor
public class LineSections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<LineSection> lineSections = new ArrayList<>();

    public void addSection(LineSection lineSection) {

        validateUpStation(lineSection.getUpStation());
        validateDownStation(lineSection.getDownStation());

        lineSections.add(lineSection);
    }

    public void deleteSection(Long stationId) {
        if(lineSections.size() <= 1) {
            throw new SubwayException(SubwayExceptionType.CANNOT_DELETE_SINGLE_SECTION);
        }
        Optional<Station> lastDownStation = getLastDownStation();
        if(lastDownStation.isPresent() && !lastDownStation.get().getId().equals(stationId)) {
            throw new SubwayException(SubwayExceptionType.CANNOT_DELETE_NON_LAST_DOWN_STATION);
        }
        lineSections.remove(lineSections.size() - 1);
    }

    public List<Long> getStationIds() {
        return lineSections.stream()
            .flatMap(lineSection -> Stream.of(
                lineSection.getUpStation().getId(),
                lineSection.getDownStation().getId()))
            .distinct()
            .collect(Collectors.toList());
    }

    private Optional<LineSection> getLastLineSection() {
        return lineSections.isEmpty() ? Optional.empty() : Optional.of(lineSections.get(lineSections.size() - 1));
    }

    private Optional<Station> getLastDownStation() {
        Optional<LineSection> lastSection = getLastLineSection();
        return lastSection.map(LineSection::getDownStation);
    }

    private void validateUpStation(Station upStation) {
        Optional<Station> lastDownStation = getLastDownStation();
        if (lastDownStation.isPresent() && !lastDownStation.get().getId().equals(upStation.getId())) {
            throw new InvalidUpStationException(upStation.getId());
        }
    }

    private void validateDownStation(Station downStation) {
        if (getStationIds().stream()
            .anyMatch(stationId -> stationId.equals(downStation.getId()))) {
            throw new InvalidDownStationException(downStation.getId());
        }
    }
}
