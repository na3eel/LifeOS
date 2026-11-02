import { useColorScheme, View, Text, StyleSheet } from "react-native";

export default function HomeScreen() {
  const dark = useColorScheme() === "dark";
  const colors = dark
    ? { background: "#101310", surface: "#1A201B", text: "#F1F4EF", muted: "#A6B0A5", accent: "#A8C99A" }
    : { background: "#F7F8F4", surface: "#FFFFFF", text: "#202820", muted: "#737D72", accent: "#557D4E" };

  return (
    <View style={[styles.page, { backgroundColor: colors.background }]}>
      <Text style={[styles.eyebrow, { color: colors.muted }]}>MONDAY · YOUR DAY</Text>
      <Text style={[styles.title, { color: colors.text }]}>Make today count.</Text>
      <View style={[styles.card, { backgroundColor: colors.surface }]}>
        <Text style={[styles.label, { color: colors.muted }]}>DAILY PROGRESS</Text>
        <Text style={[styles.progress, { color: colors.text }]}>0<Text style={{ color: colors.muted }}>/ 5</Text></Text>
        <View style={styles.track}><View style={[styles.fill, { backgroundColor: colors.accent, width: "0%" }]} /></View>
        <Text style={[styles.helper, { color: colors.muted }]}>Your first small step starts here.</Text>
      </View>
      <Text style={[styles.section, { color: colors.text }]}>Today</Text>
      <View style={[styles.empty, { borderColor: colors.muted }]}>
        <Text style={{ color: colors.text, fontSize: 16, fontWeight: "600" }}>Your habits will live here</Text>
        <Text style={{ color: colors.muted, marginTop: 6 }}>Create life areas and habits to shape your routine.</Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  page: { flex: 1, paddingHorizontal: 24, paddingTop: 76 },
  eyebrow: { fontSize: 11, letterSpacing: 1.8, fontWeight: "700" },
  title: { fontSize: 32, lineHeight: 39, fontWeight: "700", marginTop: 12, marginBottom: 28 },
  card: { borderRadius: 24, padding: 22, shadowColor: "#000", shadowOpacity: 0.04, shadowRadius: 18, elevation: 2 },
  label: { fontSize: 11, letterSpacing: 1.3, fontWeight: "700" },
  progress: { fontSize: 42, fontWeight: "700", marginTop: 12 },
  track: { height: 7, backgroundColor: "#E5E9E1", borderRadius: 8, overflow: "hidden", marginTop: 16 },
  fill: { height: "100%", borderRadius: 8 },
  helper: { marginTop: 12, fontSize: 13 },
  section: { fontSize: 20, fontWeight: "700", marginTop: 32, marginBottom: 14 },
  empty: { borderWidth: 1, borderStyle: "dashed", borderRadius: 20, padding: 20 }
});