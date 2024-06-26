SUMMARY = "HumidityPiTemperature project for Raspberry Pi to read and display data from DHT22 sensor"
DESCRIPTION = "This project reads data from a DHT22 sensor connected to a Raspberry Pi and displays the temperature and humidity using Qt Quick."
HOMEPAGE = "https://github.com/yourusername/hupitp"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=aa2f8d409dd6bbcfc8e5c96c76fd88b7"
S = "${WORKDIR}"
SRC_URI = "file://CMakeLists.txt \
	   file://LICENSE \
	   file://dht22.cpp \
	   file://dht22.h \
	   file://Humidity.qml \
	   file://main.cpp \
     file://mainwindow.cpp \
     file://mainwindow.h \
     file://mainwindow.ui \
     file://resources.qrc \
     file://customchartview.h \
     file://customchartview.cpp \
	   file://Main.qml \
	   file://Temperature.qml \
	   file://Log.qml"


DEPENDS += "wiringpi qtquick3d qtdeclarative qtdeclarative-native qtbase qtcharts qtquick3d-native"
RDEPENDS_${PN} += "libxcursor qtwayland qtquick3d-tools"


inherit qt6-cmake

FILES_${PN} += "${datadir}/hupitp/*"
