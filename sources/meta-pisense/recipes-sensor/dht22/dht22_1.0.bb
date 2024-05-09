# dht22_1.0.bb

SUMMARY = "DHT22 Application"
DESCRIPTION = "An application to read DHT22 sensor data and output JSON format"
SECTION = "utils"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${WORKDIR}/README.md;md5=60d8a35b47f7833ab2f2ace8df33718b"

SRC_URI = "file://dht22.c \
           file://dht22_json.c \
           file://README.md"

S = "${WORKDIR}"

do_compile() {
    ${CC} -o dht22.out dht22.c -lwiringPi -lm -Wl,--hash-style=gnu
    sed -i 's/%\n/%%\n/g' dht22.c
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 dht22.out ${D}${bindir}/dht22
}

FILES_${PN} += "${bindir}/"

DEPENDS = "wiringpi"


