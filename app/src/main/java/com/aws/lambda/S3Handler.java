package com.aws.lambda;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.model.LogType;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import com.aws.lambda.model.Address;
import com.aws.lambda.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aws.lambda.JacksonConfig.objectMapper;

public class S3Handler implements RequestHandler<S3Event, Response> {

    private static final String DYNAMO_ARN = "arn:aws:lambda:eu-north-1:607695928816:function:LambdaDynamoDBInsertHandler";
    private static final String DYNAMO_FUNC_NAME = "LambdaDynamoDBInsertHandler";
    private static final S3Client s3Client = S3Client.builder()
            .region(Region.EU_NORTH_1)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    private static final AWSLambda client = AWSLambdaClientBuilder.standard().build();

    @Override
    public Response handleRequest(S3Event s3event, Context context) {

        List<Address> addresses = new LinkedList<>();

        if (Objects.isNull(s3event) || Objects.isNull(context)) {
            throw new IllegalStateException("No event/context provided");
        }

        LambdaLogger logger = context.getLogger();

        if (Objects.isNull(logger)) {
            throw new IllegalStateException("No logger found");
        }

        try {


            for (S3EventNotificationRecord s3EventNotificationRecord : s3event.getRecords()) {
                logger.log("Starting  operation: ");

                S3EventNotification.S3Entity s3EventNotificationRecordS3 = s3EventNotificationRecord.getS3();
                String srcBucket = s3EventNotificationRecordS3.getBucket().getName();
                String srcKey = s3EventNotificationRecordS3.getObject().getUrlDecodedKey();

                ResponseInputStream<GetObjectResponse> responseInputStream = getHeadObject(srcBucket, srcKey);

                List<Address> addressList = getAddresses(responseInputStream);
                logger.log("Adding addresses: " + addressList.toString());
                addresses.addAll(addressList);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Response response = new Response(addresses);

        String prettyString;
        ByteBuffer buffer;

        try {
            prettyString = objectMapper.writeValueAsString(response);
            byte[] bytes = objectMapper.writeValueAsBytes(response);
            buffer = ByteBuffer.wrap(bytes);
            logger.log("Response as bytes:" + prettyString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        InvokeRequest request = new InvokeRequest()
                .withFunctionName(DYNAMO_FUNC_NAME)
                .withInvocationType(InvocationType.RequestResponse)
                .withLogType(LogType.Tail)
//                .withClientContext("S3Handler")
                .withPayload(prettyString);

        InvokeResult invokeResult = client.invoke(request);
        logger.log("Response gotten from invocation: " + invokeResult);

        logger.log("Ending  operation: ");
        return response;
    }

    private List<Address> getAddresses(ResponseInputStream<GetObjectResponse> responseInputStream) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseInputStream, StandardCharsets.UTF_8))) {

            return bufferedReader.lines()
                    .skip(1)
                    .map(line -> {

                        if (Objects.isNull(line)) throw new IllegalStateException("No input");
                        String[] tokens = line.split(",");


                        if (tokens.length != 4) throw new IllegalStateException("Incomplete address");

                        return Address.builder()
                                .setStreetName(tokens[0])
                                .setCity(tokens[1])
                                .setState(tokens[2])
                                .setCountry(tokens[3])
                                .build();
                    })
                    .collect(Collectors.toList());
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    private ResponseInputStream<GetObjectResponse> getHeadObject(String bucket, String key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        return S3Handler.s3Client.getObject(getObjectRequest);

    }
}
