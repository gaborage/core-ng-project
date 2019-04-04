package core.log.web;

import core.framework.web.exception.BadRequestException;

import static core.framework.util.Strings.format;

/**
 * @author neo
 */
public class CollectEventRequestValidator {
    private static final int MAX_CONTEXT_VALUE_LENGTH = 1000;

    void validate(CollectEventRequest request) {
        for (CollectEventRequest.Event event : request.events) {
            if (event.result == CollectEventRequest.Result.OK && event.action == null)
                throw new BadRequestException("action must not be null if result is OK");
            if ((event.result == CollectEventRequest.Result.WARN || event.result == CollectEventRequest.Result.ERROR)
                    && event.errorCode == null)
                throw new BadRequestException("errorCode must not be null if result is WARN/ERROR");

            event.context.forEach((key, value) -> {
                if (value.length() > MAX_CONTEXT_VALUE_LENGTH)
                    throw new BadRequestException(format("context value is too long, key={}, value={}...(truncated)", key, value.substring(0, 200)));
            });
        }
    }
}
