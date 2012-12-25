#!/bin/bash

set -e

_JAVA_OPTIONS='-Dawt.useSystemAAFontSettings=lcd -Xms256m -Xmx512m -XX:MaxHeapSize=256m'
APP_NAME="ReBudget"
PACK="com.sbar.rebudget"

# FIXME
cd ~/git/rebudget

./clear.sh

#android create project --name "${APP_NAME}" --target "android-15" --path .\
#--activity MainActivity --package "${PACK}"

android update project --name "${APP_NAME}" --path . --target "android-15"
ant debug

#adb install -r bin/*-debug.apk
adb uninstall "${PACK}" && adb install -r bin/*-debug.apk


#webput bin/*-debug.apk b.apk
