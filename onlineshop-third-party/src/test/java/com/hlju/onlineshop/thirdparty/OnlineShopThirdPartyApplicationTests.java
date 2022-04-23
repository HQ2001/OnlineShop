package com.hlju.onlineshop.thirdparty;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.tea.*;
import com.aliyun.dysmsapi20170525.*;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.*;
import com.aliyun.teaopenapi.models.*;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.hlju.onlineshop.thirdparty.service.SmsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class OnlineShopThirdPartyApplicationTests {

    @Autowired
    private OSSClient ossClient;

    @Autowired
    private SmsService smsService;

    @Test
    public void smsTest() {
        smsService.sendSmsCode("15504630661", "123456");
    }

    @Test
    public void testUpload() {
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "online-shop-hq";
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = "02.jpg";

        try {
            String content = "E:\\nicePicture\\02.jpg";
            ossClient.putObject(bucketName, objectName, new FileInputStream(content));
        } catch (OSSException oe) {
            System.out.println("Error Message:" + oe.getErrorMessage());
            oe.printStackTrace();
        } catch (ClientException ce) {
            System.out.println("Error Message:" + ce.getMessage());
            ce.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

}
