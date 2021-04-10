package io.neutrino.listener;

import io.neutrino.model.NeutrinoProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        NeutrinoProfile profile = new NeutrinoProfile(p.getUniqueId());
        if(!profile.exists()) {
            profile.save();
        }
    }
}
