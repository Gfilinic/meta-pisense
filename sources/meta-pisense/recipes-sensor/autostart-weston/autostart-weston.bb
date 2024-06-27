SUMMARY = "Environment setup script"
LICENSE = "CLOSED"


PR = "r0"

SRC_URI = "file://startApp.sh"
	
S = "${WORKDIR}"

do_install() {
    install -d ${D}${sysconfdir}/profile.d
    install -m 0755 ${S}/startApp.sh ${D}${sysconfdir}/profile.d/startApp.sh
}

FILES_${PN} += "${sysconfdir}/profile.d/startApp.sh"

