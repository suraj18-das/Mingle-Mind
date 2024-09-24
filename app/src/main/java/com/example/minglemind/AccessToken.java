package com.example.minglemind;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AccessToken {
    private static final String firebaseMessagingScope="https://www.googleapis.com/auth/firebase.messaging";

    public String getAccessToken(){
        try{
            String jsonString="{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"mingle-mind\",\n" +
                    "  \"private_key_id\": \"44d80dd9a41a55eabfbbd945b4c413e8e87eaae7\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQClYacUeOQqLV3C\\nVQpozPs5UZsHQaiNDQJSXNaUPn0y8ZFZ5qZazV0mnCAfT7WlcgTD0NT1CM6uxDkZ\\n027u9UMvciLZwbslBThOrSzPcQhnyFYaegjnlf08OW+b4wJtvKTk9Oj9K0iXyn0m\\nZnGbvcGOup/msZ0QVpBg1aA6nCH3xfh/qfcyc2T3WYRWT5ONyP1jhgnQhVRRQ+0C\\n3x4pm8xkZNx08cTdwOgSGaP4eonGCUgPpybLqwzI7E08FoFIRCpebb+y/lxfPnQj\\ndqFxIIxtsue4AT4YX9RJ0qX4sW8ffnbdNxq9FzyKd0rAplRLDY7lB2DirByko2Ao\\n49x5wNjHAgMBAAECggEAAtOY8SSINC2Jt5hDF2INfYzNNM7X+SH6r+EU7JXm1BfF\\nD3+XYnm13ykpWWLZrJ+DjYjRSVpvSEhjq3MHWqX7EhTSzw/DFl5kK6zeohfN+EW4\\nLWqrIhe+3luJWOVrDxuuyJgw+6BUp/3jeeQ9VwqWtzN6wSaFYMtoW027ZKHZG/l3\\n1YEu583rR8t5ERo6x3EA9iJhLCxxHDfzZKKJmRCRwCvPsQ7tyNS3A+MvZbYo7Kh3\\nYyX1B1SU+Ghjh51OdY/U5TNU2cv0rwgq32CiAt+yC0WwBAnEoYXYgZzdIYovh/w9\\nPxGCKpIme1FHKRKyhrYVtz7YOU6dLih7ViG9+YkG1QKBgQDpuK1ak/Fj2R6RsrjI\\nJLJHXnDxd0I136G2NYTq1Aw2TTnp/3ZA2kpRT6kACDqkxzD3fYZiQ91btszEKOdz\\no0xVgHzo7Sal/zrWrrFkSY3OCV4cndqkJuLeJw1PlUptrfhBhgZ1xK69iFK9DF03\\nQQdQoavET2H0apCLIGaMuTSpFQKBgQC1JVQk/5c3pa4lc5gZ/jMHH7jZsNEZOBfq\\nSVttXOHK5j5h6EdrSyMQ2dfy7SyHK9NvrFsB/Z5+5/zbDjFvrVumR+nszHgXVEca\\n652iMgoQ7dfY5pfmsVyu86Cq+WWG1RxWFxd+8bU1bBu2sdOvIILjhXyZOggnjC2B\\nW4bov9a5awKBgQC7L3VnfeVumo2pEwJ7CSjfj/vU6DbW/ux9ChASIxJEy0T+6+LB\\nvhKXXL11kSGmpwHItXfTa5b1xUnqMPht3PNd5zIjOwwhFnEBT5gzQdWYllgRgzuB\\nDMPWOGCoxGB/xz10/Opfl857bZsWXbiqcCJzqYfwCcPiBRcO1jxLqGlMoQKBgQCL\\nN+UzijvtyKfOoMiFMZiGIRswM3SArVuz9C0QIBJongvTs3EwRM7uZH5TdXSDbmtR\\nqGVJYmItpp3uvTnBWrrVUO+3zm19gIglaG/99/dEi4cYbCDSa/2PeC5DOOmOdrsQ\\nby7foWRjanLiNkk6jwIJWs4n9THPMyzF/lHxD383gQKBgE7/ZVAINKmlrffKeYrD\\nU9DI8RinoZapE8Ors23dm8fQ9597ns0URCgEAAqrM5J3xKT4bpWPnib22nt3piHe\\naPedhnv/D1vsDdp2yfjcAIHj76TsenCLxw/V8TPJitEgV8WAZ45qbHWdu2y35wpf\\no9cmHAc47wB2ymuxM/UM7K/p\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-9yzn7@mingle-mind.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"110200780502439073900\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-9yzn7%40mingle-mind.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n";

            InputStream stream=new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials googleCredentials=GoogleCredentials.fromStream(stream)
                            .createScoped(Lists.newArrayList(firebaseMessagingScope));

            googleCredentials.refresh();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
