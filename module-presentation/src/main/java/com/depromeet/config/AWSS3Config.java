package com.depromeet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSS3Config {

    /*
     * https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/regions/providers/DefaultAwsRegionProviderChain.html
     *
     * aws accessKey와 secretKey를 application.yml에 올리는 것은 알다시피 위험한 행동이다.
     * 물론 IAM한테 적절한 권한을 주면 피해를 최소화 할 수 있으나 application.yml에 올리지 않고도 하는 방법이 있다.
     * DefaultAwsRegionProviderChain 라는 것이 있는데 이는 아래의 4가지 체인을 돌면서 accessKey, secretKey 정보를 얻는다.
     * 1. os 환경변수의 accessKey, SecretKey
     * 2. java system 속성의 aws.accessKeyId, aws.secretKey 확인
     * 3. /.aws/credentials 파일의 정보를 가져온다.
     * 4. ec2의 역할을 가져와 인증(ec2에 s3 관련 역할이 있어야 함)
     *
     * 4번이 가장 안전하겠으나...우리는 NCP를 사용하므로 1번 방법을 사용하겠다.
     * */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.AP_NORTHEAST_2)
                .build();
    }
}
