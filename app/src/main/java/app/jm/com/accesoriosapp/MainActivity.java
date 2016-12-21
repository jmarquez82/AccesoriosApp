package app.jm.com.accesoriosapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import app.jm.com.accesoriosapp.util.ServerSocket;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

    static int status = 0;
    SurfaceHolder holder;
    private MediaPlayer player;
    private int item = 1;

    public static File carpetaHome = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Source/DeviceAccesorios");
    public static File carpetaVideos = new File(carpetaHome.getAbsoluteFile() + "/videos/");
    public static File carpetaBeacons = new File(carpetaHome.getAbsolutePath()+"/beacons/");
    public static File inactividad = new File(carpetaHome.getAbsolutePath()+"/inactividad/");

    VideoView surface;

    private int url = 0;
    private ArrayList<String> videoList = new ArrayList<String>();

    ServerSocket server;

    String pathDir;
    String pathFile;
    String pathDirectory;

    ImageView imgs;
    LinearLayout llImage, llVideo;
    boolean mRestored = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        server = new ServerSocket(this);

        mRestored = savedInstanceState != null;
        llImage = (LinearLayout) findViewById(R.id.layoutImg);
        llVideo = (LinearLayout) findViewById(R.id.layoutVideo);

        imgs = (ImageView) findViewById(R.id.imgs);
        imgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String conf = (server.getIpAddress() + ":" + server.getPort());
                Toast toast = Toast.makeText(MainActivity.this, "Config =  " + conf, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();

            }
        });



        videoList.add(inactividad+ "/INACTIVIDAD.mov");
        /*videoList.add(pathX + "/Propuesta02_rotado.mp4");
        videoList.add(pathX + "/Propuesta03_rotado.mp4");
        videoList.add(pathX + "/Propuesta06_rotado.mp4");*/

        surface = (VideoView) findViewById(R.id.surface);
        holder = surface.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        player = new MediaPlayer();
        player.setOnPreparedListener(this);

        //VideoControllerView controller = new VideoControllerView(this);
        //player.setLooping(true);
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("ERROR", "MALDITASEA [ " + what + "] { " + extra + " }");
                //Intent i = new Intent(Beacons.this, Beacons.class);
                //startActivity(i);
                // finish();
                return false;
            }
        });


        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                url++;
                if (surface != null) {
                    //current = null;
                    //surface.stopPlayback();
                }

                if (url > videoList.size() - 1) {
                    url = 0;
                }

                try {
                    video(videoList.get(url));
                } catch (Exception ex) {

                }
            }
        });

        try {
            video(videoList.get(0));
        } catch (Exception ex) {
            Log.i("No carga video:", ex.toString());
        }

    }


    public void setVideo(String path) {

        Log.i("Path Video: ", path);
        try{
            String pathFile = carpetaBeacons.getPath() + "/" + path;
            video(pathFile);

        }catch(Exception ex){
            Log.i("Error","No se pudo recibir el video");
        }

    }


    public void video(String url) throws IOException {
        Log.i("Video: ", url);
        if (player != null) {

            player.stop();
            player.reset();
            player.setDataSource(url);
            player.prepareAsync();
            //Log.i(  );
            //player.start();

        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        //if (!mRestored) player.seekTo(player.getDuration());
        //player.seekTo(2);
        player.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDisplay(holder);


    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }


}

