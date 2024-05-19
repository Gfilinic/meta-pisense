#!/bin/sh

# Create the directory and set permissions
mkdir -p /tmp/$USER-weston
chmod 0700 /tmp/$USER-weston

# Set XDG_RUNTIME_DIR
export XDG_RUNTIME_DIR=/tmp/$USER-weston

# Start Weston
weston

