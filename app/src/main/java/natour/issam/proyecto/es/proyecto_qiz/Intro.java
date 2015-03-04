package natour.issam.proyecto.es.proyecto_qiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

//Implement SurfaceHolder interface to Play video
//Implement this interface to receive information about changes to the surface
public class Intro extends Activity implements SurfaceHolder.Callback{

    MediaPlayer mediaPlayer;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Button buttonSaltarVideo;
    VideoView mVideoView;
    View view;
    boolean pausing = false;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //Button buttonPlayVideo = (Button)findViewById(R.id.playvideoplayer);

        getWindow().setFormat(PixelFormat.UNKNOWN);

        //Displays a video file.
        mVideoView = (VideoView)findViewById(R.id.videoview);
        buttonSaltarVideo = (Button)findViewById(R.id.buttonSaltarVideo);

        String uriPath = "android.resource://" + getPackageName() + "/"+ R.raw.intro;
        Uri uri = Uri.parse(uriPath);
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.start();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent pasar = new Intent(getApplicationContext(),Panel_Inicio.class);
                startActivity(pasar);
            }
        });


/**
        buttonPlayVideo.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {

                // VideoView refference see main.xml
                VideoView mVideoView = (VideoView)findViewById(R.id.videoview);

                String uriPath = "android.resource://" + getPackageName() + "/"+R.raw.video;

                Uri uri = Uri.parse(uriPath);
                mVideoView.setVideoURI(uri);
                mVideoView.requestFocus();
                mVideoView.start();


            }});**/
    }
    public void saltarsevideo(View view){
        Intent pasar = new Intent(getApplicationContext(),Panel_Inicio.class);
        startActivity(pasar);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }
}