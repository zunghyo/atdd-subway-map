package subway.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApplicationExceptionType {

    INTERNAL_SERVER_ERROR("500", "Server Error");

    private final String code;
    private final String message;
}
