package com.oscimate.oscimate_soulflame.config;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import com.oscimate.oscimate_soulflame.Main;

import com.oscimate.oscimate_soulflame.mixin.fire_overlays.client.GameRendererMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

import static com.oscimate.oscimate_soulflame.config.ConfigScreen.windowHeight;

public class ChangeFireHeightScreen extends Screen {

    private Screen parent;

    private int counter = 16;
    private int ticks = 0;

    protected ChangeFireHeightScreen(Screen parent) {
        super(Text.translatable("options.videoTitle"));
        this.parent = parent;
    }
    public void onClose() {
        client.setScreen(parent);
    }
    @Override
    protected void init() {
        FireHeightSliderWidget customTimeSliderWidget = new FireHeightSliderWidget(this.width / 2 - 75, 10, 150, 20, Text.literal("Height"), (double) Main.CONFIG_MANAGER.getCurrentFireHeightSlider() /100);
        this.addDrawableChild(customTimeSliderWidget);
        this.addDrawableChild(new ButtonWidget.Builder(ScreenTexts.DONE, button -> onClose()).dimensions(width / 2 - 100, height/2 + windowHeight/2 + 20, 200, 20).build());
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
        super.render(context, mouseX, mouseY, delta);

        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        GameRenderer gameRenderer = MinecraftClient.getInstance().gameRenderer;
        gameRenderer.loadProjectionMatrix(gameRenderer.getBasicProjectionMatrix(((GameRendererMixin)gameRenderer).callGetFov(gameRenderer.getCamera(), MinecraftClient.getInstance().getTickDelta(), false)));
        matrixStack.loadIdentity();

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, new Identifier("textures/block/fire_1.png"));

        float f = 0.0F;
        float a = 1.0F;
        float i = 1/32F * (counter-1) + 0.0001F;
        float j = 1/32F * counter;

        var modelView = RenderSystem.getModelViewStack();
        modelView.push();
        modelView.loadIdentity();
        RenderSystem.applyModelViewMatrix();

        matrixStack.translate(0.0, Main.currentFireHeight, 0.0);

        for (int r = 0; r < 2; ++r) {
            matrixStack.push();
            matrixStack.translate((float)(-(r * 2 - 1)) * 0.24f, -0.3f, 0.0f);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)(r * 2 - 1) * 10.0f));
            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
            bufferBuilder.vertex(matrix4f, -0.5F, -0.5F, -0.5F).color(1.0f, 1.0f, 1.0f, 0.9f).texture(a, j).next();
            bufferBuilder.vertex(matrix4f, 0.5F, -0.5F, -0.5F).color(1.0f, 1.0f, 1.0f, 0.9f).texture(f, j).next();
            bufferBuilder.vertex(matrix4f, 0.5F, 0.5F, -0.5F).color(1.0f, 1.0f, 1.0f, 0.9f).texture(f, i).next();
            bufferBuilder.vertex(matrix4f, -0.5F, 0.5F, -0.5F).color(1.0f, 1.0f, 1.0f, 0.9f).texture(a, i).next();
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            matrixStack.pop();
        }

        modelView.pop();
        RenderSystem.applyModelViewMatrix();

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);


        if (ticks % 4 == 0) counter++;
        ticks++;
        if (counter > 32) {
            counter = 0;
            ticks = 0;
        }
    }
}