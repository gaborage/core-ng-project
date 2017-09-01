package core.framework.impl.validate.v2;

import core.framework.api.util.Maps;

import java.util.Map;

/**
 * @author neo
 */
public class ValidationErrors {
    public Map<String, String> errors;

    public void add(String field, String error) {
        if (errors == null) errors = Maps.newLinkedHashMap();
        errors.put(field, error);
    }

    boolean hasError() {
        return errors != null;
    }
}