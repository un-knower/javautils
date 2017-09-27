package com.cenrise.utils.kylin;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KylinBuildTest {

    private static String encoding;
    private static final String baseURL = "http://10.1.30.101:7070/kylin/api";


    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date startTime = sdf.parse("2017-08-28 00:00:00");
//        Date endTime = sdf.parse("2017-08-28 23:59:59");
        Date startTime = sdf.parse("2017-08-29 00:00:00");
        Date endTime = sdf.parse("2017-08-30 23:59:59");
        String startTimeLong = String.valueOf(startTime.getTime());
        String endTimeLong = String.valueOf(endTime.getTime()) ;

        String body = "{\"startTime\":"+startTimeLong+",\"endTime\":"+endTimeLong+",\"buildType\":\"BUILD\"}";
        System.out.println(body);

        KylinBuildTest krmm = new KylinBuildTest();

        krmm.login("ADMIN","KYLIN");
        krmm.buildCube("170823_MPOS_OPERATION",body);
    }

    /**
     * 授权登陆
     *
     * @param user
     * @param passwd
     * @return
     */
    public StringBuffer login(String user, String passwd) {
        KylinBuildTest krmm = new KylinBuildTest();
        String method = "POST";
        String para = "/user/authentication";
        byte[] key = (user + ":" + passwd).getBytes();
        encoding = Base64.encodeBase64String(key);
        return krmm.excute(baseURL,para, method, null);
    }

    /**
     * Rebuild Cube
     *
     * @param cubeName
     * @param params
     * @return
     */
    public StringBuffer buildCube(String cubeName, String params) {
        String method = "PUT";
        String para = "/cubes/" + cubeName + "/rebuild";
        return excute(baseURL, para, method, params);
    }



    /**
     * 通过httpClient方式调用kylin Restful接口
     *
     * @param kylinIp
     * @param para
     * @param method
     * @param params
     * @return
     */
    public StringBuffer excute(String kylinIp, String para, String method, String params) {
        StringBuffer out = new StringBuffer();
        try {
            URL url = new URL(kylinIp + para);
            System.out.println(url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            if (params != null) {
                byte[] outputInBytes = params.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(outputInBytes);
                os.close();
            }
            InputStream content = (InputStream) connection.getInputStream();
            // 解决乱码问题
            BufferedReader in = new BufferedReader(new InputStreamReader(content, Charset.forName("UTF-8")));
            String line;
            while ((line = in.readLine()) != null) {
                out.append(line);
            }
            in.close();
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }


}


