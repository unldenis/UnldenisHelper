# UnldenisHelper

Library that increases productivity in the production of spigot plugins.
<br><br>
<h3>Events</h3>
Classic use of the Spigotmc-api: 
```java
//imports
public class PlayerListeners implements Listener {
    @EventHandler
    public void onQuit(PlayerMoveEvent event) {
        if(!e.getPlayer().hasPermission("plugin.admin")) return;
        if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) return;
        event.getPlayer().sendMessage("You moved an entire block");
    }
}

/*
    And in the main class:
*/
    getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
```
<br><br>
Using my library, these few lines of code will suffice for you to insert into any method!
```java
    Events.subscribe(PlayerMoveEvent.class)
    .filter(e -> !e.getPlayer().hasPermission("plugin.admin"))
    .filter(e -> e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ())
    .handler(event -> {
        event.getPlayer().sendMessage("You moved an entire block");
    }).bind(plugin);
```
