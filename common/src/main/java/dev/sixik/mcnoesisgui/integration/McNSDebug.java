package dev.sixik.mcnoesisgui.integration;

import dev.sixik.noesisgui.NoesisGui;
import dev.sixik.noesisgui.nsgui.NSFrameworkElement;
import dev.sixik.noesisgui.nsgui.NSGui_RenderFlags;
import dev.sixik.noesisgui.nsgui.NSGui_Visibility;
import dev.sixik.noesisgui.nsgui.NSIView;
import dev.sixik.noesisgui.nshandlers.NSEventHandlerManager;
import dev.sixik.noesisgui_impl.NSThemes;
import dev.sixik.noesisgui_render.gl.NSOpenGl;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class McNSDebug {

    private static NSIView debug;

    public static NSIView createDebug() {
        if(debug != null)
            return debug;

        NSFrameworkElement data = NoesisGui.parseXaml("""
<Grid xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
      xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml">

    <!-- Главное меню -->
    <Grid x:Name="MainMenuRoot">
        <StackPanel HorizontalAlignment="Center" VerticalAlignment="Center">
            <Button x:Name="PlayButton" Content="Play" Width="200" Margin="0,0,0,10"/>
            <Button x:Name="SettingsButton" Content="Settings" Width="200" Margin="0,0,0,10"/>
            <Button x:Name="ExitButton" Content="Exit" Width="200"/>
        </StackPanel>
    </Grid>

    <!-- Окно настроек поверх -->
    <Grid x:Name="SettingsRoot"
          Background="#80000000"
          Visibility="Collapsed">
        <Border Width="400" Height="300" Background="#FF202020" CornerRadius="8"
                HorizontalAlignment="Center" VerticalAlignment="Center">
            <StackPanel Margin="20">
                <TextBlock Text="Settings" FontSize="24" Margin="0,0,0,10"/>
                <!-- тут всякие ползунки, чекбоксы -->
                <Button x:Name="SettingsCloseButton"
                        Content="Back"
                        HorizontalAlignment="Right"
                        Margin="0,20,0,0"/>
            </StackPanel>
        </Border>
    </Grid>
</Grid>
                """);


        debug = NoesisGui.createView(data);
        debug.getRenderer().init(McNSClient.renderDevice);
        debug.setFlags(NSGui_RenderFlags.PPAA.value | NSGui_RenderFlags.LCD.value);

        debug.setScale(3f);
//        debug.setSize(500, 500);

        final NSFrameworkElement root = debug.getContent();
        final NSFrameworkElement mainMenu = root.findName("MainMenuRoot");
        final NSFrameworkElement settings = root.findName("SettingsRoot");

        mainMenu.setVisibility(NSGui_Visibility.Visible);
        settings.setVisibility(NSGui_Visibility.Hidden);

//        debug = NoesisGui.createView(data);
//        debug.setFlags(NSGui_RenderFlags.PPAA.value | NSGui_RenderFlags.LCD.value);
//        debug.getRenderer().init(NSOpenGl.createDevice(false));
//        debug.setSize(500, 500);

        NSEventHandlerManager.subscribe(debug, "PlayButton", (args) -> {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Play Press"));
        });

        NSEventHandlerManager.subscribe(debug, "SettingsButton", (args) -> {
            mainMenu.setVisibility(NSGui_Visibility.Hidden);
            settings.setVisibility(NSGui_Visibility.Visible);
        });

        NSEventHandlerManager.subscribe(debug, "SettingsCloseButton", (arg) -> {
            mainMenu.setVisibility(NSGui_Visibility.Visible);
            settings.setVisibility(NSGui_Visibility.Hidden);
        });

        NSEventHandlerManager.subscribe(debug, "ExitButton", (arg) -> {
            root.setVisibility(NSGui_Visibility.Hidden);
        });

        return debug;
    }
}
