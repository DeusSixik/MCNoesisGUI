package dev.sixik.mcnoesisgui.demo.show_menu;

import dev.sixik.mcnoesisgui.McNoesisGui;
import dev.sixik.mcnoesisgui.NoesisScreenWrapperImpl;
import dev.sixik.mcnoesisgui.integration.McNSClient;
import dev.sixik.noesisgui.NoesisGui;
import dev.sixik.noesisgui.nscore.NSBaseComponent;
import dev.sixik.noesisgui.nsgui.*;
import net.minecraft.client.Minecraft;

import java.util.function.Function;

public class ShowMenuDemo {

    private static NSFrameworkElement root;

    private static NSToggleButton navControls, navMobs, navTheme, navAnim, navLog;
    private static NSGrid pageControls, pageMobs, pageTheme, pageAnim, pageLog;

    private static NSTextBlock titleRight, fpsText, memText, lastEvent, mobCount, txtEcho, statsText;
    private static NSTextBox mobSearch, txtInput;
    private static NSSlider sliderValue, hue;
    private static NSProgressBar progressValue;
    private static NSWrapPanel mobWrap;
    private static NSButton btnPrimary, btnSecondary, btnApplyTheme, btnPlayAnim, btnClearLog;
    private static NSHyperlink linkNoesis;
//    private static NSListBox logList;

    private static NSIView _obj;
    private static boolean navGuard;

    public static void openDemo() {
        Minecraft.getInstance().setScreen(new NoesisScreenWrapperImpl(getOrCreate()) {
            @Override
            public void tick() {
                if(fpsText != null) {
                    fpsText.setText(Minecraft.getInstance().fpsString);
                    memText.setText(McNoesisGui.NoesisRender_Time_MS + "ms");
                }
            }
        });
    }

    public static NSIView getOrCreate() {
        if (_obj == null)
            _obj = createView();
        return _obj;
    }

    private static NSIView createView() {
        final NSFrameworkElement root = getRenderElement();
        wireUi(root);

        final NSIView view = NoesisGui.createView(root);
        view.getRenderer().init(McNSClient.renderDevice);
        view.setFlags(NSGui_RenderFlags.PPAA.value | NSGui_RenderFlags.LCD.value);
        return view;
    }

    private static NSFrameworkElement getRenderElement() {
        return NoesisGui.parseXaml("""
<Grid xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
      xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml"
      Background="#AA0B0F14">

  <Grid.Resources>
    <SolidColorBrush x:Key="Bg" Color="#AA0B0F14"/>
    <SolidColorBrush x:Key="Card" Color="#CC151B24"/>
    <SolidColorBrush x:Key="Stroke" Color="#332A3445"/>
    <SolidColorBrush x:Key="Accent" Color="#FF3B82F6"/>
    <SolidColorBrush x:Key="Text" Color="#FFE5E7EB"/>
    <SolidColorBrush x:Key="TextDim" Color="#FF9CA3AF"/>

    <Style TargetType="TextBlock">
      <Setter Property="Foreground" Value="{StaticResource Text}"/>
      <Setter Property="FontSize" Value="16"/>
    </Style>

    <Style x:Key="H1" TargetType="TextBlock">
      <Setter Property="FontSize" Value="26"/>
      <Setter Property="FontWeight" Value="SemiBold"/>
    </Style>

    <Style x:Key="CardBorder" TargetType="Border">
      <Setter Property="Background" Value="{StaticResource Card}"/>
      <Setter Property="BorderBrush" Value="{StaticResource Stroke}"/>
      <Setter Property="BorderThickness" Value="1"/>
      <Setter Property="CornerRadius" Value="14"/>
      <Setter Property="Padding" Value="14"/>
      <Setter Property="Margin" Value="0,0,0,12"/>
    </Style>

    <Style TargetType="Button">
      <Setter Property="Padding" Value="12,8"/>
      <Setter Property="Background" Value="#222A3445"/>
      <Setter Property="Foreground" Value="{StaticResource Text}"/>
      <Setter Property="BorderBrush" Value="{StaticResource Stroke}"/>
      <Setter Property="BorderThickness" Value="1"/>
      <Setter Property="Margin" Value="0,0,0,8"/>
    </Style>

    <Style x:Key="NavButton" TargetType="ToggleButton">
      <Setter Property="Padding" Value="12,10"/>
      <Setter Property="Foreground" Value="{StaticResource Text}"/>
      <Setter Property="Background" Value="#00000000"/>
      <Setter Property="BorderBrush" Value="#00000000"/>
      <Setter Property="BorderThickness" Value="0"/>
      <Setter Property="HorizontalContentAlignment" Value="Left"/>
      <Setter Property="Margin" Value="0,0,0,6"/>
    </Style>

    <Style x:Key="PrimaryButton" TargetType="Button" BasedOn="{StaticResource {x:Type Button}}">
      <Setter Property="Background" Value="{StaticResource Accent}"/>
      <Setter Property="BorderBrush" Value="#00000000"/>
    </Style>
  </Grid.Resources>

  <Grid.ColumnDefinitions>
    <ColumnDefinition Width="260"/>
    <ColumnDefinition Width="*"/>
  </Grid.ColumnDefinitions>

  <!-- Left nav -->
  <Border Background="#CC0E141E" BorderBrush="{StaticResource Stroke}" BorderThickness="0,0,1,0">
    <StackPanel Margin="16">
      <TextBlock Style="{StaticResource H1}" Text="Noesis Demo"/>
      <TextBlock Foreground="{StaticResource TextDim}" Margin="0,6,0,16"
                 Text="Minecraft integration showcase"/>

      <ToggleButton x:Name="NavControls" Style="{StaticResource NavButton}" Content="Controls"/>
      <ToggleButton x:Name="NavMobs"     Style="{StaticResource NavButton}" Content="Mob Tiles"/>
      <ToggleButton x:Name="NavTheme"    Style="{StaticResource NavButton}" Content="Themes"/>
      <ToggleButton x:Name="NavAnim"     Style="{StaticResource NavButton}" Content="Animations"/>
      <ToggleButton x:Name="NavLog"      Style="{StaticResource NavButton}" Content="Log"/>

      <Border Margin="0,14,0,0" Background="#33111827" CornerRadius="12" Padding="12">
        <StackPanel>
          <TextBlock Text="Hint" />
          <TextBlock Foreground="{StaticResource TextDim}" FontSize="13"
                     Text="Esc — close, колесо — scroll"/>
        </StackPanel>
      </Border>
    </StackPanel>
  </Border>

  <!-- Right content -->
  <Grid Grid.Column="1" Margin="18">
    <Grid.RowDefinitions>
      <RowDefinition Height="Auto"/>
      <RowDefinition Height="*"/>
    </Grid.RowDefinitions>

    <!-- Top bar -->
    <DockPanel Margin="0,0,0,12">
      <TextBlock x:Name="TitleRight" Text="Controls" Style="{StaticResource H1}" />
      <StackPanel Orientation="Horizontal" HorizontalAlignment="Right">
        <TextBlock x:Name="FpsText" Foreground="{StaticResource TextDim}" Margin="0,6,12,0" Text="FPS: -"/>
        <TextBlock x:Name="MemText" Foreground="{StaticResource TextDim}" Margin="0,6,0,0"  Text="Render MS: -"/>
      </StackPanel>
    </DockPanel>

    <!-- Pages host -->
    <Grid Grid.Row="1" x:Name="PagesHost">

      <!-- PAGE: Controls -->
      <Grid x:Name="PageControls" Visibility="Visible">
        <StackPanel>
          <TextBlock Style="{StaticResource H1}" Text="Controls and Events"/>
          <TextBlock Foreground="{StaticResource TextDim}" Margin="0,6,0,14"
                     Text="Ручное управление без биндингов." />

          <Border Style="{StaticResource CardBorder}">
            <Grid>
              <Grid.ColumnDefinitions>
                <ColumnDefinition Width="*"/>
                <ColumnDefinition Width="*"/>
              </Grid.ColumnDefinitions>

              <StackPanel Margin="0,0,10,0">
                <TextBlock Text="Buttons"/>
                <Button x:Name="BtnPrimary" Style="{StaticResource PrimaryButton}" Content="Primary Action"/>
                <Button x:Name="BtnSecondary" Content="Secondary Action"/>

                <TextBlock Margin="0,12,0,0" Text="Slider / Progress"/>
                <Slider x:Name="SliderValue" Minimum="0" Maximum="100" Value="25"/>
                <ProgressBar x:Name="ProgressValue" Height="14" Value="25" Maximum="100"/>
              </StackPanel>

              <StackPanel Grid.Column="1" Margin="10,0,0,0">
                <TextBlock Text="Text input"/>
                <TextBox x:Name="TxtInput" Height="34" Text="hello"/>
                <TextBlock x:Name="TxtEcho" Foreground="{StaticResource TextDim}" Margin="0,6,0,0" Text="echo..."/>

                <TextBlock Margin="0,12,0,0" Text="Hyperlink (event)"/>
                <TextBlock>
                  <Run Text="Docs: "/>
                  <Hyperlink x:Name="LinkNoesis" NavigateUri="https://www.noesisengine.com">
                    Noesis Engine
                  </Hyperlink>
                </TextBlock>

                <TextBlock Margin="0,14,0,0" Text="Last event"/>
                <Border Background="#33111827" Padding="10" CornerRadius="10">
                  <TextBlock x:Name="LastEvent" Text="-" Foreground="{StaticResource TextDim}"/>
                </Border>
              </StackPanel>

            </Grid>
          </Border>
        </StackPanel>
      </Grid>

      <!-- PAGE: Mobs -->
      <Grid x:Name="PageMobs" Visibility="Collapsed">
        <StackPanel>
          <TextBlock Style="{StaticResource H1}" Text="Mob Tiles (Search + Spawn)"/>
          <TextBlock Foreground="{StaticResource TextDim}" Margin="0,6,0,14"
                     Text="Плитки заполняются кодом." />

          <Border Style="{StaticResource CardBorder}">
            <Grid>
              <Grid.RowDefinitions>
                <RowDefinition Height="Auto"/>
                <RowDefinition Height="*"/>
              </Grid.RowDefinitions>

              <DockPanel>
                <TextBox x:Name="MobSearch" Width="340" Height="34" Text=""/>
                <TextBlock x:Name="MobCount" Foreground="{StaticResource TextDim}" Margin="12,6,0,0" Text="0 mobs"/>
              </DockPanel>

              <ScrollViewer Grid.Row="1" Margin="0,12,0,0">
                <WrapPanel x:Name="MobWrap" ItemWidth="150" ItemHeight="178"/>
              </ScrollViewer>
            </Grid>
          </Border>
        </StackPanel>
      </Grid>

      <!-- PAGE: Theme -->
      <Grid x:Name="PageTheme" Visibility="Collapsed">
        <StackPanel>
          <TextBlock Style="{StaticResource H1}" Text="Themes / Styles"/>
          <Border Style="{StaticResource CardBorder}">
            <StackPanel>
              <TextBlock Text="Hue"/>
              <Slider x:Name="Hue" Minimum="0" Maximum="360" Value="210"/>
              <Button x:Name="BtnApplyTheme" Style="{StaticResource PrimaryButton}" Content="Apply Theme"/>
            </StackPanel>
          </Border>
        </StackPanel>
      </Grid>

      <!-- PAGE: Anim -->
      <Grid x:Name="PageAnim" Visibility="Collapsed">
        <StackPanel>
          <TextBlock Style="{StaticResource H1}" Text="Animations"/>
          <Border Style="{StaticResource CardBorder}">
            <Grid>
              <Grid.ColumnDefinitions>
                <ColumnDefinition Width="*"/>
                <ColumnDefinition Width="Auto"/>
              </Grid.ColumnDefinitions>

              <Border x:Name="AnimCard" Background="#3322C55E" CornerRadius="14" Padding="18">
                <StackPanel>
                  <TextBlock Text="Animated card"/>
                  <TextBlock Foreground="{StaticResource TextDim}" Margin="0,6,0,0"
                             Text="Запускаем анимацию вручную."/>
                </StackPanel>
              </Border>

              <Button x:Name="BtnPlayAnim" Grid.Column="1" Margin="12,0,0,0" Content="Play"/>
            </Grid>
          </Border>
        </StackPanel>
      </Grid>

      <!-- PAGE: Log -->
      <Grid x:Name="PageLog" Visibility="Collapsed">
        <StackPanel>
          <TextBlock Style="{StaticResource H1}" Text="Log / Diagnostics"/>
          <Border Style="{StaticResource CardBorder}">
            <Grid>
              <Grid.RowDefinitions>
                <RowDefinition Height="Auto"/>
                <RowDefinition Height="*"/>
              </Grid.RowDefinitions>

              <DockPanel>
                <Button x:Name="BtnClearLog" Content="Clear"/>
                <TextBlock x:Name="StatsText" Foreground="{StaticResource TextDim}" Margin="12,6,0,0" Text="..."/>
              </DockPanel>

              <ListBox x:Name="LogList" Grid.Row="1" Margin="0,12,0,0"/>
            </Grid>
          </Border>
        </StackPanel>
      </Grid>

    </Grid>
  </Grid>
</Grid>
                """);
    }

    private static <T extends NSBaseComponent> T n(String name, Function<Long, T> tClass) {
        return root.findName(name).castTo(tClass);
    }

    private static void wireUi(NSFrameworkElement root) {
        ShowMenuDemo.root = root;

        // nav
        navControls = n("NavControls", NSToggleButton::new);
        navMobs     = n("NavMobs", NSToggleButton::new);
        navTheme    = n("NavTheme", NSToggleButton::new);
        navAnim     = n("NavAnim", NSToggleButton::new);
        navLog      = n("NavLog", NSToggleButton::new);

        // pages
        pageControls = n("PageControls", NSGrid::new);
        pageMobs     = n("PageMobs", NSGrid::new);
        pageTheme    = n("PageTheme", NSGrid::new);
        pageAnim     = n("PageAnim", NSGrid::new);
        pageLog      = n("PageLog", NSGrid::new);

        // topbar
        titleRight = n("TitleRight", NSTextBlock::new);
        fpsText    = n("FpsText", NSTextBlock::new);
        memText    = n("MemText", NSTextBlock::new);

        // controls page
        btnPrimary    = n("BtnPrimary", NSButton::new);
        btnSecondary  = n("BtnSecondary", NSButton::new);
        sliderValue   = n("SliderValue", NSSlider::new);
        progressValue = n("ProgressValue", NSProgressBar::new);
        txtInput      = n("TxtInput", NSTextBox::new);
        txtEcho       = n("TxtEcho", NSTextBlock::new);
        lastEvent     = n("LastEvent", NSTextBlock::new);
        linkNoesis    = n("LinkNoesis", NSHyperlink::new);

        // mobs page
        mobSearch = n("MobSearch", NSTextBox::new);
        mobCount  = n("MobCount", NSTextBlock::new);
        mobWrap   = n("MobWrap", NSWrapPanel::new);

        // theme / anim / log
        hue          = n("Hue", NSSlider::new);
        btnApplyTheme= n("BtnApplyTheme", NSButton::new);
        btnPlayAnim  = n("BtnPlayAnim", NSButton::new);
        btnClearLog  = n("BtnClearLog", NSButton::new);
        statsText    = n("StatsText", NSTextBlock::new);
//        logList      = n("LogList");

        var minecraft = Minecraft.getInstance();
        int value = minecraft.getWindow().calculateScale(0, minecraft.isEnforceUnicode());
        hue.setMaximum(value);
        hue.setMinimum(0);

        hue.valueChangedEvent(((nsBaseComponent, args) -> {
            hue.getView().setScale(args.newValue);
        }));

        // --- NAV events ---
        navControls.checkedEvent((c, a) -> selectPage("Controls"));
        navMobs.checkedEvent((c, a) -> selectPage("Mobs"));
        navTheme.checkedEvent((c, a) -> selectPage("Theme"));
        navAnim.checkedEvent((c, a) -> selectPage("Anim"));
        navLog.checkedEvent((c, a) -> selectPage("Log"));

        // --- Controls events (пример) ---
        btnPrimary.clickEvent((c, a) -> setLast("Primary clicked"));
        btnSecondary.clickEvent((c, a) -> setLast("Secondary clicked"));

        sliderValue.valueChangedEvent((c, a) -> {
            float v = sliderValue.getValue();
            progressValue.setValue(v);
            setLast("Slider = " + (int)v);
        });

        txtInput.textChangedEvent((c, a) -> {
            txtEcho.setText("echo: " + txtInput.getText());
        });

        linkNoesis.requestNavigateEvent((c, a) -> {
            setLast("Navigate: " + a.uri);
            System.out.println("Open url: " + a.uri);
        });

        btnClearLog.clickEvent((c, a) -> {
//            logList.getItems().clear();
            setLast("Log cleared");
        });

        btnApplyTheme.clickEvent((c, a) -> {
            setLast("Apply theme (hue=" + (int)hue.getValue() + ")");
        });

        btnPlayAnim.clickEvent((c, a) -> {
            setLast("Play anim");
        });



        fillMobsExample();

        // initial page
        selectPage("Controls");
    }

    private static void setLast(String s) {
        lastEvent.setText(s);

//        logList.getItems().add(s);
    }

    private static void selectPage(String page) {
        if (navGuard) return;
        navGuard = true;

        // скрыть все
        pageControls.setVisibility(NSGui_Visibility.Collapsed);
        pageMobs.setVisibility(NSGui_Visibility.Collapsed);
        pageTheme.setVisibility(NSGui_Visibility.Collapsed);
        pageAnim.setVisibility(NSGui_Visibility.Collapsed);
        pageLog.setVisibility(NSGui_Visibility.Collapsed);

        // снять все чекбоксы
        navControls.setIsChecked(false);
        navMobs.setIsChecked(false);
        navTheme.setIsChecked(false);
        navAnim.setIsChecked(false);
        navLog.setIsChecked(false);

        switch (page) {
            case "Controls" -> { pageControls.setVisibility(NSGui_Visibility.Visible); navControls.setIsChecked(true); titleRight.setText("Controls"); }
            case "Mobs"     -> { pageMobs.setVisibility(NSGui_Visibility.Visible);     navMobs.setIsChecked(true);     titleRight.setText("Mob Tiles"); }
            case "Theme"    -> { pageTheme.setVisibility(NSGui_Visibility.Visible);    navTheme.setIsChecked(true);    titleRight.setText("Themes"); }
            case "Anim"     -> { pageAnim.setVisibility(NSGui_Visibility.Visible);     navAnim.setIsChecked(true);     titleRight.setText("Animations"); }
            case "Log"      -> { pageLog.setVisibility(NSGui_Visibility.Visible);      navLog.setIsChecked(true);      titleRight.setText("Log"); }
        }

        navGuard = false;
    }

    private static void fillMobsExample() {
//        mobWrap.getChildren().clear();

        String[] mobs = { "minecraft:zombie", "minecraft:skeleton", "minecraft:creeper", "minecraft:enderman" };

        for (String id : mobs) {
            var btn = new NSButton();
            btn.setMargin(new NSThickness(6,6,6,6));
            btn.setPadding(new NSThickness(8,8,8,8));

            var sp = new NSStackPanel();
            var name = new NSTextBlock(); name.setText(id);
            name.setTextAlignment(NSGui_TextAlignment.TextAlignment_Center);
            sp.getChildren().add(name);

            btn.setTooltip(id);

            btn.setContent(sp);

            btn.clickEvent((c,a) -> {
                setLast("Spawn: " + id);
                System.out.println("TODO spawn: " + id);
            });

            mobWrap.getChildren().add(btn);
        }

        mobCount.setText(mobs.length + " mobs");
    }
}
