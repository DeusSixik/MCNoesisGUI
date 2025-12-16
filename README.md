<div align="center">

# MCNoesisGUI

**Integrates [NoesisGUI](https://www.noesisengine.com/) into Minecraft**

</div>

MCNoesisGUI integrates the NoesisGUI framework into Minecraft’s rendering context, allowing you to render Noesis XAML-based UI inside the game.

## Overview

- NoesisGUI runs inside the Minecraft client render loop
- UI assets (XAML, textures, etc.) are loaded from the standard Minecraft resource system
- Resources follow Minecraft `ResourceLocation` rules and are addressed as `namespace:path`

## Prerequisites

- Basic understanding of NoesisGUI and XAML
- A Minecraft mod development setup (client-side)
- UI resources placed under your mod assets directory

## Documentation

Before using this project, read the official NoesisGUI documentation:
- NoesisGUI docs: https://www.noesisengine.com/docs/Gui.Core.Index.html

## Resource loading rules

Minecraft mods store data under: `resources/assets/<modid>/...`
MCNoesisGUI loads UI files using Minecraft `ResourceLocation` syntax: `<modid>:<path>`

### Important constraints

- Use only lowercase letters in the namespace and path
- Avoid spaces and special characters in file and folder names
- Keep paths consistent with the location under `resources/assets/<modid>/`

### Examples

If the file is located at: `resources/assets/mcnoesisgui/myfolder/my_menu.xaml`
Load it with: `mcnoesisgui:myfolder/my_menu.xaml`

## How to use

1. Read the NoesisGUI documentation to understand controls, XAML structure, and styling.
2. Place your XAML and related UI assets under `resources/assets/<modid>/`.
3. Reference assets using `ResourceLocation` format (`modid:path/to/file`), ensuring the path is lowercase and matches the assets layout.


