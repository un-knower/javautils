package com.cenrise.utils.kylin;

import com.cenrise.utils.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class KylinRESTfulMethodMain {
    private static final String baseURL = "http://10.1.30.101:7070/kylin/api";


    public static void main(String[] args) throws ParseException {
        String output;
        String body;
        //1.验证
        KylinRESTfulMethod.login(baseURL, "ADMIN", "KYLIN");

        //------------------------------------------------------------
//        String body = "{\"sql\":\"select count(*) from kylin_sales\",\"offset\":0,\"limit\":50000,\"acceptPartial\":false,\"project\":\"learn_kylin\"}";
//        output = KylinRESTfulMethod.query(body);
//		//--------------------------------------------------------------------

        //------------------------------------------------------------
//		String body = "{\"sql\":\"select * from FACT_\",\"offset\":0,\"limit\":50000,\"acceptPartial\":false,\"project\":\"my_kylin\"}";
//		output = KylinRESTfulMethod.query(body);
//		//--------------------------------------------------------------------
//		output = KylinRESTfulMethod.listQueryableTables("my_kylin");
//		//----------------list all cutes------------------------------
//        output = KylinRESTfulMethod.listCubes(0, 15, "sample_cube_1", "learn_kylin");
//		//----------------------------------------------------------------
        //2.获取cube信息
        http:
//localhost:7070/kylin/api/cubes?cubeName={cube_name}&limit=15&offset=0
        output = KylinRESTfulMethod.getCube(baseURL, "170823_MPOS_OPERATION");
        System.out.println(output);
//		//----------------list my_kylin_cube information--------------
//		output = KylinRESTfulMethod.getCubeDes(baseURL,"170823_MPOS_OPERATION_TEST");
//		//----------------get data model-------------------------
//		output = KylinRESTfulMethod.getDataModel("my_kylin_model");
//		//------------------------------------------------------------

//        long startTimeLong = 0;
//        long endTimeLong = 0;
//        body = "{\"cubeName\":\"170823_MPOS_OPERATION\",\"limit\":15,\"offset\":0}";
//        String cubedes = KylinRESTfulMethod.getCubesByName(baseURL,body);
//        System.out.println("cube信息："+cubedes);

        //		前一天的数据：1，前二天的数据：2，前三天的数据：3.过去7天的数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = sdf.parse("2017-08-28 00:00:00");
        Date endTime = sdf.parse("2017-08-28 23:59:59");
        String startTimeLong = String.valueOf(startTime.getTime());
        String endTimeLong = String.valueOf(endTime.getTime());

        body = "{\"startTime\":" + startTimeLong + ",\"endTime\":" + endTimeLong + ",\"buildType\":\"BUILD\"}";
//        System.out.println("构建请求参数："+body);
//        output = KylinRESTfulMethod.buildCube(baseURL,"170823_MPOS_OPERATION_TEST",body);


//--------------------------------------------------------
//		output = KylinRESTfulMethod.enableCube("my_kylin_cube");
//		//-------------------------------------------------------
//		output = KylinRESTfulMethod.disableCube("my_kylin_cube");
//		//-------------------------------------------------------------------
//		output = KylinRESTfulMethod.purgeCube("my_kylin_cube");
//		//--------------------------------------------------------------------

        //		String jobId="test";
        //		output = KylinRESTfulMethod.resumeJob(jobId);
//		//--------------------------------------------------------------------
//		jobId="test";
//		output = KylinRESTfulMethod.discardJob(jobId);
//		//--------------------------------------------------------------------
//		jobId="test";
//		output = KylinRESTfulMethod.getJobStatus(jobId);
//		//------------------------------------------------------------------
//		jobId="test";
//		String stepId ="test";
//		output = KylinRESTfulMethod.getJobStepOutput(jobId,stepId);
//		//--------------------------------------------------------------------
//		output = KylinRESTfulMethod.getHiveTable("FACT_");
//		//---------------------------------------------------------------------
//		output = KylinRESTfulMethod.getHiveTableInfo("FACT_");
//		//--------------------------------------------------------------
//		output = KylinRESTfulMethod.getHiveTables("my_kylin", true);
//		//-----------------------------------------------------------------
//		output  = KylinRESTfulMethod.loadHiveTables("FACT_", "my_kylin");
//		//--------------------------------------------------------------------

        //output  = KylinRESTfulMethod.wipeCache("METADATA", "my_kylin_cube", "drop");

//        KylinRESTfulMethod.timestampToString(1451606400000L);
//        KylinRESTfulMethod.timestampToString(1483228800000L);

        /*SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime2 = sdf.parse("2014-1-1 00:00:00");
        System.out.println(startTime.getTime());
        System.out.println(KylinRESTfulMethod.timestampToString(1388563200000L));*/

        System.out.println("========1388563200000<->2014-01-01==========");
        System.out.println(KylinRESTfulMethod.timestampToString(1388563200000L));
        System.out.println("官方,2014-01-01的long值是：1388563200000,通过此方式获取过来的是：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-01-01 00:00:00").getTime());

        System.out.println("========1388563200000<->2014-01-01 UTC==========");
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println(f.format(1388563200000L-28800000L));
        System.out.println("官方,2014-01-01的long值是：1388563200000,通过UTC方式获取过来的是：" + DateUtil.toUTC(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-01-01 00:00:00").getTime()));


        System.out.println("========1388563200000<->Calendar to Long UTC==========");

        String str="2014-01-01";
        Date date =new SimpleDateFormat("yyyy-MM-dd").parse(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        System.out.println(calendar.getTimeInMillis());


        System.out.println("===========测试===============");
        System.out.println(KylinRESTfulMethod.timestampToString(1672560000000L));
        System.out.println(f.format(1672560000000L));

        Date startTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-01-01 00:00:00");
        System.out.println(String.valueOf(DateUtil.toUTC(startTime2.getTime())));



    }


}
