import { ScrollView, Text, View, StyleSheet, useColorScheme } from "react-native";
import { lifeAreas } from "../src/features/life-areas/data";
import { palette, spacing, radius } from "../src/design-system/tokens";

export default function LifeAreasScreen() {
  const mode = useColorScheme() === "dark" ? "dark" : "light";
  const c = palette[mode];
  return (
    <ScrollView style={{ flex: 1, backgroundColor: c.background }} contentContainerStyle={s.page}>
      <Text style={[s.eyebrow, { color: c.muted }]}>YOUR LIFE, IN BALANCE</Text>
      <Text style={[s.title, { color: c.text }]}>Life areas</Text>
      <Text style={[s.subtitle, { color: c.muted }]}>Give attention to the parts of life that matter.</Text>
      {lifeAreas.map(area => (
        <View key={area.id} style={[s.card, { backgroundColor: c.surface, borderColor: c.border }]}>
          <View style={[s.icon, { backgroundColor: area.color + "25" }]}>
            <Text style={{ color: area.color, fontSize: 24 }}>{area.symbol}</Text>
          </View>
          <View style={{ flex: 1 }}>
            <Text style={[s.name, { color: c.text }]}>{area.name}</Text>
            <Text style={[s.description, { color: c.muted }]}>{area.subtitle}</Text>
            <Text style={[s.meta, { color: c.muted }]}>{area.habitCount} habits · {area.completedCount} completed today</Text>
          </View>
          <Text style={{ color: c.muted, fontSize: 20 }}>›</Text>
        </View>
      ))}
    </ScrollView>
  );
}

const s = StyleSheet.create({
  page: { padding: spacing.lg, paddingTop: 72, gap: spacing.md },
  eyebrow: { fontSize: 11, letterSpacing: 1.5, fontWeight: "700" },
  title: { fontSize: 32, fontWeight: "700", marginTop: 2 },
  subtitle: { fontSize: 14, marginBottom: 8 },
  card: { borderWidth: 1, borderRadius: radius.lg, padding: 16, flexDirection: "row", alignItems: "center", gap: 14 },
  icon: { width: 48, height: 48, borderRadius: 16, alignItems: "center", justifyContent: "center" },
  name: { fontSize: 17, fontWeight: "600" },
  description: { fontSize: 12, marginTop: 3 },
  meta: { fontSize: 11, marginTop: 8 }
});