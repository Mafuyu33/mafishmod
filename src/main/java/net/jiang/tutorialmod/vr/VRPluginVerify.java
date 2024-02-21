package net.jiang.tutorialmod.vr;

public class VRPluginVerify {

    public static boolean hasAPI = false;

    // Only checks for API if not in-world
    public static boolean clientInVR() {
        return hasAPI && VRPluginProxy.vrAPIIInVR();
    }
}
