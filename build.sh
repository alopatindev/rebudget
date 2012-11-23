#!/bin/bash

set -e

_JAVA_OPTIONS='-Dawt.useSystemAAFontSettings=lcd -Xms256m -Xmx512m -XX:MaxHeapSize=256m'
APP_NAME="ReBudget"
PACK="com.sbar.rebudget"

# FIXME
cd ~/coding/github/rebudget

#android create project --name "${APP_NAME}" --target "android-15" --path .\
#--activity MainActivity --package "${PACK}"

android update project --name "${APP_NAME}" --path . --target "android-15"
ant debug

webput bin/*-debug.apk b.apk
