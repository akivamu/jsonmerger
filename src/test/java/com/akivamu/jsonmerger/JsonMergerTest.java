package com.akivamu.jsonmerger;

import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;

public class JsonMergerTest {

    @Test
    public void testMergeFlat() {
        JsonObject originJsonObject = TestUtil.readJsonObjectFromResources("/flat/origin.json");
        JsonObject updateJsonObject = TestUtil.readJsonObjectFromResources("/flat/ext.json");

        JsonMerger merger = new JsonMerger();

        JsonObject result = merger.merge(originJsonObject, updateJsonObject);
        Assert.assertEquals(5, result.size());
        Assert.assertEquals("jsonmerger.com", result.get("host").getAsString());
        Assert.assertEquals("default", result.get("note").getAsString());
        Assert.assertEquals(80, result.get("port").getAsInt());
        Assert.assertEquals(true, result.get("tls").getAsBoolean());
        Assert.assertEquals("new", result.get("additional").getAsString());
    }

    @Test
    public void testMergeNested() {
        JsonObject originJsonObject = TestUtil.readJsonObjectFromResources("/nested/origin.json");
        JsonObject updateJsonObject = TestUtil.readJsonObjectFromResources("/nested/ext.json");

        JsonMerger merger = new JsonMerger();

        JsonObject result = merger.merge(originJsonObject, updateJsonObject);
        Assert.assertEquals(4, result.size());

        JsonObject webObject = result.get("web").getAsJsonObject();
        Assert.assertEquals(5, webObject.size());
        Assert.assertEquals("jsonmerger.com", webObject.get("host").getAsString());
        Assert.assertEquals("default", webObject.get("note").getAsString());
        Assert.assertEquals(80, webObject.get("port").getAsInt());
        Assert.assertEquals(true, webObject.get("tls").getAsBoolean());
        Assert.assertEquals("new", webObject.get("additional").getAsString());

        Assert.assertEquals("flatPropOriginOnly", result.get("flatPropOriginOnly").getAsString());
        Assert.assertEquals("flatPropUpdated", result.get("flatProp").getAsString());
        Assert.assertEquals("flatPropExtOnly", result.get("flatPropExtOnly").getAsString());
    }
}
