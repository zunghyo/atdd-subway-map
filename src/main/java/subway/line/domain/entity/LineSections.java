package subway.line.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.line.exception.LineException;
import subway.line.exception.LineExceptionType;
import subway.station.domain.Station;

@Embeddable
@Getter
@NoArgsConstructor
public class LineSections {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private final List<LineSection> lineSections = new ArrayList<>();

    public void addSection(LineSection lineSection) {

        validateUpStation(lineSection.getUpStation());
        validateDownStation(lineSection.getDownStation());

        lineSections.add(lineSection);
    }

    public List<Long> getStationIds() {
        return lineSections.stream()
            .flatMap(lineSection -> Stream.of(
                lineSection.getUpStation().getId(),
                lineSection.getDownStation().getId()))
            .distinct()
            .collect(Collectors.toList());
    }

    private LineSection getLastSection() {
        return lineSections.isEmpty() ? null : lineSections.get(lineSections.size() - 1);
    }

    private void validateUpStation(Station upStation) {
        if (!lineSections.isEmpty() && !getLastSection().getDownStation().getId().equals(upStation.getId())) {
            throw new LineException(LineExceptionType.INVALID_UP_STATION);
        }
    }

    private void validateDownStation(Station downStation) {
        if (getStationIds().stream()
            .anyMatch(lineSectionId -> lineSectionId.equals(downStation.getId()))) {
            throw new LineException(LineExceptionType.INVALID_DOWN_STATION);
        }
    }

}
