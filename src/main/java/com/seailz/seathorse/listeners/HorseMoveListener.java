package com.seailz.seathorse.listeners;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public class HorseMoveListener implements Listener {

    private final Method[] methods = ((Supplier<Method[]>) () -> {
        try {
            Method getHandle = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".entity.CraftEntity").getDeclaredMethod("getHandle");
            return new Method[] {
                    getHandle, getHandle.getReturnType().getDeclaredMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class)
            };
        } catch (Exception ex) {
            return null;
        }
    }).get();

    @EventHandler
    public void onEntityMove(PlayerMoveEvent e) throws InvocationTargetException, IllegalAccessException {
        if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ() && e.getFrom().getY() == e.getTo().getY()) return;
        if (!(e.getPlayer().getVehicle() instanceof Horse)) return;
        System.out.println("Was a horse");
        Horse horse = (Horse) e.getPlayer().getVehicle();
        Minecart minecart = null;

        for (Entity entity : horse.getLocation().getNearbyEntities(10, 10, 10)) {
            System.out.println("Found entity: " + entity.getType());
            if (entity instanceof Minecart) {
                minecart = (Minecart) entity;
                System.out.println("Entity is a minecart");
                break;
            }
        }

        if (minecart == null) return;
        System.out.println("Found minecart");

        /* Entity passenger = null;
        if (minecart.getPassengers().size() != 0) {
            passenger = minecart.getPassengers().get(0);
            minecart.removePassenger(passenger);
        } */


        Vector behind = horse.getLocation().getDirection().normalize().multiply(-1);
        teleportWithPassenger(horse.getLocation().add(behind), minecart);
        minecart.setVelocity(behind);

        /* if (passenger != null)
            minecart.setPassenger(passenger); */

        System.out.println("teleported minecart");
    }

    private void teleportWithPassenger(Location loc, Entity entity) throws InvocationTargetException, IllegalAccessException {
        methods[1].invoke(methods[0].invoke(entity), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }
}
