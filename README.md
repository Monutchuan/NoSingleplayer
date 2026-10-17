# OnlyMultiplayer

A **client-side Forge mod** for Minecraft 1.12.2 that suppresses the **Singleplayer** button on the main menu.

> Note: this is a **mod**, not a server plugin. The Singleplayer button lives on the client's own main menu, so it can only be changed from the client side. Installing this mod on a dedicated server has no effect on players' buttons.

## What it does

When the main menu (`GuiMainMenu`) opens, the mod finds the `menu.singleplayer` button and handles it according to the configured mode:

| Mode      | Behavior                                                        | Default |
|-----------|----------------------------------------------------------------|---------|
| `remove`  | The button is removed entirely from the main menu.             | ✅ yes  |
| `disable` | The button stays visible but is greyed out and cannot be clicked. | no   |

## Configuration

The mode is controlled by `config/onlymultiplayer.cfg` (created on first launch):

```cfg
# How to handle the Singleplayer button: 'remove' = hide it entirely, 'disable' = grey it out but keep it visible.
mode=remove
```

You can also change it in-game from the **Mods** list → select *OnlyMultiplayer* → **Config**.

Valid values: `remove` (or anything unrecognized falls back to `remove`) and `disable`.

## Build

This project uses ForgeGradle 2.3.2 and ships with the Gradle wrapper (`gradlew` / `gradlew.bat` / `gradle/wrapper/gradle-wrapper.jar`), so a local Gradle install is **not** required.

```bash
./gradlew build
```

The first build downloads Minecraft 1.12.2, Forge 1.12.2-14.23.5.2847 and the MCP mappings, then decompiles/recompiles and packages the mod. The built jar is at:

```
build/libs/OnlyMultiplayer-1.12.2-1.0.0.jar
```

## Install

Drop the built jar into your Minecraft client's `mods/` folder (`.minecraft/mods/`), then launch the game with Forge 1.12.2. The Singleplayer button will be handled per your `mode` setting.

## Notes

- Original clean-room implementation. No third-party dependencies.
- `clientSideOnly = true` in the `@Mod` annotation, so it is simply skipped on a dedicated server.

## License

[MIT](LICENSE) © 2026 Monutchuan
