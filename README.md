# UnldenisHelper

Library that increases productivity in the production of spigot plugins.
<br><br>
Classic use of the Spigotmc-api: 
```java
//imports
public class PlayerQuitEvent implements Listener {
    @EventHandler
    public void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        Game game = plugin.getGameManager().findGame(player);
        if(game==null) return;
        event.setQuitMessage(null);
        game.quit(player, true);
    }
}
```
