DESCRIPTION = "Scripts to start Weston and an application"
LICENSE = "CLOSED"

SRC_URI = "file://weston.sh \
           file://startApp.sh"

do_install() {
    # Install weston-autostart script
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/weston.sh ${D}${sysconfdir}/init.d/weston-autostart

    # Create symlinks to ensure the script runs at startup
    install -d ${D}/etc/rc.d/rc5.d
    ln -sf ${sysconfdir}/init.d/weston-autostart ${D}/etc/rc.d/rc5.d/S99weston-autostart
}

PACKAGES = "${PN}"
FILES_${PN} += "${sysconfdir}/init.d/weston-autostart \
                ${base_dir}/home/root/startApp.sh \
                /etc/rc.d/rc5.d/S99weston-autostart"

