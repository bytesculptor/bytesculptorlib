# Byte Sculptor Lib
Collecting in this library some classes, layouts, icons, strings/translations and other files I use for my apps.

## Integration in an app project
- In your Android project, go to the folder "app", create a folder "libs" (or wherever you prefer) and add the lib as a git subrepo: `git submodule add https://github.com/bytesculptor/ByteSculptorLib.git`

- Open the app project, go to `File -> New -> Import Module` and select the folder 'ByteSculptorLib' in the project path you just added as subrepo
- Add in the `build.gradle` file of the app project in the dependencies: <b>implementation project(':ByteSculptorLib')</b>

License
-------

    Copyright 2021 Byte Sculptor Software

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
  
