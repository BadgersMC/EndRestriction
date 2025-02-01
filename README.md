# Paper Plugin Template

Example project to get started with Paper plugin development.

<!-- modrinth_exclude.start -->

---

This project includes a `build.gradle.kts` configuration with several tasks:

* Running a local Paper Minecraft server:
  * `runDevBundleServer` - Run a local Paper Minecraft server (source: https://github.com/jpenilla/run-task)
  * `buildPluginAndRunServer` - Run a local Paper Minecraft server with a new build of the plugin
* Plugin versioning:
  * `incrementMajorVersion` - Update the first digit of the project version
  * `incrementMinorVersion` - Update the second digit of the project version
  * `incrementPatchVersion` - Update the last digit of the project version
* Plugin deployment:
  * `modrinth` - Deploy the plugin to [Modrinth](https://modrinth.com)

---

You must configure your environment variables first:

* Windows
  ```bat
  setx MODRINTH_TOKEN "your-modrinth-token"
  ```

* Bash
  ```bash
  export MODRINTH_TOKEN="your-modrinth-token"
  ```

<!-- modrinth_exclude.end -->