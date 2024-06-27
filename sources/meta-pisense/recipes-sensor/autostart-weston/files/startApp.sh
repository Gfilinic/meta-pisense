#!/bin/sh
mkdir -p /tmp/weston
chmod 0700 /tmp/weston
export XDG_RUNTIME_DIR=/tmp/weston
export WAYLAND_DISPLAY=wayland-1
if ! pgrep -x "weston" > /dev/null
then
  weston &
  sleep 2
fi

export QT_QPA_PLATFORM=wayland
export LANG=C.UTF-8
export LC_ALL=C.UTF-8
/usr/bin/appHuPiTp
