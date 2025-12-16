package dev.sixik.mcnoesisgui.demo.test_provider;

import dev.sixik.mcnoesisgui.api.NoesisScreen;
import dev.sixik.noesisgui.NoesisGui;
import dev.sixik.noesisgui.nsgui.NSFrameworkElement;

public class TestNoesisXamlProvider extends NoesisScreen {
    @Override
    protected NSFrameworkElement createRenderElement() {
        return NoesisGui.parseXaml("""
<Grid xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
      xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml"
      Background="#66000000">

  <!-- This forces a second XAML load through your XamlProvider -->
  <Grid.Resources>
    <ResourceDictionary>
      <ResourceDictionary.MergedDictionaries>
        <ResourceDictionary Source="ui/TestStyles.xaml"/>
      </ResourceDictionary.MergedDictionaries>
    </ResourceDictionary>
  </Grid.Resources>

  <Border CornerRadius="10"
          Padding="14"
          Background="#AA1E1E1E"
          HorizontalAlignment="Center"
          VerticalAlignment="Center">

    <StackPanel>
      <TextBlock Text="XamlProvider OK"
                 Foreground="White"
                 FontSize="22"
                 Margin="0,0,0,10"
                 TextAlignment="Center"/>

      <Button Content="Button from Styles.xaml"
              Style="{StaticResource TestButtonStyle}"/>

      <TextBlock Text="If you see a styled button, nested XAML loading works."
                 Foreground="#FFD0D0D0"
                 Margin="0,10,0,0"
                 TextWrapping="Wrap"
                 Width="320"/>
    </StackPanel>

  </Border>
</Grid>
                """);
    }
}
