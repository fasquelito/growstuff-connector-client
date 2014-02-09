package tools;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Aleks
 * Date: 03/02/14
 * Time: 13:29
 * To change this template use File | Settings | File Templates.
 */
public class RPi {


    public static boolean configure(String SSID, String PSK) throws JSchException {
        JSch jsch=new JSch();


        try {

            /*Session session = jsch.getSession("pi", "192.168.0.1");
            jsch.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword("raspberry");

            UserInfo usrInfo = new MyUserInfo();
            session.setUserInfo(usrInfo);
            session.connect(20000);*/

            Session session = jsch.getSession("pi", "192.168.0.1", 22);
            //UserInfo ui = new MyUserInfo();
            //session.setUserInfo(ui);
            //Not a good idea to disable key checking but only way to make it work for now
            //session.setConfig("StrictHostKeyChecking", "no");
            jsch.setConfig("StrictHostKeyChecking", "no");
            session.setPassword("growstuff");
            session.connect(40000);

            Channel channel = session.openChannel("exec");

            ((ChannelExec)channel).setCommand("sudo ./networks/configure.sh " + SSID + " " + PSK + " & 2> /dev/null");

            channel.setInputStream(null);

            ((ChannelExec)channel).setErrStream(System.err);
            InputStream in=channel.getInputStream();

            channel.connect();


            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);
                    if(i<0)break;
                    System.out.print(new String(tmp, 0, i));
                }
                if(channel.isClosed()){
                    System.out.println("exit-status: "+channel.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }

            channel.disconnect();
            session.disconnect();

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return true;
    }

    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {

        @Override
        public String getPassphrase() {
            return null;
        }
        @Override
        public String getPassword() {
            return null;
        }
        @Override
        public boolean promptPassphrase(String arg0) {
            return false;
        }
        @Override
        public boolean promptPassword(String arg0) {
            return false;
        }
        @Override
        public boolean promptYesNo(String arg0) {
            return false;
        }
        @Override
        public void showMessage(String arg0) {
        }
        @Override
        public String[] promptKeyboardInteractive(String arg0, String arg1,
                                                  String arg2, String[] arg3, boolean[] arg4) {
            return null;
        }
    }

}