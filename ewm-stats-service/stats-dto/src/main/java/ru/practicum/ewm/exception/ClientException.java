package ru.practicum.ewm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientException extends Exception {
    protected final HttpStatus httpStatus;
    protected String apiMessage;

    public ClientException(String internalMessage,
                           String apiMessage,
                           HttpStatus httpStatus) {
        super(internalMessage);
        this.httpStatus = httpStatus;
        this.apiMessage = apiMessage;
    }

    public static ClientException getBadRequestException(String internalMessage,
                                                         String apiMessage) {
        return new ClientException(internalMessage, apiMessage, HttpStatus.BAD_REQUEST);
    }

    public static ClientException getUnauthorizedException(String internalMessage,
                                                           String apiMessage) {
        return new ClientException(internalMessage, apiMessage, HttpStatus.UNAUTHORIZED);
    }

    public static ClientException getForbiddenException(String internalMessage,
                                                        String apiMessage) {
        return new ClientException(internalMessage, apiMessage, HttpStatus.FORBIDDEN);
    }

    public static ClientException getNotFoundException(String internalMessage,
                                                       String apiMessage) {
        return new ClientException(internalMessage, apiMessage, HttpStatus.NOT_FOUND);
    }

    public static ClientException getInternalServerError(String internalMessage) {
        return new ClientException(
                internalMessage,
                "something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
