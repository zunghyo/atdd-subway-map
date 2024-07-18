package subway.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.line.exception.LineException;
import subway.line.exception.LineExceptionType;
import subway.station.exception.StationException;
import subway.station.exception.StationExceptionType;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StationException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleStationException(StationException e) {
        StationExceptionType exceptionType = e.getStationExceptionType();
        return ResponseEntity.status(Integer.parseInt(exceptionType.getCode())).body(new ExceptionResponse(exceptionType.getCode(), exceptionType.getMessage()));
    }

    @ExceptionHandler(LineException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleLineException(LineException e) {
        LineExceptionType exceptionType = e.getLineExceptionType();
        return ResponseEntity.status(Integer.parseInt(exceptionType.getCode())).body(new ExceptionResponse(exceptionType.getCode(), exceptionType.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handlerRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage()));
    }
}
