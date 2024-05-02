SUMMARY = "DHTXXD - A utility to read DHT11/DHT21/DHT22/DHT33/DHT44 sensors"
DESCRIPTION = "A utility to read temperature and humidity data from DHT11/DHT21/DHT22/DHT33/DHT44 sensors connected to Raspberry Pi GPIO"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://DHTXXD_README;md5=4b37e365351ca0909893ec6e61f6c66b"

SRC_URI = "file://DHTXXD.c \
           file://DHTXXD.h \
           file://test_DHTXXD.c"

S = "${WORKDIR}"

do_compile() {
    ${CC} ${LDFLAGS} -Wall -pthread -o DHTXXD test_DHTXXD.c DHTXXD.c
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 DHTXXD ${D}${bindir}
}

FILES_${PN} = "${bindir}/DHTXXD"
