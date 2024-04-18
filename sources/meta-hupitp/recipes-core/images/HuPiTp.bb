DESCRIPTION = "Custom Raspberry Pi image with simple GUI for collecting temperature and humidity data"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit core-image-weston

# Set passwords for root and pi users
USER_PASSWORD_ROOT = "root"
USER_HASH_ROOT = "${@bb.utils.md5(bb.utils.contains('DISTRO_FEATURES', 'pam', 'pam', '', d) + ':' + d.getVar('USER_PASSWORD_ROOT', True) + ':' + d.getVar('FILESPATH', True), d)}"
USER_PASSWORD_PI = "pi"
USER_HASH_PI = "${@bb.utils.md5(bb.utils.contains('DISTRO_FEATURES', 'pam', 'pam', '', d) + ':' + d.getVar('USER_PASSWORD_PI', True) + ':' + d.getVar('FILESPATH', True), d)}"

EXTRA_USERS_PARAMS = "usermod -P '${USER_HASH_ROOT}' root; \
                       usermod -P '${USER_HASH_PI}' pi;"


# Add your custom application package to be installed
#IMAGE_INSTALL_append = " my-app"

