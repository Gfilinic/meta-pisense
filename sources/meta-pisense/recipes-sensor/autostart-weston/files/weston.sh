#!/bin/sh

rm /etx/xdg/weston/weston.ini
cat <<EOF > /etc/xdg/weston/weston.ini
[core]
backend=drm-backend.so
EOF

# Create temporary directory for XDG_RUNTIME_DIR
mkdir -p /tmp/$USER-weston
chmod 0700 /tmp/$USER-weston
export XDG_RUNTIME_DIR=/tmp/$USER-weston


# Set WAYLAND_DISPLAY
export WAYLAND_DISPLAY=wayland-1

# cp /usr/bin/startApp.sh $HOME/startApp.sh
# chmod +x $HOME/startApp.sh

# Start Weston
weston &


