package subway.line.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LineExceptionType {

    LINE_NOT_FOUND("400", "Entity Not Found");

    private final String code;
    private final String message;
}
