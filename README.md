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
