package com.neotys.coap.common;

import javax.swing.*;
import java.net.URL;

public class CoapUtils {
    private static final ImageIcon COAP_ICON;
    static {

        final URL iconURL = CoapUtils.class.getResource("coap.png");
        if (iconURL != null) {
            COAP_ICON = new ImageIcon(iconURL);
        } else {
            COAP_ICON = null;
        }
    }

    public CoapUtils() {
    }
    public static ImageIcon getCoapIconIcon() {
        return COAP_ICON;
    }

}
