import { View, Text, StyleSheet, useColorScheme } from "react-native";
import { palette } from "../../src/design-system/tokens";
export default function CalendarScreen() {
 const mode=useColorScheme()==="dark"?"dark":"light"; const c=palette[mode];
 return <View style={[s.page,{backgroundColor:c.background}]}><Text style={[s.eyebrow,{color:c.muted}]}>YOUR HISTORY</Text><Text style={[s.title,{color:c.text}]}>Calendar</Text><View style={[s.card,{backgroundColor:c.surface,borderColor:c.border}]}><Text style={[s.month,{color:c.text}]}>November 2026</Text><Text style={{color:c.muted,lineHeight:25}}>Monthly habit heatmap and daily completion history will appear here as tracking data is connected.</Text></View></View>
}
const s=StyleSheet.create({page:{flex:1,padding:24,paddingTop:72,gap:12},eyebrow:{fontSize:10,letterSpacing:1.5,fontWeight:"700"},title:{fontSize:30,fontWeight:"700"},card:{padding:20,borderWidth:1,borderRadius:20,marginTop:12},month:{fontSize:18,fontWeight:"700",marginBottom:10}});