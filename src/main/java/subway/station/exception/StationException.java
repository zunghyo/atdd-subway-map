package subway.station.exception;

import lombok.Getter;

@Getter
public class StationException extends RuntimeException {
    private final StationExceptionType stationExceptionType;

    public StationException(StationExceptionType stationExceptionType) {
        super(stationExceptionType.getMessage());
        this.stationExceptionType = stationExceptionType;
    }
}
