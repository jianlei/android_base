package com.daren.base.util;

import com.squareup.otto.Bus;

/**
 * Description: 主要用于otto事件分发
 * Created by wangjianlei on 15/5/4.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
