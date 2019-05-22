package com.edlplan.framework.utils.dataobject.json;

import org.json.JSONObject;

public interface JsonLike {

    void loadJSON(JSONObject object);

    JSONObject toJSON();

}
