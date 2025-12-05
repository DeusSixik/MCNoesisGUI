package dev.sixik.mcnoesisgui.integration;

import dev.sixik.noesisgui.NoesisGui;
import dev.sixik.noesisgui.nsgui.NSFrameworkElement;
import dev.sixik.noesisgui.nsgui.NSGui_RenderFlags;
import dev.sixik.noesisgui.nsgui.NSIView;
import dev.sixik.noesisgui_impl.NSThemes;
import dev.sixik.noesisgui_render.gl.NSOpenGl;

public class McNSDebug {

    private static NSIView debug;

    public static NSIView createDebug() {
        if(debug != null)
            return debug;

        NSFrameworkElement data = NoesisGui.parseXaml("""
<Grid xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
      xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml"
      Background="#00000000">

    <!-- Полупрозрачная подложка при желании -->
    <Border Background="#80000000"/>

    <!-- Само меню по центру -->
    <Border Width="360"
            Height="220"
            Background="#FF202020"
            CornerRadius="8"
            HorizontalAlignment="Center"
            VerticalAlignment="Center"
            Padding="16">
        <StackPanel>

            <TextBlock Text="Main Menu"
                       FontSize="24"
                       Foreground="White"
                       HorizontalAlignment="Center"
                       Margin="0,0,0,12"/>

            <Separator Margin="0,0,0,8"/>

            <Button x:Name="PlayButton"
                    Content="Play"
                    Margin="0,0,0,8"
                    Height="32"/>

            <Button x:Name="SettingsButton"
                    Content="Settings"
                    Margin="0,0,0,8"
                    Height="32"/>

            <Button x:Name="ExitButton"
                    Content="Exit"
                    Height="32"/>

        </StackPanel>
    </Border>
</Grid>
                """);


        debug = NoesisGui.createView(data);
        debug.getRenderer().init(McNSClient.renderDevice);
        debug.setFlags(NSGui_RenderFlags.PPAA.value | NSGui_RenderFlags.LCD.value);

        debug.setScale(3f);
//        debug.setSize(500, 500);

//        debug = NoesisGui.createView(data);
//        debug.setFlags(NSGui_RenderFlags.PPAA.value | NSGui_RenderFlags.LCD.value);
//        debug.getRenderer().init(NSOpenGl.createDevice(false));
//        debug.setSize(500, 500);

        return debug;
    }
}
