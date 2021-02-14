package nl.twinkelmc.rides.events

import nl.twinkelmc.rides.Main
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ConnectionEvent: Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        Main.instance.playerModule.register(event.player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        Main.instance.playerModule.remove(event.player)
    }

}