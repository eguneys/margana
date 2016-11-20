set apkpath="build/outputs/apk/anagram-release-unsigned.apk"
set alignedapkpath="build/outputs/apk/anagram-release-aligned.apk"

echo $apkpath
                
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore debug.keystore %apkpath% android

jarsigner -verify -verbose -certs %apkpath%

del %alignedapkpath%

zipalign.exe -v 4 %apkpath% %alignedapkpath%
