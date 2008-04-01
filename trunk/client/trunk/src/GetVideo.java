/******************************************************************************
 * Copyright (C) 2008 Adam Parker. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/

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
   
    public MediaPlayer getGUI() throws Exception
    {
        //device = CaptureDeviceManager.getDevice("vfw:Microsoft WDM Image Capture (Win32):0");
        loc = new MediaLocator("rtp://" + strIP + ":" + strPort + "/video");
        data = Manager.createDataSource(loc);
        player = Manager.createPlayer(data);
           
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setPlayer(player);
		  return mediaPlayer;
    }
}
