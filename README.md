# UnldenisHelper

Library that increases productivity in the production of spigot plugins.
<br><br>
Classic use of the Spigotmc-api: 
```java
import com.github.unldenis.Archery;
import com.github.unldenis.gamelogic.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
public class PlayerQuitEvent(Archery plugin) implements Listener {
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
