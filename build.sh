#!/bin/bash

set -e

_JAVA_OPTIONS='-Dawt.useSystemAAFontSettings=lcd -Xms256m -Xmx512m -XX:MaxHeapSize=256m'
APP_NAME="ReBudget"
PACK="com.sbar.rebudget"
ANDROID_SDK="/opt/android-sdk-update-manager"

# FIXME
cd ~/git/rebudget

./clear.sh

ctags -R .

#android create project --name "${APP_NAME}" --target "android-15" --path .\
#--activity MainActivity --package "${PACK}"

android update project --name "${APP_NAME}" --path . --target "android-15"

mkdir libs/
ln -s $ANDROID_SDK/extras/android/support/v4/android-support-v4.jar libs/

#ant '-Djava.compilerargs=-Xlint:unchecked -Xlint:deprecation' debug
ant debug

adb install -r bin/*-debug.apk
#adb uninstall "${PACK}" && adb install -r bin/*-debug.apk

webput bin/*-debug.apk b.apk
