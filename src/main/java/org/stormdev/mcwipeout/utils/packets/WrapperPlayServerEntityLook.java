package org.stormdev.mcwipeout.utils.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

/**
 * Sent by the server when an entity rotates.
 */
public class WrapperPlayServerEntityLook extends AbstractPacket {

    /**
     * The packet type that is wrapped by this wrapper.
     */
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_LOOK;

    /**
     * Constructs a new wrapper and initialize it with a packet handle with default values
     */
    public WrapperPlayServerEntityLook() {
        super(TYPE);
    }

    /**
     * Constructors a new wrapper for the specified packet
     *
     * @param packet the packet to wrap
     */
    public WrapperPlayServerEntityLook(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieves the id of the entity to move
     *
     * @return id of the entity to move
     */
    public int getEntityId() {
        return this.handle.getIntegers().read(0);
    }

    /**
     * Sets the id of the entity to move
     *
     * @param value id of the entity to move
     */
    public void setEntityId(int value) {
        this.handle.getIntegers().write(0, value);
    }

    /**
     * Gets the rotation around the y-axis (yaw) as degrees.
     *
     * @return rotation around the y-axis (yaw) as degrees.
     */
    public float getYaw() {
        return angleToDegrees(this.handle.getBytes().read(0));
    }

    /**
     * Sets the rotation around the y-axis (yaw) in degrees. This value will be implicitly converted to a discrete rotation.
     * Thus, the angle returned by {@link WrapperPlayServerEntityLook#getYaw()} might differ.
     *
     * @param value new y-axis rotation in degrees
     */
    public void setYaw(float value) {
        this.handle.getBytes().write(0, degreesToAngle(value));
    }

    /**
     * Gets the rotation around the x-axis (pitch) in degrees.
     *
     * @return rotation around the x-axis (pitch) in degrees.
     */
    public float getPitch() {
        return angleToDegrees(this.handle.getBytes().read(1));
    }

    /**
     * Sets the rotation around the x-axis (pitch) in degrees. This value will be implicitly converted to a discrete rotation.
     * Thus, the angle returned by {@link WrapperPlayServerEntityLook#getPitch()} might differ.
     *
     * @param value new x-axis rotation in degrees
     */
    public void setPitch(float value) {
        this.handle.getBytes().write(1, degreesToAngle(value));
    }

    /**
     * Retrieves whether the entity is on ground
     *
     * @return true if the entity is on ground
     */
    public boolean getOnGround() {
        return this.handle.getBooleans().read(0);
    }

    /**
     * Sets whether the entity is on ground
     *
     * @param value true if the entity is on ground
     */
    public void setOnGround(boolean value) {
        this.handle.getBooleans().write(0, value);
    }

    public static float angleToDegrees(byte rawAngle) {
        return rawAngle / 256.0F * 360.0F;
    }

    public static byte degreesToAngle(float degree) {
        return (byte) ((int) (degree * 256.0F / 360.0F));
    }
}