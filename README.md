<center><img src="https://i.postimg.cc/MKPVVR1s/dplogo-512.png" alt="logo"></center>
<center><img src="https://i.postimg.cc/RZ9dqPFx/introduce.png" alt="introduce"></center>

Example Video : *Coming soon!*

This plugin rewards players on a Minecraft server with special points for staying online (AFK) and allows them to exchange those points for items in an in-game shop.  
Encourage player engagement by creating fun AFK point shops where players can earn rewards just by being active, all without complicated setup or configuration!

---

<center><img src="https://i.postimg.cc/RZ9dqP08/description.png" alt="description"></center>

- Reward players with **AFK Points** over time for remaining in designated AFK areas  
- Create one or multiple **point shops** and define custom items as rewards for points  
- **Automatically save** and track each player’s point balance and shop data  
- Easily configure **shop items and prices** through an intuitive in-game GUI  
- Real-time **point balance display** via PlaceholderAPI integration for live updates  

---

<center><img src="https://i.postimg.cc/rwcjzhpH/depend-plugin.png" alt="depend-plugin"></center>

- All DP-Plugins require the **`DPP-Core`** plugin.  
- The plugin will not work if **`DPP-Core`** is not installed.  
- You can download **`DPP-Core`** here: <a href="https://github.com/DP-Plugins/DPP-Core/releases" target="_blank">Click me!</a>  
- This plugin integrates with **PlaceholderAPI**.  
- If PlaceholderAPI is not installed, the placeholder features provided by this plugin will not work.  

---

<center><img src="https://i.postimg.cc/dV01RxJB/installation.png" alt="installation"></center>

1️⃣ Place the **`DPP-Core`** plugin and this plugin file (**`DP-AFKShop-*.jar`**) into your server’s **`plugins`** folder.  

2️⃣ Restart the server, and the plugin will be automatically enabled.  

3️⃣ If needed, you can open and modify **`config.yml`** and **`plugin.yml`** to customize settings.  

---

<center><img src="https://i.postimg.cc/jSKcC85K/settings.png" alt="settings"></center>

- **`config.yml`**: Manages basic plugin settings (such as AFK point interval and messages).  

---

<center><img src="https://i.postimg.cc/SxqdjZKw/command.png" alt="command"></center>

❗ Some commands require admin permission (`dpas.admin`).  

**Command List and Examples**

| Command                               | Permission  | Description                                                    | Example                               |
|---------------------------------------|-------------|----------------------------------------------------------------|---------------------------------------|
| `/dpas create <name>`                 | dpas.admin  | Create a new AFK point shop                                    | `/dpas create MyShop`                 |
| `/dpas items <shop_name>`             | dpas.admin  | Open a GUI to edit the items of an existing AFK point shop     | `/dpas items MyShop`                  |
| `/dpas price <shop_name>`             | dpas.admin  | Open a GUI to edit item prices in an existing AFK point shop   | `/dpas price MyShop`                  |
| `/dpas maxpage <shop_name> <maxPage>` | dpas.admin  | Set the maximum number of pages for the specified shop         | `/dpas maxpage MyShop 5`              |
| `/dpas pointset <time> <amount>`      | dpas.admin  | Set the points awarded per interval while AFK                  | `/dpas pointset 60 5`                 |
| `/dpas areaadd <areaName>`            | dpas.admin  | Add a new area where AFK points can be accumulated             | `/dpas areaadd Lobby`                 |
| `/dpas arearemove <areaName>`         | dpas.admin  | Remove an existing AFK point accumulation area                 | `/dpas arearemove Lobby`              |
| `/dpas areamode`                      | dpas.admin  | Toggle area selection mode for defining AFK point areas        | `/dpas areamode`                      |
| `/dpas areaset <areaName>`            | dpas.admin  | Save the selected region as an AFK point area                  | `/dpas areaset Lobby`                 |
| `/dpas open <shop_name>`              | None        | Open the specified AFK point shop for browsing and purchases   | `/dpas open MyShop`                   |

**❗Notes when using commands**

- Shop and area names support Korean and English, but **spaces are not allowed**.  
- Invalid commands or names will display an error message.  
- Shop items and prices are edited via GUI and saved automatically.  
- AFK areas are defined by selecting two corner points, then saving the region.  
- Admin commands require **OP** status or the `dpas.admin` permission.  

---

<center><img src="https://i.postimg.cc/Z5ZH0fqL/api-integration.png" alt="api-integration"></center>

Display a player’s AFK point balance in real-time on your server!

- **`%dpas_point%`**: The number of AFK points the player currently has.  

---

<center><a href="https://discord.gg/JnMCqkn2FX"><img src="https://i.postimg.cc/4xZPn8dC/discord.png" alt="discord"></a></center>

- https://discord.gg/JnMCqkn2FX  
- If you have any questions or issues, please contact your server administrator or refer to the plugin distribution page.  
- Feel free to leave suggestions for new features or improvements anytime!

---
