package douyu;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Md5Utils.java.*;
import domain.Message;
public class dyserver {
	Map map =new HashMap<String, Integer>();
	public Socket socket = null; 
	public OutputStream os;
	public InputStream is;
	double totalSpeed,speed;
	int count,count2,live;
	private ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream(1000);
	private OutputStream outputStream;
	String roomId = "287471";
	String groupId = "28";
	private byte[] info= new byte[1000];
	public void Init()
	{
		try {
			socket = new Socket("220.167.12.146", 8601);
			os=socket.getOutputStream();
			is=socket.getInputStream();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public void conn()
	{
		
	}
    public void sendReq() throws IOException {
        String devid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String rt = String.valueOf(System.currentTimeMillis() / 1000);
        String ver = "20150526";
        String magic = "7oE9nPEG9xXV69phU31FYCLUagKeYtsF";
        String vk = Md5Utils.md5(rt + magic + devid);

        sendMessage("type@=loginreq/username@=/ct@=2/password@=/roomid@=" + roomId +
                "/devid@=" + devid + "/rt@=" + rt + "/vk@=" + vk + "/ver@=" + ver + "/");
    }

    public void sendKeepLive() throws IOException {
    	speed=count2/50.0;
    	count2=0;
    	live++;
    	totalSpeed=count/(50.0*live);
        sendMessage("type@=keeplive/tick@=70/");
    }

    /**
     * 将数据放入缓冲流
     */
    public void putDataToStream(int[] data) {
        if (data != null) {
            for (int i : data) {
                byteArrayOutputStream.write(i);
            }
        }
    }
    public void sendMessage(String context) throws IOException {
        Message message = new Message(context);
        putDataToStream(message.getLength());
        putDataToStream(message.getCode());
        putDataToStream(message.getMagic());
        byteArrayOutputStream.write(context.getBytes());
        putDataToStream(message.getEnd());
        System.out.println(message);
        os.write(byteArrayOutputStream.toByteArray());
        byteArrayOutputStream.reset();
    }

    public void send() throws IOException, InterruptedException {

        System.out.println("进入房间:" + roomId);
        System.out.println("加入group:" + groupId);
        sendMessage("type@=loginreq/username@=auto_x1Rss3Zx4I/password@=123/roomid@=" + roomId + "/ct@=2/");
        ///sendMessage("type@=joingroup/rid@=" + roomId + "/gid@=" + groupId + "/");

        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.close();
        }
    }
    public void send2() throws IOException
    {
        System.out.println("进入房间:" + roomId);
        System.out.println("加入group:" + groupId);
        sendMessage("type@=joingroup/rid@="+roomId+"/gid@="+groupId+"/");
        ///sendMessage("type@=joingroup/rid@=" + roomId + "/gid@=" + groupId + "/");

        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.close();
        }
    }
    public void parseResponse(String response) {
        String REGEX = "type@=chatmessage/.*/content@=(.*)/snick@=(.*?)/";
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(response);
        
        String REGEX2 = "type@=dgn/gfid@=.*/snick@=(.*?)/"; //解析礼物
        Pattern pattern2= Pattern.compile(REGEX2);
        Matcher matcher2 = pattern2.matcher(response);
        
        String REGEX3 = "type@=bc_buy_deserve/.*/lev@=.*/snick@=(.*?)/"; //解析礼物
        Pattern pattern3= Pattern.compile(REGEX3);
        Matcher matcher3 = pattern3.matcher(response);

        if (matcher.find()) {
        	count++;count2++;
            System.out.println(count+"."+matcher.group(2) + ": " + matcher.group(1));
        }
        /*if (matcher2.find()) {
        	///count++;count2++;
        	/*if(matcher2.group(1)=="50")
            System.out.println("..........."+matcher2.group(2) + " 赠送主播  " + "(100鱼丸)"+"...........");
        	if(matcher2.group(1)=="57")
                System.out.println("..........."+matcher2.group(2) + " 赠送主播  " + "(赞)"+"...........");
        	if(matcher2.group(1)=="53")
                System.out.println("..........."+matcher2.group(2) + " 赠送主播  " + "(520鱼丸)"+"...........");*/
        	//System.out.println("..........."+matcher3.group(0) + " 赠送主播  "+matcher3.group(1) + "(初级酬勤)"+"...........");
        	//System.out.println("..........."+matcher3.group(0) + " 赠送主播  "+matcher3.group(1) + "(初级酬勤)"+"...........");
        }
    
	    /*if (matcher3.find()) {
	    	///count++;count2++;
	    	if(matcher3.group(1)=="1")
	    		System.out.println("..........."+matcher3.group(2) + " 赠送主播  " + "(初级酬勤)"+"...........");
	    	if(matcher3.group(1)=="2")
		        System.out.println("..........."+matcher3.group(2) + " 赠送主播  " + "(中级酬勤)"+"...........");
	    	if(matcher3.group(1)=="3")
		        System.out.println("..........."+matcher3.group(2) + " 赠送主播  " + "(高初级酬勤)"+"...........");
	    	
	    }*/
        
    
    public void receive() throws IOException {
        int i;
        byte[] bytes = new byte[1024];
        while (socket != null && socket.isConnected() && (i = is.read(bytes)) != -1) {
            parseResponse(new String(bytes, 0, i));
        }

    }
}
