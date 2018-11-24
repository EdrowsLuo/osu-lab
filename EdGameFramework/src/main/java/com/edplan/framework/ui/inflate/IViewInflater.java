package com.edplan.framework.ui.inflate;

import com.edplan.framework.ui.EdAbstractViewGroup;
import com.edplan.framework.ui.EdView;

import org.json.JSONObject;

public interface IViewInflater<T extends EdView> {
    T inflate(JSONObject object, EdAbstractViewGroup parent);
}
