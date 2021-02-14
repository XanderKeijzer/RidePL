package nl.twinkelmc.rides

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import nl.twinkelmc.rides.clientside.ProtocolCreator
import nl.twinkelmc.rides.clientside.StandHandler
import nl.twinkelmc.rides.events.ConnectionEvent
import nl.twinkelmc.rides.events.InteractEvent
import nl.twinkelmc.rides.modules.EntityID
import nl.twinkelmc.rides.modules.PlayerModule
import nl.twinkelmc.rides.panel.PanelStorage
import nl.twinkelmc.rides.rides.continuous.droomvlucht.Droomvlucht
import nl.twinkelmc.rides.rides.continuous.droomvlucht.DroomvluchtOld
import nl.twinkelmc.rides.rides.CommandHandler
import nl.twinkelmc.rides.rides.TrackLoader
import nl.twinkelmc.rides.rides.coaster.TrackRenderer
import nl.twinkelmc.rides.rides.coaster.vogelrok.Vogelrok
import nl.twinkelmc.rides.rides.utils.RenderUtils
import nl.twinkelmc.rides.rides.utils.StandCreator
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {

    companion object {
        lateinit var instance: Main
        val entityID = EntityID()
        lateinit var protocolManager: ProtocolManager
        lateinit var protocolCreator: ProtocolCreator
        lateinit var standHandler: StandHandler
    }

    /*
    Modules
     */
    val playerModule = PlayerModule()

    /*
    Ride Utils
     */
    val standCreator = StandCreator()
    val renderUtils = RenderUtils()

    /*
    Rides
     */
    val droomvluchtOld = DroomvluchtOld()
    val droomvlucht = Droomvlucht()
    val vogelrok = Vogelrok()

    val trackRenderer = TrackRenderer()
    val trackLoader = TrackLoader()
    //val coasterReader = CoasterReader()

    /*
    Panels
     */
    val panelStorage = PanelStorage()

    override fun onEnable() {

        instance = this

        playerModule.register(Bukkit.getOnlinePlayers())
        protocolManager = ProtocolLibrary.getProtocolManager()
        protocolCreator = ProtocolCreator()
        standHandler = StandHandler()

        panelStorage.loadPanels()

        droomvlucht.start()
        vogelrok.start()

        registerEvents()
        registerCommands()

        logger.info("$name enabled.")

    }

    override fun onDisable() {

        panelStorage.removePanels()

        droomvlucht.stop()
        vogelrok.stop()

        trackRenderer.clear()

        logger.info("$name disabled.")

    }

    private fun registerEvents() {
        Bukkit.getPluginManager().registerEvents(InteractEvent(), this)
        Bukkit.getPluginManager().registerEvents(ConnectionEvent(), this)
    }

    private fun registerCommands() {
        this.getCommand("devride")?.setExecutor(CommandHandler())
        this.getCommand("panel")?.setExecutor(nl.twinkelmc.rides.panel.CommandHandler())
        this.getCommand("ride")?.setExecutor(nl.twinkelmc.rides.rides.coaster.CommandHandler())
    }

}