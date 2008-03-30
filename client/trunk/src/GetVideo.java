import javax.media.*;
import javax.media.control.*;
import javax.media.protocol.*;
import javax.media.util.*;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.bean.playerbean.*;
import javax.swing.*;
import java.net.*;

public class GetVideo
{
	private String strIP;
	private String strPort;
	private CaptureDeviceInfo device;
    private MediaLocator loc;
    private DataSource data = null;
    private MediaPlayer mediaPlayer;
    private Player player;
	
	 public GetVideo(String pIP, String pPort)
    {
    	strIP = pIP;
    	strPort = pPort;
    }
   
    public MediaPlayer getGUI()
    {
        try
        {
            //device = CaptureDeviceManager.getDevice("vfw:Microsoft WDM Image Capture (Win32):0");
            loc = new MediaLocator("rtp://" + strIP + ":" + strPort + "/video");
            data = Manager.createDataSource(loc);
            player = Manager.createPlayer(data);
           
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setPlayer(player);
			return mediaPlayer;
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
		  return new MediaPlayer();
    }

    public void closeConnection()
    {
		data.disconnect();
		mediaPlayer = null;
		player = null;
		data = null;
		loc = null;
	}
}

