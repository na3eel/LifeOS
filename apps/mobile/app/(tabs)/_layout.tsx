import { Tabs } from "expo-router";
import { Text, useColorScheme } from "react-native";
import { palette } from "../../src/design-system/tokens";

const icons: Record<string, string> = { index: "⌂", "life-areas": "◈", calendar: "▦", insights: "⌁", coach: "✳" };

export default function TabLayout() {
  const mode = useColorScheme() === "dark" ? "dark" : "light";
  const c = palette[mode];
  return (
    <Tabs screenOptions={({ route }) => ({
      headerShown: false,
      tabBarStyle: { backgroundColor: c.tab, borderTopColor: c.border, height: 82, paddingTop: 10, paddingBottom: 22 },
      tabBarActiveTintColor: c.accent,
      tabBarInactiveTintColor: c.muted,
      tabBarLabelStyle: { fontSize: 10, fontWeight: "600" },
      tabBarIcon: ({ color }) => <Text style={{ color, fontSize: 21 }}>{icons[route.name] ?? "·"}</Text>
    })}>
      <Tabs.Screen name="index" options={{ title: "Today" }} />
      <Tabs.Screen name="life-areas" options={{ title: "Areas" }} />
      <Tabs.Screen name="calendar" options={{ title: "Calendar" }} />
      <Tabs.Screen name="insights" options={{ title: "Insights" }} />
      <Tabs.Screen name="coach" options={{ title: "Coach" }} />
    </Tabs>
  );
}