/* (C) TAMA Studios 2025 */
package com.code.tama.tts.server.enums;

import com.code.tama.tts.server.tardis.controls.*;

public enum Controls {
    EMPTY(new EmptyControl()),
    THROTTLE(new ThrottleControl()),
    X_CONTROL(new X_Control()),
    Y_CONTROL(new Y_Control()),
    Z_CONTROL(new Z_Control()),
    INCREMENT_CONTROL(new IncrementControl()),
    POWER_CONTROL(new PowerControl()),
    FACING_CONTROL(new FacingControl()),
    VARIANT_CONTROL(new VariantControl()),
    ;
    private final AbstractControl control;
    Controls(AbstractControl control) {
        this.control = control;
    }

    public AbstractControl GetControl() {
        return this.control;
    }

    public Controls Cycle() {
        if(this.ordinal() + 1 >= Controls.values().length) {
            return Controls.values()[0];
        }
        else return Controls.values()[this.ordinal() + 1];
    }
}