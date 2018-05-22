package cz.vutbr.fit.openmrdp;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.messages.OperationType;

/**
 * @author Jiri Koudelka
 * @since 10.05.2018
 */
final class Parameters {
    private final OperationType operationType;

    @Nullable
    private final String resource;

    @Nullable
    private final String endpoint;

    @Nullable
    private final String fileName;

    @Nullable
    private final String userName;

    @Nullable
    private final String password;

    private Parameters(Builder builder) {
        this.operationType = Preconditions.checkNotNull(builder.operationType);
        this.resource = builder.resource;
        this.endpoint = builder.endpoint;
        this.fileName = builder.fileName;
        this.userName = builder.userName;
        this.password = builder.password;
    }

    static class Builder{
        private OperationType operationType;
        private String resource;
        private String endpoint;
        private String fileName;
        private String userName;
        private String password;

        Builder withOperationType(OperationType operationType){
            this.operationType = operationType;
            return this;
        }

        Builder withResource(String resource){
            this.resource = resource;
            return this;
        }

        Builder withEndpoint(String endpoint){
            this.endpoint = endpoint;
            return this;
        }

        Builder withFileName(String fileName){
            this.fileName = fileName;
            return this;
        }

        Builder withUserName(String userName){
            this.userName = userName;
            return this;
        }

        Builder withPassword(String password){
            this.password = password;
            return this;
        }

        Parameters build(){
            return new Parameters(this);
        }
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

    String getFileName() {
        return fileName;
    }

    String getUserName() {
        return userName;
    }

    String getPassword() {
        return password;
    }
}
