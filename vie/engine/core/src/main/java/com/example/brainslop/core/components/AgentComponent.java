package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class AgentComponent implements Component {

    public String faction = "";
    public String behaviorType = "";
    public String currentState = "";
    public transient Entity targetEntity = null;
}

