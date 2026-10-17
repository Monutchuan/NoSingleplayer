# OnlyMultiplayer

A **client-side Forge mod** for Minecraft 1.12.2 that removes or disables the **Singleplayer** button on the main menu, and can optionally jump straight into a server on startup.

> Note: this is a **mod**, not a server plugin. The Singleplayer button lives on the client's own main menu, so it can only be changed from the client side. Installing this mod on a dedicated server has no effect on players' buttons.

## What it does

### Singleplayer button

When the main menu (`GuiMainMenu`) opens, the mod finds the `menu.singleplayer` button and handles it according to the configured mode:

| Mode      | Behavior                                                        | Default |
|-----------|----------------------------------------------------------------|---------|
| `remove`  | The button is removed and the remaining buttons shift up half a row. | ✅ yes  |
| `disable` | The button stays visible but is greyed out and cannot be clicked. | no   |

### Auto-connect

When `autoConnect` is on and `serverAddress` is set, the main menu is replaced by a **connecting screen** the moment it would appear, so the player sees the server address instead of a menu flash.

- **Player disconnects on purpose** (pause menu → *Disconnect*): the game closes instead of dropping back to the server list.
- **Player aborts while connecting**: the single button on the connecting screen reads *Quit Game* and closes the game; there is no way back to the server list or the main menu.
- **Connection lost / kicked / server unreachable**: the vanilla disconnect screen is kept, so the server's own message (wrong version, whitelist, ban reason, ...) is still shown — only its single *Back* button is replaced with **Retry** and **Quit Game**.
- `resourcePack` controls how server resource packs are handled: `allow` accepts them without asking, `prompt` asks the player (vanilla behaviour), `deny` refuses them.

This is meant for shared machines and kiosk-style setups where the game should only ever talk to one server.

## Configuration

All options live in `config/onlymultiplayer.cfg` (created on first launch):

```cfg
mode=remove
autoConnect=false
serverAddress=
resourcePack=prompt
```

| Option          | Values                          | Default  | Meaning |
|-----------------|---------------------------------|----------|---------|
| `mode`          | `remove` / `disable`            | `remove` | How to handle the Singleplayer button. |
| `autoConnect`   | `true` / `false`                | `false`  | Join `serverAddress` when the main menu loads. |
| `serverAddress` | `host` or `host:port`           | empty    | Server to join; the port defaults to `25565`. |
| `resourcePack`  | `allow` / `prompt` / `deny`     | `prompt` | How to handle server resource packs. |

Unrecognized values fall back to the defaults (`remove`, `prompt`).

You can also change these in-game from the **Mods** list → select *Only Multiplayer* → **Config**.

## Build

This project uses ForgeGradle 2.3.2 and ships with the Gradle wrapper (`gradlew` / `gradlew.bat` / `gradle/wrapper/gradle-wrapper.jar`), so a local Gradle install is **not** required.

```bash
./gradlew build
```

The first build downloads Minecraft 1.12.2, Forge 1.12.2-14.23.5.2847 and the MCP mappings, then decompiles/recompiles and packages the mod. The built jar is at:

```
build/libs/OnlyMultiplayer-1.12.2-1.0.0.jar
```

The jar name keeps the Minecraft version; the version shown in the Mods list is the mod version (`1.0.0`).

## Install

Drop the built jar into your Minecraft client's `mods/` folder (`.minecraft/mods/`), then launch the game with Forge 1.12.2.

## Notes

- Original clean-room implementation. No third-party dependencies.
- `clientSideOnly = true` in the `@Mod` annotation, so it is simply skipped on a dedicated server.
- The resource-pack policy is attached to the auto-connect entry (`ServerData`), so it only affects this connection and does not change the client's global setting.

## License

[MIT](LICENSE) © 2026 Monutchuan
