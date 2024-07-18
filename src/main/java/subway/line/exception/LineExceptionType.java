package subway.line.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LineExceptionType {

    LINE_NOT_FOUND("400", "Entity Not Found"),
    INVALID_UP_STATION("400","Invalid Up Station"),
    INVALID_DOWN_STATION("400","Invalid Down Station") ;

    private final String code;
    private final String message;
}
