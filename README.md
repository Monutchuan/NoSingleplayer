# NoSingleplayer

A **client-side Forge mod** for Minecraft 1.12.2 that suppresses the **Singleplayer** button on the main menu.

> Note: this is a **mod**, not a server plugin. The Singleplayer button lives on the client's own main menu, so it can only be changed from the client side. Installing this mod on a dedicated server has no effect on players' buttons.

## What it does

When the main menu (`GuiMainMenu`) opens, the mod finds the `menu.singleplayer` button and handles it according to the configured mode:

| Mode      | Behavior                                                        | Default |
|-----------|----------------------------------------------------------------|---------|
| `remove`  | The button is removed entirely from the main menu.             | ✅ yes  |
| `disable` | The button stays visible but is greyed out and cannot be clicked. | no   |

## Configuration

The mode is controlled by `config/nosingleplayer.cfg` (created on first launch):

```cfg
# How to handle the Singleplayer button: 'remove' = hide it entirely, 'disable' = grey it out but keep it visible.
mode=remove
```

You can also change it in-game from the **Mods** list → select *No Singleplayer* → **Config**.

Valid values: `remove` (or anything unrecognized falls back to `remove`) and `disable`.

## Build

This project uses ForgeGradle 2.3.2. The Gradle wrapper jar is **not** committed, so grab it from the official Forge 1.12.2 MDK first:

1. Download `forge-1.12.2-14.23.5.2847-mdk` from [files.minecraftforge.net](https://files.minecraftforge.net).
2. Copy `gradlew`, `gradlew.bat`, and `gradle/wrapper/gradle-wrapper.jar` from the MDK into this project's root.
3. Run:

   ```bash
   ./gradlew build
   ```

The built jar is at `build/libs/NoSingleplayer-1.12.2-1.0.0.jar`.

## Install

Drop the built jar into your Minecraft client's `mods/` folder (`.minecraft/mods/`), then launch the game with Forge 1.12.2. The Singleplayer button will be handled per your `mode` setting.

## Notes

- Original clean-room implementation. No third-party dependencies.
- `clientSideOnly = true` in the `@Mod` annotation, so it is simply skipped on a dedicated server.

## License

[MIT](LICENSE) © 2026 Monutchuan
