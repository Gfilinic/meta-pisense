DESCRIPTION = "Custom Raspberry Pi image with simple GUI for collecting temperature and humidity data"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit core-image


IMAGE_FEATURES += "splash package-management ssh-server-dropbear hwcodecs weston "

inherit extrausers
PASSWD = "\$6\$JwrqgzUt5N2MjceA\$xxSg85lwTFCVG6lVxnHeu42bPZr114GYj/8NlPV6myHrGn9xwL0lX9nupwShZv8izDCMXABI7zXI0SiIzO00x."
EXTRA_USERS_PARAMS = "\
    usermod -p '${PASSWD}' root; \
    "


# Add your custom application package to be installed
IMAGE_INSTALL += "raspi-gpio \
		  dht22 \
		  hupitp \
		  ${Qt_UTILS}"
		   
Qt_UTILS = "qtbase \
            qtquick3d \
            qtdeclarative \
            qtwayland \
            qtgraphs"
            
RDEPENDS_${PN} = "qtwayland"      
PACKAGECONFIG += " qtwayland wayland libinput fontconfig"

QT_QPA_PLATFORM = "wayland"

QB_MEM = "-m 512"
