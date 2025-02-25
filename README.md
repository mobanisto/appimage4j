# appimage4j

The appimage4j project is a JVM Toolkit for creating, manipulating and
extracting AppImage packages.

## License

This library is released under the terms of the GNU Lesser General Public
License.

See [LGPL.md](LGPL.md) and [GPL.md](GPL.md) for details.

## Usage

### Prerequisites

Some functionality requires a JVM >= 13 in order to work properly.
In particular, extracting AppImage packages only works on Java 13+.
This is because AppImage packages often contain symbolic links and due to
[a bug](https://bugs.openjdk.org/browse/JDK-8220793) only fixed in 2019,
the JVM is not able to modify the file properties of symbolic links.

### Building

Build the project using Gradle:

    ./gradlew clean createRuntime

### Running

The main command line executable of the project is this:

    ./scripts/appimage4j

It accepts a subtask, any of:

* `create`
* `extract`
* `dump-info`

Any of the subtasks accepts its own set of command line arguments.

#### Creating an AppImage package

To create an AppImage self-contained executable, run this, providing a
properly prepared app image direcotry as input:

    ./scripts/appimage4j create --input <app impage dir> --output <app image>

#### Extracting an AppImage package

You can extract any existing AppImage package by extracting the files
contained within the embedded squashfs filesystem:

    ./scripts/appimage4j extract --input <app image> --output <app image dir>

#### Dumping info about an AppImage package

This command displays a number of debug information about an AppImage file.
Currently, it displays the file size, embedded executable size and the
squashfs filesystem offset within the app image.
It also dumps some information about the contained squashfs filesystem:
it shows some debug information about superblocks and tables of the squashfs
and lists all the files and directories contained within.

    ./scripts/appimage4j dump-info --input <app image>
