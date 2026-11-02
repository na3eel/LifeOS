import { useMemo, useState } from "react";
import { ScrollView, Text, View, StyleSheet, useColorScheme, Pressable } from "react-native";
import { palette, spacing, radius } from "../../src/design-system/tokens";

const sampleHabits = [
  { id: "1", name: "Morning movement", detail: "Body · 20 minutes", symbol: "◒" },
  { id: "2", name: "Read a few pages", detail: "Mind · 15 minutes", symbol: "✳" },
  { id: "3", name: "Quiet reflection", detail: "Soul · 10 minutes", symbol: "☼" }
];

export default function TodayScreen() {
  const mode = useColorScheme() === "dark" ? "dark" : "light";
  const c = palette[mode];
  const [completed, setCompleted] = useState<string[]>([]);
  const completedCount = completed.length;
  const progress = useMemo(() => Math.round((completedCount / sampleHabits.length) * 100), [completedCount]);

  function toggleHabit(id: string) {
    setCompleted(current => current.includes(id) ? current.filter(item => item !== id) : [...current, id]);
  }

  return (
    <ScrollView style={{ flex: 1, backgroundColor: c.background }} contentContainerStyle={s.page}>
      <View style={s.top}>
        <View>
          <Text style={[s.eyebrow, { color: c.muted }]}>YOUR DAY · TODAY</Text>
          <Text style={[s.title, { color: c.text }]}>Make today count.</Text>
        </View>
        <View style={[s.avatar, { backgroundColor: c.accentSoft }]}><Text style={{ color: c.accent, fontWeight: "700" }}>L</Text></View>
      </View>
      <View style={[s.progressCard, { backgroundColor: c.surface, borderColor: c.border }]}>
        <View style={{ flex: 1 }}>
          <Text style={[s.eyebrow, { color: c.muted }]}>DAILY RHYTHM</Text>
          <Text style={[s.count, { color: c.text }]}>{completedCount}<Text style={{ color: c.muted, fontSize: 22 }}> / {sampleHabits.length}</Text></Text>
          <Text style={[s.muted, { color: c.muted }]}>{completedCount === sampleHabits.length ? "You showed up for yourself today." : "A little progress adds up."}</Text>
        </View>
        <View style={[s.ring, { borderColor: c.accentSoft }]}><Text style={{ color: c.accent, fontSize: 20, fontWeight: "700" }}>{progress}%</Text></View>
      </View>
      <View style={s.sectionRow}><Text style={[s.section, { color: c.text }]}>Today's habits</Text><Text style={{ color: c.accent, fontWeight: "600" }}>Today</Text></View>
      {sampleHabits.map(h => {
        const isDone = completed.includes(h.id);
        return <Pressable key={h.id} onPress={() => toggleHabit(h.id)} accessibilityRole="checkbox" accessibilityState={{ checked: isDone }} accessibilityLabel={h.name}
          style={[s.habit, { backgroundColor: c.surface, borderColor: isDone ? c.accent : c.border }]}>
          <View style={[s.habitIcon, { backgroundColor: c.accentSoft }]}><Text style={{ color: c.accent, fontSize: 19 }}>{h.symbol}</Text></View>
          <View style={{ flex: 1 }}><Text style={[s.habitName, { color: c.text, textDecorationLine: isDone ? "line-through" : "none" }]}>{h.name}</Text><Text style={[s.muted, { color: c.muted }]}>{h.detail}</Text></View>
          <View style={[s.check, { borderColor: isDone ? c.accent : c.border, backgroundColor: isDone ? c.accent : "transparent" }]}>{isDone ? <Text style={s.checkmark}>✓</Text> : null}</View>
        </Pressable>;
      })}
      <View style={s.sectionRow}><Text style={[s.section, { color: c.text }]}>A note to yourself</Text></View>
      <View style={[s.note, { backgroundColor: c.accentSoft }]}><Text style={{ color: c.text, fontSize: 14, lineHeight: 21 }}>Consistency is built one intentional day at a time.</Text></View>
    </ScrollView>
  );
}

const s = StyleSheet.create({
  page: { padding: 22, paddingTop: 68, paddingBottom: 36, gap: 14 },
  top: { flexDirection: "row", alignItems: "center", justifyContent: "space-between", marginBottom: 12 },
  eyebrow: { fontSize: 10, letterSpacing: 1.5, fontWeight: "700" },
  title: { fontSize: 29, fontWeight: "700", marginTop: 9 },
  avatar: { width: 42, height: 42, borderRadius: 21, alignItems: "center", justifyContent: "center" },
  progressCard: { borderWidth: 1, borderRadius: radius.lg, padding: 20, flexDirection: "row", alignItems: "center" },
  count: { fontSize: 38, fontWeight: "700", marginTop: 10 },
  muted: { fontSize: 12, marginTop: 5 },
  ring: { width: 76, height: 76, borderRadius: 38, borderWidth: 7, alignItems: "center", justifyContent: "center" },
  sectionRow: { flexDirection: "row", justifyContent: "space-between", alignItems: "center", marginTop: 20, marginBottom: 3 },
  section: { fontSize: 19, fontWeight: "700" },
  habit: { borderWidth: 1, borderRadius: 18, padding: 13, flexDirection: "row", alignItems: "center", gap: 12 },
  habitIcon: { width: 42, height: 42, borderRadius: 14, alignItems: "center", justifyContent: "center" },
  habitName: { fontSize: 14, fontWeight: "600" },
  check: { width: 24, height: 24, borderWidth: 1.5, borderRadius: 8, alignItems: "center", justifyContent: "center" },
  checkmark: { color: "#FFFFFF", fontSize: 15, fontWeight: "700", lineHeight: 18 },
  note: { padding: 17, borderRadius: 18 }
});
