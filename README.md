# MinecraftLives
_mm diamond soup_

## Content
1. Introduction
2. Functions
3. Recipes
4. Installation
5. Config
6. Future Plans

## Introduction
MinecraftLives adds lives to your Spigot server. Everyone spawns with a specific amount of lives that decrease every time you die.
Lives can be gained by crafting Stew of Life.

## Functions
- Lives for every player
- Customizable as fricc

## Recipes
To craft the Stew of Life, you first need a Bowl of Healing which is crafted like this:

![r_bowl](https://user-images.githubusercontent.com/23456524/111805845-b324ae00-88d1-11eb-8c5a-935ae4374d23.png)

You can then use that bowl to craft a Stew of Life:

![r_stew](https://user-images.githubusercontent.com/23456524/111806059-e1a28900-88d1-11eb-8871-17befc86eba4.png)

(The flower is an Oxeye Daisy. Note that the mushrooms are completely interchangeable.)

## Installation
To install the plugin, head over to [RELEASES](https://github.com/InitialPosition/MinecraftLives/releases), download the latest `.jar` file and place it into the `plugins` folder of your Spigot installation.
The plugin will generate a config file on first startup. It is recommended to edit this file and to either reload or restart the server afterwards.

## Config
The following parameters exist in the config file and can be changed.

| Parameter | Usage | Accepted input |
| :-------- | ----- | -------------- |
| CONF_INITIAL_LIVES | The amount of lives every player starts with | `int` > 0 |
| CONF_MAX_LIVES | The maximum amount of lives a player can have | `int` > 0 |
| CONF_BAN_MESSAGE | The message to ban players with | `string` |
| CONF_ON_DEATH_BAN_TIME | The amount of time to ban dead players in minutes | `int` |

Other config settings are only used internally and should not be changed.

## Future Plans
- [ ] Balancing
