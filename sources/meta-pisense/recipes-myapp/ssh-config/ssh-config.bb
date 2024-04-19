DESCRIPTION = "Recipe for configuring Dropbear SSH server"
LICENSE = "CLOSED"

# Specify dependencies
DEPENDS += "dropbear"

# Specify the path to the post-install script
do_install() {
    # Include the contents of pisense.pub in the root's .ssh/authorized_keys
    install -d ${D}/home/root/.ssh
    install -m 0644 /home/goc/.ssh/pisense.pub ${D}/home/root/.ssh/authorized_keys
    # Set permissions for .ssh directory
    chmod 700 ${D}/home/root/.ssh

    # Set permissions for authorized_keys file
    chmod 600 ${D}/home/root/.ssh/authorized_keys
}

# Specify the post-install script directory
do_install[dirs] = "${D}"

# Specify the directory containing the post-install script
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

do_generate_dropbear_keys() {
    # Create directory to store Dropbear keys if it doesn't exist
    install -d ${D}${sysconfdir}/dropbear

    # Generate Dropbear RSA key
    ${STAGING_BINDIR_NATIVE}/dropbearkey -t rsa -f ${D}${sysconfdir}/dropbear/dropbear_rsa_host_key

    # Generate Dropbear DSS key
    ${STAGING_BINDIR_NATIVE}/dropbearkey -t dss -f ${D}${sysconfdir}/dropbear/dropbear_dss_host_key
}

addtask generate_dropbear_keys after do_install before do_build

FILES:${PN} += "/home/root/.ssh/*"

