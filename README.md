# Fastpath

Fastpath is a Fabric mod for Minecraft 1.21.8 that optimizes **chunk section rebuilds** and **rendering performance**.  
It introduces:
- Flag-based geometry change detection (no more expensive hash scans)
- Compile throttling per tick for smoother frame pacing
- Smart skipping of empty sections and light-only updates
- Optional aggressive culling (experimental)

## Features
- ⚡ Faster chunk rebuilds
- 🎯 Stable FPS with rebuild throttling
- 🔍 HUD overlay with live diagnostics
- 🛠 Configurable via `fastpath.json`

## Installation
1. Install [Fabric Loader](https://fabricmc.net/) and [Fabric API](https://modrinth.com/mod/fabric-api).
2. Drop the Fastpath `.jar` into your `mods/` folder.
3. Launch Minecraft.

## Configuration
Edit `config/fastpath.json` to tweak:
- `skipEmptySections`
- `skipLightOnly`
- `maxCompilesPerTick`
- `aggressiveCulling`

## License
This project is licensed under the [MIT License](LICENSE).