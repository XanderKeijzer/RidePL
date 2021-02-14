package nl.twinkelmc.rides.panel

import org.bukkit.Location

data class Panel(val location: Location, val rotation: Double, val model: Int, val buttons: ArrayList<Button> = arrayListOf())