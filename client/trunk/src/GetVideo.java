import javax.media.*;
import javax.media.control.*;
import javax.media.protocol.*;
import javax.media.util.*;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.bean.playerbean.*;
import javax.swing.*;

public class GetVideo
{
    public GetVideo()
    {
    }
   
    public MediaPlayer videoGUI()
    {
        //JFrame frame = new JFrame();
        //frame.setBounds(0, 0, 800, 600);
       
        CaptureDeviceInfo device;
        MediaLocator loc;
        MediaLocator outLoc;
        DataSource data;
        DataSource outData;
        MediaPlayer mediaPlayer;
        Player player;
        DataSink rtptransmitter = null;
        Processor processor;
        //TrackControl track[];
       
        try
        {
            device = CaptureDeviceManager.getDevice("vfw:Microsoft WDM Image Capture (Win32):0");
            loc = device.getLocator();
            data = Manager.createDataSource(loc);
            player = Manager.createPlayer(data);
           // processor = Manager.createProcessor(loc);
           
           // processor.configure();
           // processor.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW));
           
            //track = processor.getTrackControls();
            //processor.realize();
           
            //outData = processor.getDataOutput();
           
            //outLoc = new MediaLocator("rtp://127.0.0.1:2000/video/1");
           // rtptransmitter = Manager.createDataSink(outData, outLoc);
           
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setPlayer(player);
			player.start();
			return mediaPlayer;
           
            //mediaPlayer.setBounds(0, 0, 355, 288);
           
            //mediaPlayer
           
            //data.connect();
           // rtptransmitter.open();
           // rtptransmitter.start();
           
            //mediaPlayer.start();
            //frame.setContentPane(mediaPlayer);
            //frame.setVisible(true);
       
          //  System.out.println("Test");
           
         //   rtptransmitter.stop();
         //   rtptransmitter.close();
           
        //    mediaPlayer.stop();
           
            //data.disconnect();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
		return new MediaPlayer();
    }
}

