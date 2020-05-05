# Byte Sculptor Lib
Basics for my Android apps. 
Trying to collect in this lib classes, layouts, icons, strings/translations and other files I use in most of my apps.

## Integration in an app project
- Create a new folder on the project top (where the folders app, gradle, etc. are). Give it a name, but not 'bytesculptorlib'. I name it 'extlib'. So I can add more libs as submodules later to this folder.
- Add the lib as a git subrepo in the new folder: `git submodule add https://github.com/bytesculptor/bytesculptorlib.git`

- Open the app project, go to `File -> New -> Import Module -> Import Gradle Project` and select the folder 'bytesculptorlib' in the project path you just added as subrepo
- Add in the build.gradle file of the app project in the dependencies: <b>implementation project(':bytesculptorlib')</b>
