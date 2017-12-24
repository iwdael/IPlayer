# iplayer [![](https://jitpack.io/v/aliletter/iplayer.svg)](https://jitpack.io/#aliletter/iplayer)
Iplayer is a player based on ijkplayer, which saves the process of compiling source code. It can play video or local video in mp4, avi, RMVB, FLV and other formats.[中文文档](https://github.com/aliletter/iplayer/blob/master/README_CHINESE.md)
## Instruction
Iplayer supports video dragging, pausing, playing, and playing video. Through setOnIPlayerStatusListener method can listen to the player's various state changes, the user can implement some special operation.
### Code Sample
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
```Java
  <com.aliletter.iplayer.widget.IPlayer
        android:id="@+id/iPlayer"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        app:fullScreenIconEnable="false" />
```

## How to
To get a Git project into your build:
### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories.   [click here for details](https://github.com/aliletter/CarouselBanner/blob/master/root_build.gradle.png)
```Java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### Step 2. Add the dependency
Add it in your application module build.gradle at the end of dependencies where you want to use.[click here for details](https://github.com/aliletter/CarouselBanner/blob/master/application_build.gradle.png)
```Java
	dependencies {
                ...
	        compile 'com.github.aliletter:iplayer:v1.1.0'
	}
```
### Step 3. Set JniLibs directory
Add it in your application module build.gradle.[click here for details](https://github.com/aliletter/gifengine/blob/master/jnilibs.png)
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
### Step 4. Add the permission
Add it in your application AndroidManifest.xml in the manifest label.   [click here for details](https://github.com/aliletter/OnHttp/blob/master/androimanifest.png)
```Java
    <uses-permission android:name="android.permission.INTERNET" />
```
### Step 5. Copy dynamic library file
Click [here](https://raw.githubusercontent.com/aliletter/iplayer/master/libs.7z) ,unzip and copy the files to your application libs directory.
[click here for details](https://github.com/aliletter/gifengine/blob/master/libs.png)
<br>![Text Image](https://github.com/aliletter/iplayer/blob/master/iplayer.gif)
<br><br><br>
## Thank you for your browsing
If you have any questions, please join the QQ group. I will do my best to answer it for you. Welcome to star and fork this repository, alse follow me.
<br>
![Image Text](https://github.com/aliletter/CarouselBanner/blob/master/qq_group.png)

