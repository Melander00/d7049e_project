package com.example.brainslop.core.serialize;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.JsonValue;

public class Json extends com.badlogic.gdx.utils.Json {

    public Json() {
        super();

        setSerializer(GameObject.class, new com.badlogic.gdx.utils.Json.Serializer<GameObject>() {

            @Override
            public void write(com.badlogic.gdx.utils.Json json, GameObject obj, Class knownType) {
                json.writeObjectStart();
                json.writeValue("name", obj.name);
                json.writeValue("tag", obj.tag);

                json.writeArrayStart("components");
                for (Component c : obj.components) {
                    json.writeValue(c, Component.class, c.getClass());
                }
                json.writeArrayEnd();

                json.writeObjectEnd();
            }

            @Override
            public GameObject read(com.badlogic.gdx.utils.Json json, JsonValue data, Class type) {
                GameObject obj = new GameObject();
                obj.name = data.getString("name", "");
                obj.tag = data.getString("tag", "");

                JsonValue comps = data.get("components");
                if (comps != null) {
                    for (JsonValue c : comps) {
                        String className = c.getString("class");

                        try {
                            Class<?> cls = Class.forName(className);
                            Component comp = (Component) json.readValue(cls, c);
                            obj.components.add(comp);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to load component: " + className, e);
                        }
                    }
                }

                return obj;
            }
        });
    }

}
