package com.alipay.common.util.tools;

/**
 * Created by xianyu.hxy on 2015/5/29.
 */
        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.DatagramPacket;
        import java.net.DatagramSocket;
        import java.net.InetAddress;
        import java.util.ArrayList;

        import org.json.JSONException;
        import org.json.JSONObject;

        import android.text.format.Time;

public class MyLog
{
    private static String phoneID=android.os.Build.ID;
    public static class MLog  //静态类
    {

        /**
         * 主方法，唯一对外公开，新建线程执行，避免数据量过大，导致程序假死
         */
        public static void Log()
        {
            Thread th=new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    getLog();
                }
            });

        }

        /**
         * 捕获日志
         */
        private static void getLog()
        {
            System.out.println("--------func start--------");
            try
            {
                ArrayList<String> cmdLine=new ArrayList<String>();
                cmdLine.add("logcat");
                cmdLine.add("-d");

                ArrayList<String> clearLog=new ArrayList<String>();
                clearLog.add("logcat");
                clearLog.add("-c");

                Process process=Runtime.getRuntime().exec(cmdLine.toArray(new String[cmdLine.size()]));
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuffer stringBuffer=new StringBuffer(2048);
                String str=null;
               // L.getFile("sysLog");
                while((str=bufferedReader.readLine())!=null)
                {
                  //  if(stringBuffer.length()>2000){stringBuffer.delete(0,stringBuffer.length()-1);}
                    stringBuffer.append(str);
                    Runtime.getRuntime().exec(clearLog.toArray(new String[clearLog.size()]));


                    //sendLogUDP(logToJobj(str));

                }
                if(str==null)
                {
                    System.out.println("--   is null   --");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            System.out.println("--------func end--------");
        }

        /**
         * 将Log转换成JSONObject
         * @param s
         * @return
         */
        private static JSONObject logToJobj(String s)
        {

            String s1=s.substring(0,1);

            String s2=s.substring(s.indexOf('/')+1, s.indexOf('('));

            String s3=s.substring(s.indexOf('(')+1, s.indexOf(')'));

            String s4=s.substring(s.indexOf(':')+1);


            JSONObject obj=new JSONObject();
            try
            {
                obj.put("PhoneID", phoneID);
                obj.put("Type", s1.trim());
                obj.put("Tag", s2.trim());
                obj.put("ThreadID", s3.trim());
                obj.put("Msg", s4.trim());
                obj.put("Time", getTime());
            }
            catch (JSONException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return obj;
        }

        /**
         * 通过UDP方式发送数据到服务器
         * @param obj
         */
        private  void sendLogUDP(JSONObject obj)
        {
            String objStr=obj.toString();
            int port=8081;
            DatagramSocket ds=null;
            InetAddress ia=null;
            if(obj!=null)
            {
                try
                {
                    ds=new DatagramSocket();
                    ia=InetAddress.getByAddress(null);
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                int msgLenght=objStr.length();
                byte[] msgByte=objStr.getBytes();

                DatagramPacket dp=new DatagramPacket(msgByte, msgLenght, ia, port);

                try
                {
                    ds.send(dp);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }
            }
        }

        /**
         * 获取当前时间
         * @return
         */
        private static String getTime()
        {
            Time time = new Time("GMT+8");
            time.setToNow();
            int year = time.year;
            int month = time.month;
            int day = time.monthDay;
            int minute = time.minute;
            int hour = time.hour;
            int sec = time.second;
            return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+sec;
        }
    }
}