package com.jalasoft.todoly.keyWordDrivenTest;

import keyword.engine.KeyWordEngine;
import org.testng.annotations.Test;

public class KeyWordDrivenTest {
    public KeyWordEngine keyWordEngine;

    @Test
    public void loginT() {
        keyWordEngine = new KeyWordEngine();
        keyWordEngine.startExecutions("login");
    }
}
