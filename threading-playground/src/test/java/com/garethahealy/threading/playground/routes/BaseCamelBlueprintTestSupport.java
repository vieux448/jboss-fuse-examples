package com.garethahealy.threading.playground.routes;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;

public class BaseCamelBlueprintTestSupport extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/threading-playground-context.xml";
    }
}
