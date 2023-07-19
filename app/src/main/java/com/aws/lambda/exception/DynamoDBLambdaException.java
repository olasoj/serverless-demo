package com.aws.lambda.exception;

public class DynamoDBLambdaException extends RuntimeException {

    public DynamoDBLambdaException(String message) {
        super(message);
    }

    public DynamoDBLambdaException(String message, Throwable cause) {
        super(message, cause);
    }

    public DynamoDBLambdaException(Throwable cause) {
        super(cause);
    }
}
