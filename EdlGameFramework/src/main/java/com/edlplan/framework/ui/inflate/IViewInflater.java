package com.edlplan.framework.ui.inflate;

import com.edlplan.framework.ui.EdAbstractViewGroup;
import com.edlplan.framework.ui.EdView;

import org.json.JSONObject;

public interface IViewInflater<T extends EdView> {
    T inflate(JSONObject object, EdAbstractViewGroup parent);
}
