package com.akivamu.jsonmerger;

import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonMergerTest {
    private JsonObject originJsonObject;
    private JsonObject updateJsonObject;

    @Before
    public void init() {
        originJsonObject = TestUtil.readJsonObjectFromResources("/flat/origin.json");
        updateJsonObject = TestUtil.readJsonObjectFromResources("/flat/ext.json");
    }

    @Test
    public void testMergeFlat() {
        JsonMerger merger = new JsonMerger();

        JsonObject result = merger.merge(originJsonObject, updateJsonObject);
        Assert.assertEquals(5, result.size());
        Assert.assertEquals("jsonmerger.com", result.get("host").getAsString());
        Assert.assertEquals("default", result.get("note").getAsString());
        Assert.assertEquals(80, result.get("port").getAsInt());
        Assert.assertEquals(true, result.get("tls").getAsBoolean());
        Assert.assertEquals("new", result.get("additional").getAsString());
    }
}
