package com.example.brainslop.core.components;
import com.badlogic.ashley.core.Component;

public class InputComponent implements Component {
    public transient float axisX = 0f;
    public transient float axisY = 0f;

    public transient boolean action1 = false;
    public transient boolean action2 = false;

}