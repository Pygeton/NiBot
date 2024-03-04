@echo off
cd /d D:\Software\leidian\LDPlayer9
adb start-server
adb -s emulator-5554 forward tcp:9099 tcp:5800
adb -s emulator-5554 shell am start -n com.fuqiuluo.shamrock
adb -s emulator-5554 shell am start -n com.tencent.mobileqq/.activity.SplashActivity