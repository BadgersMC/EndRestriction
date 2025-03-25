# Paper Plugin Template

<!-- modrinth_exclude.start -->
[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/Atrimilan/PaperPluginTemplate/release.yml?branch=master&event=workflow_dispatch&style=flat-square)](https://github.com/Atrimilan/PaperPluginTemplate/actions/workflows/release.yml)
[![GitHub Tag](https://img.shields.io/github/v/tag/Atrimilan/PaperPluginTemplate?style=flat-square)](https://github.com/Atrimilan/PaperPluginTemplate/releases)
[![GitHub License](https://img.shields.io/github/license/Atrimilan/PaperPluginTemplate?style=flat-square)](https://github.com/Atrimilan/PaperPluginTemplate/blob/master/LICENSE)
<!-- modrinth_exclude.end -->

Example project to get started with Paper plugin development and publication.

Available features :
* Automatically send a welcome message to players joining the server.
* `/flyspeed [<player>] <speed>` - Change the flight speed of a player (value between 0.0 and 10.0), if no player is specified, the player executing the command will be used.

<!-- modrinth_exclude.start -->
## 1. Gradle tasks

This project includes a `build.gradle.kts` configuration with useful tasks :

* Running a local Paper Minecraft server :
  * `runDevBundleServer` - Run a local Paper Minecraft server (using : https://github.com/jpenilla/run-task)
  * `buildPluginAndRunServer` - Run a local Paper Minecraft server with a new build of the plugin

* Plugin versioning :
  * `incrementMajorVersion` - Update the first digit of the project version
  * `incrementMinorVersion` - Update the second digit of the project version
  * `incrementPatchVersion` - Update the last digit of the project version

## 2. GitHub Action workflow

This project also includes a [release.yml](./.github/workflows/release.yml) file for publishing new versions of your plugin on CurseForge, Modrinth and GitHub.
Secrets and variables must be defined in the repository **Security** settings :

1. Secrets :
    * `CURSEFORGE_TOKEN` - CurseForge [API token](https://curseforge.com/account/api-tokens)
    * `MODRINTH_TOKEN` - Modrinth [Personal access token](https://modrinth.com/settings/pats) (with **Write projects** and **Create versions** permissions)
2. Variables :
    * `CURSEFORGE_PROJECT_ID` - CurseForge public project ID
    * `MODRINTH_PROJECT_ID` - Modrinth public project ID
> If your prefer to store all your variables in [gradle.properties](./gradle.properties) (**NOT YOUR SECRETS**), or store them in GitHub variables, do not forget to update  [release.yml](./.github/workflows/release.yml) in accordingly.
<!-- modrinth_exclude.end -->