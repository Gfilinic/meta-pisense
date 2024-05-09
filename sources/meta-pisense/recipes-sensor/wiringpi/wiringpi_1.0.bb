DESCRIPTION = "A library to control Raspberry Pi GPIO channels"
HOMEPAGE = "https://projects.drogon.net/raspberry-pi/wiringpi/"
SECTION = "devel/libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING.LESSER;md5=e6a600fd5e1d9cbde2d983680233ad02"

SRC_URI[md5sum] = "492aa9e6edcb42abb1e724a3bb9d11ef"
SRC_URI[sha256sum] = "a383cfa1a7bd6e1c0e96645d04d90cbeda2c62712183921b6661f02225961037"

SRC_URI = "\
    file://CMakeLists.txt \
    file://version.h \
    file://COPYING.LESSER \
    file://debian \
    file://debian-template \
    file://devLib \
    file://gpio \
    file://wiringPi/ \
    file://wiringPiD/ \
"

SRC_URI[md5sum] = "c227be5416f474bd3022eb3d3fb0ee2d"
SRC_URI[sha256sum] = "32a9c2f1f992fd70b1f61de9f58733a226a68d65e947f1fe672a94b03d9c292f"

S = "${WORKDIR}/wiringPi"

inherit cmake

DEPENDS += "virtual/kernel libxcrypt"

EXTRA_OECMAKE += "-DWIRINGPI_SUDO:BOOL=OFF"

FILES_${PN} = "${libdir}/* ${includedir}/*"
FILES_${PN}-dev += "${libdir}/* ${includedir}/*"
FILES_${PN}-dbg += "${libdir}/*"

BBCLASSEXTEND = "native"
