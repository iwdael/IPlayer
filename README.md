# iplayer [![](https://jitpack.io/v/aliletter/iplayer.svg)](https://jitpack.io/#aliletter/iplayer)
iplayer is a player based on ijkplayer, eliminating the need to compile the source code, it can play mp4, avi, rmvb, flv and other formats of video.(iplayer是一款基于ijkplayer的播放器，免去了编译源码的过程，它可以播放mp4、avi、rmvb、flv等格式的视频。)
# How to
To get a Git project into your build:
## Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
## Step 2. Add the dependency

	dependencies {
          compile 'com.github.aliletter:iplayer:v1.0.0'
	}
## Step 3. Set JniLibs directory

```Java
android {
    ...
    sourceSets {
        main() {
            jniLibs.srcDirs = ['libs']
        }
    }
}

```
## Step 4. Copy dynamic library file
Click here [dynamic library file](https://github.com/aliletter/iplayer/raw/master/libs.7z) ,copy the files to your application.

# Instructions
## Step 1. Add Iplayer to the layout file, the fullScreenIconEnable property defaults to true. If true, it can play video full screen and you need to declare this activity(com.aliletter.iplayer.IPlayerActivity) in your AndroidManifest.xml .
```Java
  <com.aliletter.iplayer.widget.IPlayer
        android:id="@+id/iPlayer"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        app:fullScreenIconEnable="false" />
```

## Step 2. Realize iplayer life cycle.
```Java
public class MainActivity extends AppCompatActivity {
    IPlayer iPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iPlayer = (IPlayer) findViewById(R.id.iPlayer);
        iPlayer.setCover(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setVideoUrl("http://gslb.miaopai.com/stream/~t7hYwFO974U4fDLTI3basB81DRAFPYTMjdPgw__.mp4?mpflag=64&vend=1&os=3&partner=4&platform=2&cookie_id=&refer=miaopai&scid=%7Et7hYwFO974U4fDLTI3basB81DRAFPYTMjdPgw__");
    }

    @Override
    protected void onResume() {
        super.onResume();
        iPlayer.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        iPlayer.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iPlayer.onDestroy();

    }
}
```