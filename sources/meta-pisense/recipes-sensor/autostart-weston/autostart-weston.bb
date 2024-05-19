# Copyright (C) 2024 Your Company
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Script to start Weston"
LICENSE = "CLOSED"

SRC_URI = "file://weston.sh"

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/weston.sh ${D}${sysconfdir}/init.d/weston-autostart
}

PACKAGES = "${PN}"
FILES_${PN} = "${sysconfdir}/init.d/weston-autostart"

