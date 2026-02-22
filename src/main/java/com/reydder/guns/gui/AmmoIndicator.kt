package com.reydder.guns.gui

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
import com.hypixel.hytale.server.core.universe.PlayerRef

class AmmoIndicator(playerRef: PlayerRef, private val text: String? = null, private val points: Float = 0.0F): CustomUIHud(playerRef) {
    override fun build(uiCommandBuilder: UICommandBuilder) {
        uiCommandBuilder.append("Hud/AmmoOverlay.ui")
        if (text == null) {
            uiCommandBuilder.set("#AmmoIndicatior.Visible", false)
        } else {
            uiCommandBuilder.set("#AmmoIndicatior.Text", text)
            uiCommandBuilder.set("#AmmoIndicatior.Visible", true)
        }
        uiCommandBuilder.set("#PointsIndicator.Text", "${points.toInt()}")
    }
}