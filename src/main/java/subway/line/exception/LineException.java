package subway.line.exception;

import lombok.Getter;

@Getter
public class LineException extends RuntimeException {
    private final LineExceptionType lineExceptionType;

    public LineException(LineExceptionType lineExceptionType) {
        super(lineExceptionType.getMessage());
        this.lineExceptionType = lineExceptionType;
    }
}
