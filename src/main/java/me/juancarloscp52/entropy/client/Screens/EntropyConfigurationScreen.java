package me.juancarloscp52.entropy.client.Screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.entropy.EntropySettings;
import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.client.EntropyIntegrationsSettings;
import me.juancarloscp52.entropy.Entropy;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;


public class EntropyConfigurationScreen extends Screen {
    private static final Identifier LOGO = new Identifier("entropy", "textures/logo-with-text.png");
    EntropySettings settings = Entropy.getInstance().settings;
    EntropyIntegrationsSettings integrationsSettings = EntropyClient.getInstance().integrationsSettings;
    short eventDuration;
    short timerDuration;

    TextFieldWidget authToken;
    TextFieldWidget channelName;
    CheckboxWidget integrationsCheckbox;
    CheckboxWidget sendChatMessages;
    ButtonWidget eventDurationWidget;
    ButtonWidget timerDurationWidget;
    ButtonWidget done;

    Screen parent;

    public EntropyConfigurationScreen(Screen parent) {
        super(new TranslatableText("entropy.title"));
        this.parent=parent;
    }

    protected void init(){

        eventDuration=settings.baseEventDuration;
        timerDuration=settings.timerDuration;


        eventDurationWidget = new ButtonWidget(this.width/2-160,60,150,20,new TranslatableText("entropy.options.eventDuration",eventDuration), button -> {
            this.eventDuration+=300;
            if(this.eventDuration>900)
                this.eventDuration=300;
        });
        this.addButton(eventDurationWidget);


        timerDurationWidget = new ButtonWidget(this.width/2+10,60,150,20,new TranslatableText("entropy.options.timerDuration",timerDuration), button -> {
            this.timerDuration+=300;
            if(this.timerDuration>900)
                this.timerDuration=300;
        });
        this.addButton(timerDurationWidget);


        TranslatableText twitchIntegrationsTranslatable = new TranslatableText("entropy.options.twitchIntegrations");
        integrationsCheckbox = new CheckboxWidget(this.width/2-((textRenderer.getWidth(twitchIntegrationsTranslatable)/2)+22), 90, 150, 20, twitchIntegrationsTranslatable, settings.integrations);
        this.addButton(integrationsCheckbox);


        authToken = new TextFieldWidget(this.textRenderer,this.width/2+10,120,125,20,new TranslatableText("entropy.options.twitchIntegrations.OAuthToken"));
        authToken.setMaxLength(64);
        authToken.setText(integrationsSettings.authToken);
        authToken.setRenderTextProvider((s, integer) -> {
            StringBuilder hiddenString= new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                hiddenString.append("*");
            }
            return OrderedText.styledString(hiddenString.toString(), Style.EMPTY);
        });
        this.addButton(authToken);


        channelName = new TextFieldWidget(this.textRenderer,this.width/2+10,150,125,20,new TranslatableText("entropy.options.twitchIntegrations.channelName"));
        channelName.setText(integrationsSettings.channel);
        this.addButton(channelName);


        Text sendChatMessagesText = new TranslatableText("entropy.options.twitchIntegrations.sendChatFeedBack");
        sendChatMessages = new CheckboxWidget(this.width/2-((textRenderer.getWidth(sendChatMessagesText)/2)+11), 180, 150, 20, sendChatMessagesText, integrationsSettings.sendChatMessages);
        this.addButton(sendChatMessages);


        this.done = new ButtonWidget(this.width/2-100,this.height-30,200,20, ScreenTexts.DONE, button -> onDone());
        this.addButton(done);

        this.addButton(
                new ButtonWidget(this.width/2+((textRenderer.getWidth(twitchIntegrationsTranslatable)/2)+6),90,20,20,new TranslatableText("entropy.options.questionMark"),(button -> {}),(buttonWidget, matrixStack, i, j) ->
                this.renderOrderedTooltip(matrixStack,textRenderer.wrapLines(new TranslatableText("entropy.options.twitchIntegrations.help"),this.width/2), i, j)));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta){
        this.renderBackground(matrices);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        matrices.push();
        matrices.translate(this.width/2f-18.8f,0,0);
        matrices.scale(0.2f,0.2f,0.2f);
        client.getTextureManager().bindTexture(LOGO);
        this.drawTexture(matrices,0,0,0,0,188,187);
        matrices.pop();
        RenderSystem.disableBlend();


        this.eventDurationWidget.setMessage(new TranslatableText("entropy.options.eventDuration", eventDuration/20));
        this.timerDurationWidget.setMessage(new TranslatableText("entropy.options.timerDuration",timerDuration/20));

        TranslatableText oAuthTranslation = new TranslatableText("entropy.options.twitchIntegrations.OAuthToken");
        drawTextWithShadow(matrices,this.textRenderer,oAuthTranslation,this.width/2-10-textRenderer.getWidth(oAuthTranslation),126,16777215);

        TranslatableText channelName = new TranslatableText("entropy.options.twitchIntegrations.channelName");
        drawTextWithShadow(matrices,this.textRenderer,channelName,this.width/2-10-textRenderer.getWidth(channelName),156,16777215);

        super.render(matrices, mouseX, mouseY, delta);
    }

    private void onDone(){

        settings.integrations = integrationsCheckbox.isChecked();
        integrationsSettings.authToken = authToken.getText();
        integrationsSettings.channel = channelName.getText();
        integrationsSettings.sendChatMessages = sendChatMessages.isChecked();
        settings.baseEventDuration = eventDuration;
        settings.timerDuration = timerDuration;

        EntropyClient.getInstance().saveSettings();
        Entropy.getInstance().saveSettings();
        onClose();
    }
}
