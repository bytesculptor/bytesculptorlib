# Byte Sculptor Lib
Basics for my Android apps. 
Trying to collect in this lib classes, layouts, icons, strings/translations and other files I use in most of my apps.

## Integration in an app project
- In your Android project, go to the folder app/libs and add the lib as a git subrepo: `git submodule add https://github.com/bytesculptor/bytesculptorlib.git`

- Open the app project, go to `File -> New -> Import Module` and select the folder 'bytesculptorlib' in the project path you just added as subrepo
- Add in the build.gradle file of the app project in the dependencies: <b>implementation project(':bytesculptorlib')</b>
