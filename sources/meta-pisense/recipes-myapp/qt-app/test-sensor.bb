SUMMARY = "Test program to read input from sensor"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

DEPENDS += "wiringpi"

SRC_URI = "file://test-sensor.c"

S = "${WORKDIR}"

CFLAGS += "-I${STAGING_INCDIR}"
LDFLAGS += "-L${STAGING_LIBDIR}"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} -o test_sensor ${S}/test-sensor.c -lwiringPi
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/test_sensor ${D}${bindir}
}

FILES_${PN} = "${bindir}/test_sensor"

