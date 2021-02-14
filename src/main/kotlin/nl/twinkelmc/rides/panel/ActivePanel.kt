package nl.twinkelmc.rides.panel

import org.bukkit.entity.ArmorStand

data class ActivePanel(val panel: ArmorStand, val buttons: ArrayList<ArmorStand> = arrayListOf())