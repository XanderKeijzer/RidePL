package nl.twinkelmc.rides.rides.continuous

import org.bukkit.Location
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector

data class CartPosition(val loc: Location,
                        val rot: EulerAngle, val up: Vector, val dir: Vector, val left: Vector,
                        val rotRotated: EulerAngle, val upRotated: Vector, val dirRotated: Vector, val leftRotated: Vector)