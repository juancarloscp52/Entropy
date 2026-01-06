# Change Log

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/) and this project adheres to [Semantic Versioning](http://semver.org/).

Find your version by looking for the file for your mod-loader:

- Entropy-`<mod-version>`+mc`<mc-version>`.jar

## [1.15] - 2026-01-06

Special thanks to the Entropy Contributors: bl4ckscor3, Kanawanagasaki and slicedlime! <3

Available versions:
- Minecraft 1.21.8, 1.21.10

### Added

- Allow multiple integrations to be active at the same time
- Added setting for the YouTube polling interval
- The event queue now shows fake teleport events as fake once they're done

### New Events

- Racing Balloon
- Rainbow Fog

### Changed/Fixed

- The integration settings have been reworked
- Fixed Teleport To Spawn event not teleporting to the actual world spawn
- Fixed Midas Touch event converting the whole inventory instead of only held items
- Fixed fake teleport events not being identifiable during voting
- Fixed empty progress bar rendering for some events when there is no progress
- Changes to Loyal Companion event:
  - Horses now spawn with random health instead of just one heart
  - Horses are now tamed and owned by the player
- Fixed players always being teleported to the overworld for random teleport events
- The Far Random TP event now spreads players relative to their own position instead of relative to 0, 0
- Fixed Midas Touch event killing swimming players
- The Blur event is now disabled when accessibility mode is active
- Fixed random teleport events not working without operator permissions
- Fixed Haunted Chests sounds not playing at the chests' position
- Fixed Half-Hearted event resetting the maximum amount of health a player has
- Fixed Spawn Pet event spawning pets in walls

## [1.14] - 2025-06-18

Special thanks to the Entropy Contributors: bl4ckscor3, Kanawanagasaki and slicedlime! <3

Available versions:
- Minecraft 1.21.6

### New Events

- Bouncy Blocks

## [1.13.2] - 2025-03-10

Special thanks to the Entropy Contributors: bl4ckscor3, Kanawanagasaki and slicedlime! <3

Available versions:
- Minecraft 1.21.4

### Changed/Fixed

- Fixed issue that broke integration votes.

## [1.13.1] - 2025-03-09

Special thanks to the Entropy Contributors: bl4ckscor3, Kanawanagasaki and slicedlime! <3

Available versions:
- Minecraft 1.21.3, 1.21.4

### Changed/Fixed

- Reworked Herobrine Removed Event fog to be more eerie :)

## [1.13] - 2025-03-01 2025-03-08

Special thanks to the Entropy Contributors: bl4ckscor3, Kanawanagasaki and slicedlime! <3

Available versions:
- Minecraft 1.20.1, 1.20.2, 1.20.4, 1.20.6, 1.21.1

### New Events
- Nothing
- Silence
- Rainbow Trails
- Spawn Rainbow Sheep
- jeb_
- Armor Trims

### Changed/Fixed

- Increased base event and timer duration settings to accept up to 180 seconds.
- One Heart event has been revamped and is now called "Half-Hearted".
- Improved Stuttering, Midas Touch and So Sweet! Events.
- Entropy Overlay now hides when Debug F3 is being shown.
- Improved German translations.
- Added Brazilian Portuguese translations.
- Small fixes to various events.

## [1.12] - 2023-03-14

For releases prior to Entropy 1.13, please check the changelog attached to the version download page.
