
DESCRIPTION = "WiringPi is a PIN based GPIO access library written in C for Raspberry Pi boards."
LICENSE = "LGPL-3.0+"
LIC_FILES_CHKSUM = "file://COPYING.LESSER;sha256=8d4b523100505d9f69ce4984322feddb46f4c3bf2a7533dfcaf686fa5a6daa91"

SRC_URI = "https://github.com/WiringPi/WiringPi/releases/download/3.2/wiringpi_3.2_arm64.deb"

S = "${WORKDIR}"

do_install() {
    dpkg -x ${S}/wiringpi_3.2_arm64.deb ${D}
}

FILES_${PN} += "${bindir}/gpio \
                ${bindir}/gpio-admin \
                ${includedir}/wiringPi.h \
                ${includedir}/wiringSerial.h \
                ${libdir}/libwiringPi.so \
                ${datadir}/man/man1/gpio.1.gz \
                ${datadir}/man/man1/gpio-admin.1.gz \
               "
