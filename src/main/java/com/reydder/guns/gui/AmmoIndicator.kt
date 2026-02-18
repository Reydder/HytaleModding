package com.reydder.guns.gui

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
import com.hypixel.hytale.server.core.universe.PlayerRef

class AmmoIndicator(playerRef: PlayerRef, private val text: String? = null): CustomUIHud(playerRef) {
    override fun build(uiCommandBuilder: UICommandBuilder) {
        if (text != null) {
            uiCommandBuilder.append("Hud/AmmoOverlay.ui")
            uiCommandBuilder.set("#AmmoIndicatior.Text", text)
        } else {
            //uiCommandBuilder.clear("#AmmoOverlay")
        }
    }
}