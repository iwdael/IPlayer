# iplayer [![](https://jitpack.io/v/aliletter/iplayer.svg)](https://jitpack.io/#aliletter/iplayer)
iplayer是一款基于ijkplayer的播放器，免去了编译源码的过程，它可以播放mp4、avi、rmvb、flv等格式的网络视频或者本地视频。
## 使用说明
iplayer支持视频的拖动，暂停，播放，屏播放视频。通过setOnIPlayerStatusListener方法可以监听到播放器的各种状态的改变，使用者可以实现一些特别操作。
### Code Sample
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

## 如何配置
将本仓库引入你的项目:
### Step 1. 添加JitPack仓库到Build文件
合并以下代码到项目根目录下的build.gradle文件的repositories尾。[点击查看详情](https://github.com/aliletter/CarouselBanner/blob/master/root_build.gradle.png)
```Java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### Step 2. 添加依赖   
合并以下代码到需要使用的application Module的dependencies尾。[点击查看详情](https://github.com/aliletter/CarouselBanner/blob/master/application_build.gradle.png)
```Java
	dependencies {
                ...
	        compile 'com.github.aliletter:iplayer:v1.1.0'
	}
```
### Step 3. 设置JniLibs目录
合并以下代码到你的application module的build.gradle。[点击查看详情](https://github.com/aliletter/gifengine/blob/master/jnilibs.png)
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
### Step 4. 添加权限
合并以下代码到应用的AndroidManifest.xml的manifest标签中。[点击查看详情](https://github.com/aliletter/OnHttp/blob/master/androimanifest.png)
```Java
    <uses-permission android:name="android.permission.INTERNET" />
```
### Step 5. 复制动态库文件
点击[这里](https://raw.githubusercontent.com/aliletter/iplayer/master/libs.7z) ,解压并复制文件到libs目录。[点击查看详情](https://github.com/aliletter/gifengine/blob/master/libs.png)
<br><br><br>
## 感谢浏览
如果你有任何疑问，请加入QQ群，我将竭诚为你解答。欢迎Star和Fork本仓库，当然也欢迎你关注我。
<br>
![Image Text](https://github.com/aliletter/CarouselBanner/blob/master/qq_group.png)
