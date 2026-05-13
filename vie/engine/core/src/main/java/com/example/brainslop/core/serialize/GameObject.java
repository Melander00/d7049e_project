package com.example.brainslop.core.serialize;

import com.badlogic.ashley.core.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    public String name = "";
    public String tag = "";
    public List<Component> components = new ArrayList<>();
}
