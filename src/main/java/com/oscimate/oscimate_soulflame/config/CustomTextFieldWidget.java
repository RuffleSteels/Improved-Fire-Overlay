package com.oscimate.oscimate_soulflame.config;

import com.sun.tools.jconsole.JConsoleContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class CustomTextFieldWidget extends TextFieldWidget {
    private ChangeFireColorScreen instance;
    public CustomTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text, ChangeFireColorScreen instance) {
        super(textRenderer, x, y, width, height, text);
        this.instance = instance;
    }

    @Override
    public void write(String text) {
        super.write(text);
        instance.input = this.getText();
        instance.updateBlockUnder(this.getText());
        instance.searchScreenListWidget.test();
    }

    @Override
    public void eraseCharactersTo(int position) {
        super.eraseCharactersTo(position);
        instance.input = this.getText();
        instance.updateBlockUnder(this.getText());
        instance.searchScreenListWidget.test();
    }
}
