package subway.common.exception;

public class ApplicationException extends RuntimeException {

    private final ApplicationExceptionType applicationExceptionType;

    public ApplicationException(String message, ApplicationExceptionType applicationExceptionType) {
        super(message);
        this.applicationExceptionType = applicationExceptionType;
    }
}
