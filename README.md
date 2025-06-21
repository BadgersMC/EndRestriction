# EndRestriction

A Minecraft Paper plugin that adds time-based restrictions to End dimension access. This plugin prevents players from locating strongholds and activating End portals until a specified time has passed.

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/dcb799ff07534090ae8c48021ece6b35)](https://app.codacy.com/gh/BadgersMC/EndRestriction?utm_source=github.com&utm_medium=referral&utm_content=BadgersMC/EndRestriction&utm_campaign=Badge_Grade)
[![GitHub License](https://img.shields.io/github/license/BadgersMC/EndRestriction?style=flat-square)](https://github.com/BadgersMC/EndRestriction/blob/main/LICENSE)

## Features

- 🕒 Time-based End dimension access restriction
- 🚫 Prevents players from locating strongholds before the unlock time
- 🔒 Blocks End portal activation until the specified time
- 📊 PlaceholderAPI support for dynamic time display
- ⚙️ Fully configurable unlock time and messages

## PlaceholderAPI Integration

This plugin provides the following placeholders:

- `%endrestrict_time%` - Shows the configured unlock time
- `%endrestrict_status%` - Displays whether the End is currently "Locked" or "Unlocked"
- `%endrestrict_remaining%` - Shows the time remaining until unlock in a human-readable format

## Configuration

The plugin is highly configurable through the `config.yml` file. You can customize:

- Unlock date and time
- Messages and notifications
- Permission settings
- Portal activation restrictions

## Installation

1. Download the latest release from the [Releases](https://github.com/BadgersMC/EndRestriction/releases) page
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin in `plugins/EndRestriction/config.yml`

## Dependencies

- Paper 1.20.x or newer
- PlaceholderAPI (optional, for placeholders support)

## Building from Source

```bash
./gradlew build
```

The built jar will be in `build/libs/`.

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/BadgersMC/EndRestriction/issues) page
2. Create a new issue if your problem hasn't been reported
3. Provide as much detail as possible, including:
   - Server version
   - Plugin version
   - Error messages
   - Steps to reproduce
