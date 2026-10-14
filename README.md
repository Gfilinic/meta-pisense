# PiSense

**A custom embedded Linux image for Raspberry Pi, with a C++/Qt application for monitoring temperature and humidity.**

PiSense brings together a Yocto-based Linux image, a DHT22 sensor and **HuPiTp**
(HumidityPiTemperature), the graphical application developed for the project.
It was created as the practical part of Goran Filinić's master's thesis at the
Faculty of Organization and Informatics, University of Zagreb.

The project connects application development with embedded Linux integration:
reading a physical sensor, presenting and storing its measurements, packaging
the application and its dependencies, and configuring the graphical environment
on the target device.

**Explore:** [HuPiTp application source](https://github.com/Gfilinic/HuPiTp)
· [Custom Yocto layer](sources/meta-pisense)
· [Master's thesis](https://urn.nsk.hr/urn:nbn:hr:211:957766)

## What the system does

- Reads temperature and humidity from a DHT22 sensor through WiringPi.
- Displays current measurements and a scrolling log in a Qt Quick/QML interface.
- Provides a live chart covering the last five minutes, a daily history chart,
  and monthly charts of daily averages using Qt Charts.
- Stores timestamped readings in a local JSON file for the history views.
- Packages the application, sensor utility, GPIO library and Qt runtime into a
  Raspberry Pi image with Wayland/Weston support.

| Component | Project configuration |
| --- | --- |
| Target board | Raspberry Pi 4, 64-bit (`raspberrypi4-64`) |
| Image build | Yocto Project / OpenEmbedded and BitBake |
| Custom layer compatibility | `nanbield`, as declared in `conf/layer.conf` |
| Application | C++17, Qt 6, Qt Quick/QML, Qt Widgets and Qt Charts |
| Application build | CMake, integrated through Yocto's `qt6-cmake` class |
| Sensor access | DHT22 on BCM GPIO 18, through WiringPi |
| Graphical environment | Wayland and Weston, with the DRM backend |

## Repository structure

This repository is the **build workspace**. The custom layer itself is under
`sources/meta-pisense/`; the other layers and application dependencies are Git
submodules.

| Path | Purpose |
| --- | --- |
| [`build/conf/local.conf`](build/conf/local.conf) | Machine selection and local image/build settings. |
| [`build/conf/bblayers.conf`](build/conf/bblayers.conf) | Enables the layers used by this workspace, including `meta-oe` and `meta-python` from meta-openembedded. |
| [`sources/meta-pisense/conf/layer.conf`](sources/meta-pisense/conf/layer.conf) | Registers the custom layer, its recipe paths and declared Yocto series compatibility. |
| [`sources/meta-pisense/recipes-core/images/`](sources/meta-pisense/recipes-core/images) | Defines the `pisense` image and the `pisense-sdk` development image. |
| [`sources/meta-pisense/recipes-sensor/`](sources/meta-pisense/recipes-sensor) | Builds and packages HuPiTp, the DHT22 utility, WiringPi and the session launch script. |
| [`sources/meta-pisense/recipes-graphics/wayland/`](sources/meta-pisense/recipes-graphics/wayland) | Customizes Weston to use the DRM backend and removes its XWayland option. |
| [`.gitmodules`](.gitmodules) | Records the external repositories and their checkout locations. |

### Submodules

The parent repository records a specific commit for each active submodule.
These include both the Yocto layers and source code consumed by local recipes.

| Checkout path | Role |
| --- | --- |
| `sources/poky` | Yocto reference distribution and build environment, including BitBake and OpenEmbedded-Core. |
| `sources/meta-openembedded` | Additional OpenEmbedded recipes; this workspace enables its `meta-oe` and `meta-python` layers. |
| `sources/meta-raspberrypi` | Raspberry Pi board support and machine configuration. |
| `sources/meta-qt6` | Qt 6 recipes and build integration. |
| `sources/meta-pisense/recipes-sensor/hupitp/files` | [HuPiTp](https://github.com/Gfilinic/HuPiTp): the project's C++/Qt GUI application. |
| `sources/meta-pisense/recipes-sensor/wiringpi/files` | [WiringPi-CMake](https://github.com/Gfilinic/WiringPi-CMake): the WiringPi fork used by the CMake-based GPIO library recipe. |
| `sources/meta-pisense/recipes-sensor/dht22/files` | [DHT22](https://github.com/wookookie/DHT22): the external sensor-reading utility packaged by the `dht22` recipe. |

The application and sensor recipes use `file://` entries to consume source files
from these local `files/` directories. Initializing the submodules is therefore
necessary before building.

## HuPiTp: the GUI application

[HuPiTp](https://github.com/Gfilinic/HuPiTp) was developed as the graphical
application for this system. Its source is maintained separately and integrated
here through a submodule and a BitBake recipe.

The application combines a **Qt Widgets main window** with a **Qt Quick/QML
dashboard** embedded through `QQuickWidget`. Qt Charts provides separate windows
for live measurements and historical statistics.

The source revision recorded by this repository is organized as follows:

| Source file | Responsibility |
| --- | --- |
| `main.cpp` | Creates the application and main window, embeds the QML dashboard, starts sensor acquisition with `QtConcurrent::run`, and connects measurement signals to the interface. |
| `dht22.cpp` / `dht22.h` | Reads the sensor through WiringPi, checks the measurement checksum, converts readings, emits temperature/humidity signals and writes JSON history. |
| `Main.qml`, `Temperature.qml`, `Humidity.qml`, `Log.qml` | Define the dashboard layout, current readings and scrolling measurement log. |
| `mainwindow.cpp` / `mainwindow.h` / `mainwindow.ui` | Define the main window and menu actions, load historical measurements, and create live, daily and monthly charts. |
| `customchartview.cpp` / `customchartview.h` | Add resize handling used by the chart windows. |
| `CMakeLists.txt` | Builds the C++17 application as `appHuPiTp`, includes its QML resources and installs the executable. |

Sensor acquisition runs in a background task with a two-second delay between
read attempts. Successful readings update the interface through Qt signals and
are stored in `sensorDataDHT22.json`, relative to the application's working
directory. The file groups readings by date and records time, humidity, and
temperature in Celsius and Fahrenheit. A shared `QReadWriteLock` coordinates
access to the history file.

HuPiTp contains its own C++ sensor-reading implementation. The separately
packaged `/usr/bin/dht22` utility is also available in the image; the GUI reads
the sensor directly rather than launching that utility.

### Integration into the image

The [`hupitp.bb`](sources/meta-pisense/recipes-sensor/hupitp/hupitp.bb) recipe
inherits `qt6-cmake` to build the application for the target and package it for
the image. The [`pisense.bb`](sources/meta-pisense/recipes-core/images/pisense.bb)
image recipe brings together HuPiTp, the DHT22 utility, Qt packages and the
graphical environment.

The `autostart-weston` recipe installs
[`startApp.sh`](sources/meta-pisense/recipes-sensor/autostart-weston/files/startApp.sh)
as `/etc/profile.d/startApp.sh`. When a login shell sources this profile script,
it prepares the Wayland environment, starts Weston if needed, and launches
`/usr/bin/appHuPiTp`. This is a login-profile launch mechanism; it does not
define a systemd boot service.

## Getting the sources

```bash
git clone https://github.com/Gfilinic/meta-pisense.git
cd meta-pisense
git submodule update --init --recursive
```

Use the recorded submodule revisions when inspecting or reproducing this
project. Updating them to the latest upstream branches can change layer and
application compatibility.

Two details of the existing submodule metadata are worth knowing:

- Several upstream layers use `git://` URLs. If that transport is unavailable
  on your network, configure the corresponding submodule URLs to use the
  upstream project's HTTPS endpoint before fetching them.
- `.gitmodules` also contains an old, duplicated nested HuPiTp path. There is
  no corresponding Git submodule entry in the committed tree at that nested
  location. The active HuPiTp checkout is the path listed in the table above.

## Build reference

The repository preserves the project's 2024 build configuration. The custom
layer declares Nanbield compatibility; consult the
[Yocto 4.3 host requirements](https://docs.yoctoproject.org/4.3/ref-manual/system-requirements.html)
for the corresponding host packages and supported distributions, and check
compatibility against the actual Poky and layer revisions you use.

**Build status:** the commands below identify the intended build entry points.
A clean image build has not been revalidated for this documentation. Some
recipes still contain legacy override names such as `RDEPENDS_${PN}` and
`FILES_${PN}`; these require migration to the colon override syntax used by
Nanbield. See the
[Yocto override syntax migration guide](https://docs.yoctoproject.org/4.3/migration-guides/migration-3.4.html#override-syntax-changes).

After initializing the submodules and resolving recipe compatibility, run from
the repository root:

```bash
source sources/poky/oe-init-build-env build
bitbake pisense
```

The environment setup enters `build/` and uses the checked-in configuration.
With the default target, image artifacts are generated under
`build/tmp/deploy/images/raspberrypi4-64/`, relative to the repository root.

The repository also provides a development image target:

```bash
bitbake pisense-sdk
```

`pisense-sdk` extends `pisense` with development packages, debugging tools and
kernel sources. To request an installable cross-development SDK for the main
image, use the SDK task from the initialized build environment:

```bash
bitbake pisense -c populate_sdk
```

SDK installers are generated under `build/tmp/deploy/sdk/`.

## Master's thesis

This project accompanies **Goran Filinić's 2024 master's thesis**, *Usporedba
razvojnih okolina za Internet stvari* (*Comparison of Development Environments
for the Internet of Things*), at the Faculty of Organization and Informatics,
University of Zagreb.

The thesis provides the academic background for the practical work presented
in this repository. It is written in Croatian.

[Read the published thesis](https://urn.nsk.hr/urn:nbn:hr:211:957766)
· [Repository record](https://dabar.srce.hr/en/object/foi:8157)

## License and third-party components

The root repository contains a [BSD 3-Clause license](LICENSE).
Submodules, upstream packages and individual recipes have their own license
terms and declarations; the root license does not replace those terms.
