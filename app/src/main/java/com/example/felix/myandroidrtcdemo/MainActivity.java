package com.example.felix.myandroidrtcdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.opengl.GLSurfaceView;
import org.webrtc.*;
import android.util.Log;

public class MainActivity extends Activity {
    private PeerConnectionFactory peerConnectionFactory;
    private MediaStream stream;
    private VideoSource videoSource;
    private VideoCapturer videoCapturer;
    private MediaConstraints videoConstraints;
    private VideoRenderer localVideoRender;
    private VideoTrack localVideoTrack;
    private GLSurfaceView surfaceView;
    private Button btn_start;
    private Button btn_stop;

    private void initVideo() {

       if (PeerConnectionFactory.initializeAndroidGlobals(MainActivity.this, true, true, false, null)) {
           Log.i("LLLLL", "initializeAndroidGlobals");
           peerConnectionFactory = new PeerConnectionFactory();
           Log.i("LLLLL", "new PeerConnectionFactory");
        }

        String frontCameraDeviceName = VideoCapturerAndroid.getNameOfFrontFacingDevice();
        Log.i("LLLLL", "getNameOfFrontFacingDevice:" + frontCameraDeviceName);
        videoCapturer = VideoCapturerAndroid.create(frontCameraDeviceName);
        Log.i("LLLLL", "VideoCapturerAndroid.create");

        videoConstraints = new MediaConstraints();
        Log.i("LLLLL", "new MediaConstraints");

        videoSource = peerConnectionFactory.createVideoSource(videoCapturer, videoConstraints);
        Log.i("LLLLL", "createVideoSource");
        localVideoTrack = peerConnectionFactory.createVideoTrack("videotest", videoSource);
        Log.i("LLLLL", "createVideoTrack");

        surfaceView = findViewById(R.id.surfaceView);

        VideoRendererGui.setView(surfaceView, new Runnable() {
            @Override
            public void run() {
                //init();
            }
        });

        VideoRendererGui.ScalingType scalingType = VideoRendererGui.ScalingType.SCALE_ASPECT_FILL;
        try {
            localVideoRender = VideoRendererGui.createGui(0, 0, 100, 100, scalingType, true);
            Log.i("LLLLL", "createGui");
        } catch (Exception e) {
            Log.i("LLLLL", "catch");
            e.printStackTrace();
        }

        localVideoTrack.addRenderer(localVideoRender);
        Log.i("LLLLL", "addRenderer");
        stream = peerConnectionFactory.createLocalMediaStream("mytest");
        Log.i("LLLLL", "createLocalMediaStream");
        stream.addTrack(localVideoTrack);
        Log.i("LLLLL", "addTrack");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVideo();

        btn_start = findViewById(R.id.buttonStart);
        btn_stop = findViewById(R.id.buttonStop);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoSource != null) {
                    videoSource.restart();
                }
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoSource != null) {
                    videoSource.stop();
                }
            }
        });
    }
}
