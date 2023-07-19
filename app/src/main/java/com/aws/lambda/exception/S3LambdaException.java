package com.aws.lambda.exception;

public class S3LambdaException extends RuntimeException {

    public S3LambdaException(String message) {
        super(message);
    }

    public S3LambdaException(String message, Throwable cause) {
        super(message, cause);
    }

    public S3LambdaException(Throwable cause) {
        super(cause);
    }
}
