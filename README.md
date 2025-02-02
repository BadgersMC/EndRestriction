# Paper Plugin Template

<!-- modrinth_exclude.start -->
[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/Atrimilan/PaperPluginTemplate/release.yml?branch=master&event=workflow_dispatch&style=flat-square)](https://github.com/Atrimilan/PaperPluginTemplate/actions/workflows/release.yml)
[![GitHub Tag](https://img.shields.io/github/v/tag/Atrimilan/PaperPluginTemplate?style=flat-square)](https://github.com/Atrimilan/PaperPluginTemplate/releases)
[![GitHub License](https://img.shields.io/github/license/Atrimilan/PaperPluginTemplate?style=flat-square)](https://github.com/Atrimilan/PaperPluginTemplate/blob/master/LICENSE)
<!-- modrinth_exclude.end -->

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

<!-- modrinth_exclude.end -->