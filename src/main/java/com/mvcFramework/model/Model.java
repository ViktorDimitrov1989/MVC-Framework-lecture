package com.mvcFramework.model;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Model {

    private HttpServletRequest request;

    private Map<String, Object> attributes;

    public Model(HttpServletRequest request){
        this.attributes = new HashMap<>();
        this.request = request;
    }

    public void addAttribute(String key, Object value){
        this.attributes.put(key, value);

        this.sendParametersToView();
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    private void sendParametersToView(){

        for (Map.Entry<String, Object> entry : this.attributes.entrySet()) {
            this.request.setAttribute(entry.getKey(), entry.getValue());
        }

    }


}
