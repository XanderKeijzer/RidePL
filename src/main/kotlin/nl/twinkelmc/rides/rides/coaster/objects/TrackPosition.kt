package nl.twinkelmc.rides.rides.coaster.objects

import org.bukkit.Location
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector

data class TrackPosition(val loc: Location,
                         val rot: EulerAngle, val up: Vector, val dir: Vector, val left: Vector)