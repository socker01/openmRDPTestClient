package cz.vutbr.fit.openmrdp;

import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.messages.OperationType;

/**
 * @author Jiri Koudelka
 * @since 10.05.2018
 */
final class Parameters {
    private final OperationType operationType;

    private final String resource;

    @Nullable
    private final String endpoint;

    Parameters(OperationType operationType, String resource, @Nullable String endpoint) {
        this.operationType = operationType;
        this.resource = resource;
        this.endpoint = endpoint;
    }

    OperationType getOperationType() {
        return operationType;
    }

    String getResource() {
        return resource;
    }

    String getEndpoint() {
        return endpoint;
    }
}
